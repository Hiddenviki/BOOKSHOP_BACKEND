package com.pet.Bookshop.service;

import com.pet.Bookshop.mapper.UserMapper;
import com.pet.Bookshop.model.dto.SignInDto;
import com.pet.Bookshop.model.dto.SignUpDto;
import com.pet.Bookshop.model.entity.User;
import com.pet.Bookshop.repository.UserRepository;
import com.pet.Bookshop.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    //проверки
    private void validateSignUpDto(SignUpDto signUpDto) {
        if (userRepository.existsByEmailOrLogin(signUpDto.getEmail(), signUpDto.getLogin())) {
            throw new RuntimeException("Пользователь с таким email или login уже существует: " + signUpDto.getEmail() + " " + signUpDto.getLogin());
        }
        if (!signUpDto.isPasswordsMatch()) {
            throw new RuntimeException("Пароли должны совпадать");
        }
    }

    // Регистрация нового пользователя с выдачей JWT-токена
    public Map<String, String> registerUserAndGetToken(SignUpDto signUpDto) {
        // Проверяем наличие пользователя с таким email или login в базе данных и соответствие паролей
        validateSignUpDto(signUpDto);

        try {
            log.info("UserService-registerUserAndGetToken: Создаём пользователя с email {}", signUpDto.getEmail());

            User user = userMapper.toUserFromSignUpDto(signUpDto);
            userRepository.save(user);

            log.info("UserService-registerUserAndGetToken: создали юзера его email: {}, дата: {}", user.getEmail(), user.getCreatedDate());

            //создаем JWT-токен
            String jwtToken = jwtUtils.generateJwtToken(user);
            // Возвращаем JWT-токен
            return Map.of("jwt-token", jwtToken);
        } catch (Exception e) {
            log.error("UserService-registerUserAndGetToken: Возникла ошибка при регистрации пользователя: " + e);
            throw new RuntimeException("Не удалось зарегистрировать пользователя: "+e);
        }
    }

    //вход
    public Map<String, String> login(SignInDto signInDto) {
        User userDb = userRepository.findByLogin(signInDto.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("нет пользователя с логином " + signInDto.getLogin()));
        if (
                userDb.isActive()
                        && userDb.getPassword() != null
                        && signInDto.getPassword() != null
                        //сначала пароль потом хэш
                        && bCryptPasswordEncoder.matches(signInDto.getPassword(), userDb.getPassword())
        ) {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(signInDto.getLogin(),
                            signInDto.getPassword());

            try {
                authenticationManager.authenticate(authInputToken);
            } catch (BadCredentialsException e) {
                log.info("UserService-login: ошибка аутентификации пользователя {}", signInDto.getLogin());
                throw new RuntimeException(e);
            }
            log.info("UserService-login: Вошёл юзер с id: {}", userDb.getId());

            String jwtToken = jwtUtils.generateJwtToken(userDb);
            return Map.of("jwt-token", jwtToken);
        } else {
            log.info("UserService-login: Неверный логин или пароль login {} password {}", signInDto.getLogin(), signInDto.getPassword());
            throw new RuntimeException("Неверный логин или пароль");
        }
    }

    public List<SignInDto> getUsers() {
        log.info("UserService-getUsers: Смотрим на всех пользователей");
        //пробегаюсь по листу и делаю из него ДТО а потом опять делаю лист
        return userRepository.findAll().stream()
                .map(userMapper::toSignInDtoFromUser)
                .collect(Collectors.toList());

    }


}