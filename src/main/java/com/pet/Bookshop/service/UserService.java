package com.pet.Bookshop.service;

import com.pet.Bookshop.mapper.UserMapper;
import com.pet.Bookshop.model.dto.SignInDto;
import com.pet.Bookshop.model.dto.SignUpDto;
import com.pet.Bookshop.model.entity.User;
import com.pet.Bookshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login);
    }

    //регистрация нового пользователя
    public SignInDto createUser(SignUpDto signUpDto) {

        // Проверяем наличие пользователя с таким email или login в базе данных
        if (userRepository.existsByEmailOrLogin(signUpDto.getEmail(),signUpDto.getLogin()) ) {
            throw new RuntimeException("Пользователь с таким email или login уже существует");
        }
        if (!signUpDto.isPasswordsMatch()) {
            throw new RuntimeException("Пароли должны совпадать");
        }
        log.info("UserService-createUser: Создаём юзера c email {}", signUpDto.getEmail());

        //тут происходит @BeforeMapping перед созданием сущности из дто
        User user = userMapper.toUserFromSignUpDto(signUpDto); //создаю сущность из дто

        //дата создания добавляется сама
        userRepository.save(user);
        log.info("UserService-createUser: создали юзера его email: {}, дата: {}", user.getEmail(), user.getCreatedDate());
        return userMapper.toSignInDtoFromUser(user);
    }

    //вход
    public SignInDto login(SignInDto signInDto) {
        User userDb = userRepository.findByLogin(signInDto.getLogin());
        if (
                userDb != null
                && userDb.isActive()
                && userDb.getPassword() != null
                && signInDto.getPassword() != null //сначала пароль потом хэш
                && bCryptPasswordEncoder.matches(signInDto.getPassword(), userDb.getPassword())
        ) {
            log.info("Вошёл юзер с id: {}", userDb.getId());
            return userMapper.toSignInDtoFromUser(userDb);
        }
        else {
            log.info("UserService-login: Неверный пароль или другие ошибки аутентификации для данных login {} password {}", signInDto.getLogin(), signInDto.getPassword());
            throw new RuntimeException("Неверный пароль или другие ошибки аутентификации");
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