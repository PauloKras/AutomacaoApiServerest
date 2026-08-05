package com.serverest.login.services;

import com.serverest.login.payloads.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.serverest.login.utils.ConfigurationManager;

public class UserService {

    private static final String BASE_URL = ConfigurationManager.getProperty("api_base_url");
    private static final String USERS_ENDPOINT = "/usuarios";

    public UserService() {
          System.out.println("BASE_URL carregada: " + BASE_URL);

        if (BASE_URL == null || BASE_URL.isBlank()) {
            throw new IllegalStateException("api_base_url não foi carregada do config.properties");
        }
        RestAssured.baseURI = BASE_URL;
    }

    public Response registerUser(User user) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(user)
                .log().all() // Log request
                .when()
                .post(USERS_ENDPOINT)
                .then().log().all() // Log response
                .extract().response(); // Extract response after logging

        return response;
    }
    
    public int getUserRegistrationStatusCode(User user) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(USERS_ENDPOINT);
        return response.getStatusCode();
    }

    public Response getUserById(String id) {
        return RestAssured.given()
                .when()
                .get(USERS_ENDPOINT + "/" + id)
                .then()
                .extract().response();
    }

    public Response updateUser(String id, User user, String token) {
        return RestAssured.given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .put(USERS_ENDPOINT + "/" + id)
                .then()
                .extract().response();
    }

    public Response deleteUser(String id, String token) {
        return RestAssured.given()
                .header("Authorization", token)
                .when()
                .delete(USERS_ENDPOINT + "/" + id)
                .then()
                .extract().response();
    }
}
