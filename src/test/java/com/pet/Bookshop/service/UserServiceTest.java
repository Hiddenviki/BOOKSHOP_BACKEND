package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.SignInDto;
import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.dto.TokenDto;
import com.pet.Bookshop.entity.User;
import com.pet.Bookshop.mapper.UserMapper;
import com.pet.Bookshop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private JwtService jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private MailService mailService;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUsers() {

        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("Veselkova.v@yandex.ru.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("victoryagraz@gmail.com");

        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        SignInDto signInDto1 = new SignInDto();
        signInDto1.setId(1L);
        signInDto1.setEmail("example1@example.com");

        SignInDto signInDto2 = new SignInDto();
        signInDto2.setId(2L);
        signInDto2.setEmail("example2@example.com");

        when(userMapper.toSignInDtoFromUser(user1)).thenReturn(signInDto1);
        when(userMapper.toSignInDtoFromUser(user2)).thenReturn(signInDto2);

        List<SignInDto> result = userService.getUsers();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("example1@example.com", result.get(0).getEmail());
        assertEquals(2L, result.get(1).getId());
        assertEquals("example2@example.com", result.get(1).getEmail());
    }

    @Test//нет проверок для приватных методов
    void testLoginSuccess() {

        SignInDto signInDto = new SignInDto();
        signInDto.setLogin("exampleLogin");
        signInDto.setPassword("examplePassword");

        User user = new User();
        user.setId(1L);
        user.setLogin(signInDto.getLogin());
        user.setPassword("$2y$08$zNDXxJeYuI8.ejCEftbZHeYVyCnbaI4nfzmIeDlJ.HCEqLhFFA0kW");
        user.setActive(true);

        when(userRepository.findByLogin(signInDto.getLogin())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(signInDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateJwtToken(user)).thenReturn("mockedJwtToken");

        TokenDto actual = userService.login(signInDto);

        assertEquals("mockedJwtToken", actual.getJwtToken());
    }

    @Test
    void testLoginUserNotFound() {
        SignInDto signInDto = new SignInDto();
        signInDto.setLogin("noSuchLogin");
        when(userRepository.findByLogin(signInDto.getLogin())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.login(signInDto)
        );
        assertEquals("нет пользователя с логином " + signInDto.getLogin(), exception.getMessage());
    }

    @Test
    void testRegisterUserSuccess() {
        // Подготовка данных
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setLogin("newUser");
        signUpDto.setEmail("newuser@example.com");
        signUpDto.setPassword1("examplePassword");
        signUpDto.setPassword2("examplePassword");

        User user = new User();
        user.setId(1L);
        user.setLogin(signUpDto.getLogin());
        user.setEmail(signUpDto.getEmail());

        SimpleMailMessage mockMessage = new SimpleMailMessage();
        mockMessage.setFrom("test@example.com");
        mockMessage.setTo("recipient@example.com");
        mockMessage.setSubject("Testing");
        mockMessage.setText("This is a test email.");
        String encodedPassword = bCryptPasswordEncoder.encode(signUpDto.getPassword1());

        when(userRepository.existsByEmailOrLogin(any(), any())).thenReturn(false);
        when(userMapper.toUserFromSignUpDto(signUpDto)).thenReturn(user);
        lenient().when(bCryptPasswordEncoder.encode(signUpDto.getPassword1())).thenReturn(encodedPassword); //Unnecessary stubbings detected поэтому lenient()

        //сохраняется юзер и берется токен
        when(userRepository.save(user)).thenReturn(user);
        when(jwtUtil.generateJwtToken(user)).thenReturn("mockedJwtToken");
        // тут составляется any письмо для регистрации
        when(mailService.createRegistrationMessage(signUpDto)).thenReturn(mockMessage);
        // просто ничего не делаем
        doNothing().when(emailService).sendSimpleMessage(mockMessage);

        //сам целевой метод
        TokenDto actual = userService.registerUser(signUpDto);

        //проверки
        assertEquals("mockedJwtToken", actual.getJwtToken());
        assertEquals(signUpDto.getLogin(), user.getLogin());
        assertEquals(signUpDto.getEmail(), user.getEmail());
    }

    @Test
    void testRegisterUserDuplicateUser() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setLogin("existingUser");
        signUpDto.setEmail("existinguser@example.com");
        when(userRepository.existsByEmailOrLogin(signUpDto.getEmail(), signUpDto.getLogin())).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.registerUser(signUpDto)
        );

        assertEquals("Пользователь с таким email или login уже существует: " + signUpDto.getEmail() + " " + signUpDto.getLogin(), exception.getMessage());
    }


}


