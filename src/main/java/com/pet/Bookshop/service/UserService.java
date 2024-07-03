package com.pet.Bookshop.service;

import com.pet.Bookshop.utils.MailUtil;
import com.pet.Bookshop.mapper.UserMapper;
import com.pet.Bookshop.model.dto.SignInDto;
import com.pet.Bookshop.model.dto.SignUpDto;
import com.pet.Bookshop.model.dto.TokenDto;
import com.pet.Bookshop.model.entity.User;
import com.pet.Bookshop.repository.UserRepository;
import com.pet.Bookshop.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    private final MailUtil mailUtil;

    private final EmailService emailService;


    // Регистрация нового пользователя с выдачей JWT-токена
    @Transactional
    public TokenDto registerUser(SignUpDto signUpDto) {
        // Проверяем наличие пользователя с таким email или login в базе данных и соответствие паролей
        validateSignUpDto(signUpDto);

        try {
            log.info("UserService-registerUserAndGetToken: Создаём пользователя с email {}", signUpDto.getEmail());

            User user = userMapper.toUserFromSignUpDto(signUpDto);
            userRepository.save(user);

            log.info("UserService-registerUserAndGetToken: создали юзера его email: {}, дата: {}", user.getEmail(), user.getCreatedDate());

            //создаем JWT-токен
            String jwtToken = jwtUtils.generateJwtToken(user);

            //отправляем письмо на почту о регистрации
            SimpleMailMessage message = mailUtil.createRegistrationMessage(signUpDto);
            emailService.sendSimpleMessage(message);

            return new TokenDto(jwtToken); // Возвращаем JWT-токен Dto


        } catch (RuntimeException e) {
            log.error("UserService-registerUserAndGetToken: Возникла ошибка при регистрации пользователя: ", e);
            throw new RuntimeException("Не удалось зарегистрировать пользователя: ", e);
        }


    }

    //вход
    @Transactional(readOnly = true)
    public TokenDto login(SignInDto signInDto) {
        User userDb = userRepository.findByLogin(signInDto.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("нет пользователя с логином " + signInDto.getLogin()));

        // Проверка учетных данных пользователя
        validateUserCredentials(signInDto, userDb);

        // Аутентификация пользователя
        authenticateUser(signInDto);

        log.info("UserService-login: Вошёл юзер с id: {}", userDb.getId());

        String jwtToken = jwtUtils.generateJwtToken(userDb);
        return new TokenDto(jwtToken);
    }



    public List<SignInDto> getUsers() {
        log.info("UserService-getUsers: Смотрим на всех пользователей");
        //пробегаюсь по листу и делаю из него ДТО а потом опять делаю лист
        return userRepository.findAll().stream()
                .map(userMapper::toSignInDtoFromUser)
                .collect(Collectors.toList());

    }

    //проверки
    private void validateSignUpDto(SignUpDto signUpDto) {
        if (userRepository.existsByEmailOrLogin(signUpDto.getEmail(), signUpDto.getLogin())) {
            throw new RuntimeException("Пользователь с таким email или login уже существует: " + signUpDto.getEmail() + " " + signUpDto.getLogin());
        }
        if (!signUpDto.isPasswordsMatch()) {
            throw new RuntimeException("Пароли должны совпадать");
        }
    }

    // Проверка учетных данных пользователя
    private void validateUserCredentials(SignInDto signInDto, User user) {
        if (!areValidCredentials(signInDto, user)) {
            log.error("UserService-login: Неверный логин или пароль login {} ", signInDto.getLogin());
            throw new RuntimeException("Неверный логин или пароль");
        }
    }

    // Проверка правильности учетных данных
    private boolean areValidCredentials(SignInDto signInDto, User user) {
        return user.isActive() && user.getPassword() != null && signInDto.getPassword() != null &&
                bCryptPasswordEncoder.matches(signInDto.getPassword(), user.getPassword());
    }

    // Аутентификация пользователя
    private void authenticateUser(SignInDto signInDto) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(signInDto.getLogin(), signInDto.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (AuthenticationException e) {
            log.error("UserService-login: ошибка аутентификации пользователя {}", signInDto.getLogin());
            throw e;
        }
    }


}