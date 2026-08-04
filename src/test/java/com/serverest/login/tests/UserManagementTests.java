package com.serverest.login.tests;

import com.serverest.login.payloads.User;
import com.serverest.login.services.UserService;
import com.serverest.login.services.AuthService;
import com.serverest.login.payloads.LoginRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagementTests {

    private UserService userService;
    private User testUser;
    private String adminToken;

    @BeforeEach
    void setup() {
        userService = new UserService();

        // Registrar um usuário administrador para obter o token
        String uniqueEmail = "admin_" + System.nanoTime() + "@example.com";
        User adminUser = new User("Admin User", uniqueEmail, "password123", "true");
        userService.registerUser(adminUser).then().statusCode(201);

        // Fazer login para obter o token de administrador
        LoginRequest loginRequest = new LoginRequest(adminUser.getEmail(), adminUser.getPassword());
        adminToken = new AuthService().login(loginRequest).getAuthorization();
    }

    @AfterEach
    void tearDown() {
        // Limpar dados de teste, se necessário
        // Por exemplo, excluir usuários criados durante os testes
    }

    // Testes de cadastro de usuário /usuários
    @Test
    @DisplayName("Deve cadastrar um novo usuário com sucesso")
    void shouldRegisterUserSuccessfully() {
        String uniqueEmail = "newuser_" + System.nanoTime() + "@example.com";
        User newUser = new User("New User", uniqueEmail, "newpassword123", "false");

        Response response = userService.registerUser(newUser);
        response.then().statusCode(201);

        String message = response.jsonPath().getString("message");
        String _id = response.jsonPath().getString("_id");

        assertEquals("Cadastro realizado com sucesso", message);
        assertNotNull(_id);

        // Excluir o usuário após o teste para limpar o ambiente
        userService.deleteUser(_id, adminToken).then().statusCode(200);
    }

    @Test
    @DisplayName("Não deve cadastrar usuário com email já existente")
    void shouldNotRegisterUserWithExistingEmail() {
        // Primeiro, cadastra um usuário
        String uniqueEmail = "duplicate_" + System.nanoTime() + "@example.com";
        User user1 = new User("User Duplicate", uniqueEmail, "pass123", "false");
        userService.registerUser(user1).then().statusCode(201);

        // Tenta cadastrar outro usuário com o mesmo email
        User user2 = new User("User Duplicate 2", uniqueEmail, "pass456", "false");
        Response response = userService.registerUser(user2);
        response.then().statusCode(400);

        String message = response.jsonPath().getString("message");
        assertEquals("Este email já está sendo usado", message);

        // Excluir o usuário após o teste para limpar o ambiente
        userService.deleteUser(response.jsonPath().getString("_id"), adminToken).then().statusCode(200);
    }


    // Testes de busca de dados de usuário
    @Test
    @DisplayName("Deve buscar dados de usuário por ID com sucesso")
    void shouldGetUserByIdSuccessfully() {
        // Primeiro, cadastra um usuário
        String uniqueEmail = "searchuser_" + System.nanoTime() + "@example.com";
        User userToSearch = new User("Search User", uniqueEmail, "searchpass", "false");
        Response registrationResponse = userService.registerUser(userToSearch);
        registrationResponse.then().statusCode(201);
        String userId = registrationResponse.jsonPath().getString("_id");

        // Busca o usuário pelo ID
        Response searchResponse = userService.getUserById(userId);
        searchResponse.then().statusCode(200);

        User foundUser = searchResponse.as(User.class);
        assertEquals(userToSearch.getNome(), foundUser.getNome());
        assertEquals(userToSearch.getEmail(), foundUser.getEmail());

        // Excluir o usuário após o teste para limpar o ambiente
        userService.deleteUser(userId, adminToken).then().statusCode(200);
    }

    @Test
    @DisplayName("Não deve buscar usuário com ID inexistente")
    void shouldNotGetUserByInvalidId() {
        String invalidId = "nonexistentId123";
        Response response = userService.getUserById(invalidId);
        response.then().statusCode(400);
        assertEquals("Usuário não encontrado", response.jsonPath().getString("message"));
    }


    // Testes de edição de usuário
    @Test
    @DisplayName("Deve editar um usuário existente com sucesso")
    void shouldEditUserSuccessfully() {
        // Primeiro, cadastra um usuário
        String uniqueEmail = "edituser_" + System.nanoTime() + "@example.com";
        User userToEdit = new User("Original Name", uniqueEmail, "editpass", "false");
        Response registrationResponse = userService.registerUser(userToEdit);
        registrationResponse.then().statusCode(201);
        String userId = registrationResponse.jsonPath().getString("_id");

        // Edita o usuário
        String updatedEmail = "edited_" + System.nanoTime() + "@example.com";
        User editedUser = new User("Edited Name", updatedEmail, userToEdit.getPassword(), "true");
        Response editResponse = userService.updateUser(userId, editedUser, adminToken);
        editResponse.then().statusCode(200);

        assertEquals("Registro alterado com sucesso", editResponse.jsonPath().getString("message"));

        // Verifica se as alterações foram aplicadas buscando o usuário
        Response searchResponse = userService.getUserById(userId);
        searchResponse.then().statusCode(200);
        User foundUser = searchResponse.as(User.class);
        assertEquals(editedUser.getNome(), foundUser.getNome());
        assertEquals(editedUser.getEmail(), foundUser.getEmail());
        assertEquals(editedUser.getAdministrador(), foundUser.getAdministrador());

        // Excluir o usuário após o teste para limpar o ambiente
        userService.deleteUser(userId, adminToken).then().statusCode(200);
    }

/* 
    @Test
    @DisplayName("Não deve editar usuário com ID inexistente")
    void shouldNotEditUserWithInvalidId() {
        String invalidId = "nonexistentEditId";
        String uniqueEmail = "editfail_" + System.nanoTime() + "@example.com";
        User editedUser = new User("Fail Edit", uniqueEmail, "pass", "false");

        Response editResponse = userService.updateUser(invalidId, editedUser, adminToken);
        editResponse.then().statusCode(400);
        assertEquals("Usuário não encontrado", editResponse.jsonPath().getString("message"));
    }
*/

    // Testes de exclusão de usuário
    @Test
    @DisplayName("Deve excluir um usuário existente com sucesso")
    void shouldDeleteUserSuccessfully() {
        // Primeiro, cadastra um usuário
        String uniqueEmail = "deleteuser_" + System.nanoTime() + "@example.com";
        User userToDelete = new User("Delete User", uniqueEmail, "deletepass", "false");
        Response registrationResponse = userService.registerUser(userToDelete);
        registrationResponse.then().statusCode(201);
        String userId = registrationResponse.jsonPath().getString("_id");

        // Exclui o usuário
        Response deleteResponse = userService.deleteUser(userId, adminToken);
        deleteResponse.then().statusCode(200);

        assertEquals("Registro excluído com sucesso", deleteResponse.jsonPath().getString("message"));

        // Tenta buscar o usuário excluído para confirmar a exclusão
        Response searchResponse = userService.getUserById(userId);
        searchResponse.then().statusCode(400);
        assertEquals("Usuário não encontrado", searchResponse.jsonPath().getString("message"));
    } 
    

    @Test
    @DisplayName("Não deve excluir usuário com ID inexistente")
    void shouldNotDeleteUserWithInvalidId() {
        String invalidId = "nonexistentDeleteId";
        Response deleteResponse = userService.deleteUser(invalidId, adminToken);
        deleteResponse.then().statusCode(200);
        assertEquals("Nenhum registro excluído", deleteResponse.jsonPath().getString("message"));
    }

}