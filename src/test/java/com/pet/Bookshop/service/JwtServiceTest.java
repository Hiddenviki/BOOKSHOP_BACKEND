package com.pet.Bookshop.service;

import com.pet.Bookshop.entity.User;
import com.pet.Bookshop.enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private User user;
    private String jwtSecret = "++++===============================testSecretKey===============================++++";
    private int jwtExpirationMs = 3600000;

    @InjectMocks
    private JwtService jwtService;

    @Test
    void testGenerateJwtToken() throws Exception {
        //user
        User user = new User();
        user.setId(1L);
        user.setLogin("testuser");
        user.setEmail("testuser@example.com");
        user.setRole(Roles.GUEST);

        // Устанавливаем поля вручную потому что иначе они не установятся
        ReflectionTestUtils.setField(jwtService, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", jwtExpirationMs);

        // Используем рефлексию для доступа к приватному методу generateKey()
        Method generateKeyMethod = JwtService.class.getDeclaredMethod("generateKey");
        generateKeyMethod.setAccessible(true);
        Key expectedKey = (Key) generateKeyMethod.invoke(jwtService);


        String expectedJwtToken = Jwts.builder()
                .setSubject(user.getLogin())
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("login", user.getLogin())
                .claim("role", user.getRole())
                .setIssuer("vveselkova")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(expectedKey)
                .compact();


        String actualJwtToken = jwtService.generateJwtToken(user);

        //проверяю поля потому что токены будут разные из-за разного времени в new Date(System.currentTimeMillis() + jwtExpirationMs)
        Claims expectedClaims = Jwts.parserBuilder().setSigningKey(expectedKey).build().parseClaimsJws(expectedJwtToken).getBody();
        Claims actualClaims = Jwts.parserBuilder().setSigningKey(expectedKey).build().parseClaimsJws(actualJwtToken).getBody();

        assertEquals(expectedClaims.getSubject(), actualClaims.getSubject());
        assertEquals(expectedClaims.get("id", Long.class), actualClaims.get("id", Long.class));
        assertEquals(expectedClaims.get("email", String.class), actualClaims.get("email", String.class));
        assertEquals(expectedClaims.get("login", String.class), actualClaims.get("login", String.class));
        assertEquals(expectedClaims.get("role", String.class), actualClaims.get("role", String.class));

    }

    @Test
    void testValidateTokenAndRetrieveClaimWithInvalidToken() {
        //создаю невалидный токен
        String invalidToken = "invalid.token.string";

        // Устанавливаем поля вручную потому что иначе они не установятся
        ReflectionTestUtils.setField(jwtService, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", jwtExpirationMs);

        String retrievedClaim = jwtService.validateTokenAndRetrieveClaim(invalidToken);

        assertEquals("", retrievedClaim);
    }

    @Test //такой же как выше но токен с истёкшим сроком (????выдает The provided Algorithm doesn't match the one defined in the JWT's Header.)
    void testValidateTokenAndRetrieveClaimWithExpiredToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        User user = new User();
        user.setId(1L);
        user.setLogin("testuser");
        user.setEmail("testuser@example.com");
        user.setRole(Roles.GUEST);

        // Устанавливаем поля вручную потому что иначе они не установятся
        ReflectionTestUtils.setField(jwtService, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", jwtExpirationMs);

        // Используем рефлексию для доступа к приватному методу generateKey()
        Method generateKeyMethod = JwtService.class.getDeclaredMethod("generateKey");
        generateKeyMethod.setAccessible(true);
        Key expectedKey = (Key) generateKeyMethod.invoke(jwtService);

        Date expirationDate = new Date(System.currentTimeMillis() - 3600*1000); // устанавливаем время истечения на час назад от текущего времени

        // создаём истекший токен
        String expiredToken = Jwts.builder()
                .setSubject(user.getLogin())
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("login", user.getLogin())
                .claim("role", user.getRole())
                .setIssuer("vveselkova")
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(expectedKey)
                .compact();


        ReflectionTestUtils.setField(jwtService, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", jwtExpirationMs);

        String retrievedClaim = jwtService.validateTokenAndRetrieveClaim(expiredToken);

        assertEquals("", retrievedClaim);
    }

    @Test //пустой токен
    void testValidateTokenAndRetrieveClaimWithEmptyToken() {
        String emptyToken = "";

        ReflectionTestUtils.setField(jwtService, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", jwtExpirationMs);

        String retrievedClaim = jwtService.validateTokenAndRetrieveClaim(emptyToken);

        assertEquals("", retrievedClaim);
    }


}