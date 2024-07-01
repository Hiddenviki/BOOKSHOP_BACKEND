package com.pet.Bookshop.configuration.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pet.Bookshop.configuration.security.userdetails.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private MyUserDetailService myUserDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader("Authorization");

        // Проверяем, есть ли заголовок "Authorization" и начинается ли он с "Bearer ", что означает передачу токена
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Недопустимый JWT-токен в заголовке Bearer");
            } else {
                try {
                    // Проверяем и извлекаем из токена у меня там логин
                    String login = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    // Получаем информацию о пользователе по его логину
                    UserDetails userDetails = myUserDetailService.loadUserByUsername(login);

                    // Создаем аутентификационный токен и устанавливаем его в контекст безопасности, если он еще не установлен
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        //отсюда можно получить текущего юзера
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException exc) {
                    // В случае ошибки валидации токена отправляем сообщение об ошибке клиенту
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Недопустимый JWT-токен");
                }
            }
        }

        // Пропускаем запрос через цепочку фильтров
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}