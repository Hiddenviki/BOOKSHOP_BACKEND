package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.SignInDto;
import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.dto.TokenDto;
import com.pet.Bookshop.dto.UserInfoDto;
import com.pet.Bookshop.entity.PostponedEmail;
import com.pet.Bookshop.entity.User;
import com.pet.Bookshop.enums.Roles;
import com.pet.Bookshop.mapper.UserInfoMapper;
import com.pet.Bookshop.mapper.UserMapper;
import com.pet.Bookshop.repository.PostponedEmailRepository;
import com.pet.Bookshop.repository.UserRepository;
import com.pet.Bookshop.security.MyUserDetails;
import com.pet.Bookshop.security.UUIDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserInfoMapper userInfoMapper;
    private final UUIDGenerator uuidGenerator;
    private final PostponedEmailRepository postponedEmailRepository;


    // Регистрация нового пользователя с выдачей JWT-токена
    @Transactional
    public TokenDto registerUser(SignUpDto signUpDto) {
        validateSignUpDto(signUpDto);

        log.info("UserService-registerUser: Создание пользователя с email: {}", signUpDto.getEmail());

        User user = userMapper.toUserFromSignUpDto(signUpDto);
        userRepository.save(user);

        try {
            sendRegistrationEmail(signUpDto);
        } catch (MailException exception) {
            log.error("UserService-registerUser: Ошибка отправки письма при регистрации");
            createPostponedRegistrationEmail(signUpDto);
        }

        String jwtToken = jwtUtil.generateJwtToken(user);

        return new TokenDto(jwtToken);
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

        String jwtToken = jwtUtil.generateJwtToken(userDb);

        return new TokenDto(jwtToken);
    }

    public List<SignInDto> getUsers() {
        log.info("UserService-getUsers: Смотрим на всех пользователей");
        //пробегаюсь по листу и делаю из него ДТО а потом опять делаю лист
        return userRepository.findAll().stream()
                .map(userMapper::toSignInDtoFromUser).toList();
    }

    public UserInfoDto verifyRegistration(String givenUuid) {
        User user = getCurrentUser();

        validateRole(user, givenUuid);

        user.setRole(Roles.USER);
        User savedUser = userRepository.save(user);

        log.info("Регистрация пользователя с Id {} подтверждена с ролью {}", savedUser.getId(), savedUser.getRole());

        return userInfoMapper.toUserInfoDto(savedUser);
    }

    public void checkAndChangeUserRole() {
        User user = getCurrentUser();

        if (user.getRole() == Roles.USER) {
            user.setRole(Roles.AUTHOR);
            userRepository.save(user);
        }
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

    //отправка регистрационного сообщения
    private void sendRegistrationEmail(SignUpDto signUpDto) {
        SimpleMailMessage message = emailService.createRegistrationMessage(signUpDto);
        emailService.sendSimpleMessage(message);
    }

    //создание отложенного письма при проблемах с отправкой
    private void createPostponedRegistrationEmail(SignUpDto signUpDto) {
        PostponedEmail postponedEmail = new PostponedEmail();
        postponedEmail.setLogin(signUpDto.getLogin());
        postponedEmail.setTopic("Registration");
        postponedEmailRepository.save(postponedEmail);
    }

    private void validateRole(User user, String givenUuid) {
        if (user.getRole() != Roles.GUEST) {
            log.error("Пользователь {} уже подтвердил регистрацию с ролью '{}'", user.getLogin(), user.getRole());
            throw new IllegalStateException("Регистрация уже подтверждена");
        }

        String generatedUuid = uuidGenerator.generateUUIDFromValue(user.getLogin()).toString();

        if (!generatedUuid.equals(givenUuid)) {
            log.error("UUID не совпадают для пользователя с логином {} - Expected: '{}', Given: '{}'", user.getLogin(), generatedUuid, givenUuid);
            throw new IllegalArgumentException("UUIDs do not match");
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}