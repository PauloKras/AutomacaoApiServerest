package com.serverest.login.services;

import com.serverest.login.entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.serverest.login.utils.ConfigurationManager;

public class UserService {

    private static final String BASE_URL = ConfigurationManager.getProperty("api.base.url");
    private static final String USERS_ENDPOINT = "/usuarios";

    public UserService() {
        RestAssured.baseURI = BASE_URL;
    }

    public User registerUser(User user) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(USERS_ENDPOINT);

        return response.as(User.class);
    }
    
    public int getUserRegistrationStatusCode(User user) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(USERS_ENDPOINT);
        return response.getStatusCode();
    }
}