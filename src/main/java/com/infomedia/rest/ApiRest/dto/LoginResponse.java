package com.infomedia.rest.ApiRest.dto;

public class LoginResponse {
    private String message;
    private boolean success;
    private String username;

    // Constructor
    public LoginResponse(String message, boolean success, String username) {
        this.message = message;
        this.success = success;
        this.username = username;
    }

    // Getters y setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}