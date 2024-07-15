package com.pet.Bookshop.сontroller;

import com.pet.Bookshop.controller.UserController;
import com.pet.Bookshop.dto.SignInDto;
import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.dto.TokenDto;
import com.pet.Bookshop.dto.UserInfoDto;
import com.pet.Bookshop.enums.Roles;
import com.pet.Bookshop.service.MyUserDetailService;
import com.pet.Bookshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private MyUserDetailService myUserDetailService;

    @InjectMocks
    private UserController userController;

    @Test
    void testSignUpAndLogin_WhenValidSignUpDto_ExpectTokenResponse() {
        SignUpDto signUpDto = new SignUpDto("victoryagraz@gmail.com", "naruta2007", "password!", "password!");
        TokenDto expectedTokenResponse = new TokenDto("token"); // Replace with an actual expected token

        when(userService.registerUser(signUpDto)).thenReturn(expectedTokenResponse);

        ResponseEntity<?> responseEntity = userController.signUpAndLogin(signUpDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedTokenResponse, responseEntity.getBody());
    }

    @Test
    void testSignIn_WhenValidSignInDto_ExpectTokenResponse() {
        SignInDto signInDto = new SignInDto(1L, "victoryagraz@gmail.com", Roles.GUEST, "naruta2007", "password!");
        TokenDto expectedTokenResponse = new TokenDto("token"); // Replace with an actual expected token

        when(userService.login(signInDto)).thenReturn(expectedTokenResponse);

        ResponseEntity<?> responseEntity = userController.signIn(signInDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedTokenResponse, responseEntity.getBody());
    }

    @Test
    void testGetUsers_ExpectListOfSignInDto() {
        List<SignInDto> expectedUserList = new ArrayList<>();
        expectedUserList.add(new SignInDto(1L, "user1@example.com", Roles.USER, "user1", "password1"));
        expectedUserList.add(new SignInDto(2L, "user2@example.com", Roles.ADMIN, "user2", "password2"));

        when(userService.getUsers()).thenReturn(expectedUserList);

        List<SignInDto> userList = userController.getUsers();

        assertEquals(expectedUserList, userList);
    }

    @Test
    void testShowUserInfo_ExpectUserInfoDto() {

        UserInfoDto expectedUserInfoDto = new UserInfoDto
                (
                        1L, "testuser", "test@example.com",
                        Roles.GUEST.toString(),
                        LocalDateTime.of(2024, 7, 12, 13, 45, 0),
                        Collections.emptyList()
                );

        when(myUserDetailService.showUserInfo()).thenReturn(expectedUserInfoDto);

        UserInfoDto userInfoDto = userController.showUserInfo();

        assertEquals(expectedUserInfoDto, userInfoDto);
    }
}
