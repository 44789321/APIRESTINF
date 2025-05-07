package com.infomedia.rest.ApiRest.dto;

public class AuthResponseDTO {
    private String message;
    private boolean success;
    private Data data;
    private String token;

    public AuthResponseDTO(String message, boolean success, Long userId, Long roleId, String token) {
        this.message = message;
        this.success = success;
        this.data = new Data(userId, roleId);
        this.token = token;
    }

    public AuthResponseDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // Getters y Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class Data {
        private Long userId;
        private Long roleId;

        // Constructor
        public Data(Long userId, Long roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }

        // Getters y Setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }
    }
}
