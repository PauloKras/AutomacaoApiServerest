package com.serverest.login.services;

import com.serverest.login.payloads.LoginRequest;
import com.serverest.login.payloads.LoginResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.serverest.login.utils.ConfigurationManager;

public class AuthService {

    private static final String BASE_URL = ConfigurationManager.getProperty("api_base_url");
    private static final String LOGIN_ENDPOINT = "/login";

    public AuthService() {
        RestAssured.baseURI = BASE_URL;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post(LOGIN_ENDPOINT);

        return response.as(LoginResponse.class);
    }
    
    public int getLoginStatusCode(LoginRequest loginRequest) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post(LOGIN_ENDPOINT);
        return response.getStatusCode();
    }
}