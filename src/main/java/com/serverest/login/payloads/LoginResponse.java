package com.serverest.login.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("authorization")
    private String authorization;

    // Default constructor for Jackson
    public LoginResponse() {
    }

    public LoginResponse(String message, String authorization) {
        this.message = message;
        this.authorization = authorization;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}