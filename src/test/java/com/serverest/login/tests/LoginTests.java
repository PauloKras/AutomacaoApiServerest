package com.serverest.login.tests;

import com.serverest.login.entities.LoginRequest;
import com.serverest.login.entities.LoginResponse;
import com.serverest.login.entities.User;
import com.serverest.login.services.AuthService;
import com.serverest.login.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {

    private AuthService authService;
    private UserService userService;
    private User testUser;

    @BeforeEach
    void setup() {
        authService = new AuthService();
        userService = new UserService();

        String uniqueEmail = "testuser_" + System.currentTimeMillis() + "@example.com";
        testUser = new User("Test User", uniqueEmail, "password123", "false");
        userService.registerUser(testUser);
    }

    @Test
    @DisplayName("Deve realizar login com sucesso com credenciais válidas")
    void shouldLoginSuccessfullyWithValidCredentials() {
        LoginRequest loginRequest = new LoginRequest(testUser.getEmail(), testUser.getPassword());
        LoginResponse loginResponse = authService.login(loginRequest);

        assertNotNull(loginResponse);
        assertEquals("Login realizado com sucesso", loginResponse.getMessage());
        assertNotNull(loginResponse.getAuthorization());
    }

    @Test
    @DisplayName("Não deve realizar login com email inválido")
    void shouldNotLoginWithInvalidEmail() {
        LoginRequest loginRequest = new LoginRequest("invalid@example.com", "password123");
        int statusCode = authService.getLoginStatusCode(loginRequest);

        assertEquals(401, statusCode);
    }

    @Test
    @DisplayName("Não deve realizar login com senha inválido")
    void shouldNotLoginWithInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest(testUser.getEmail(), "wrongpassword");
        int statusCode = authService.getLoginStatusCode(loginRequest);

        assertEquals(401, statusCode);
    }
}