package dev.devature.penguin_api.dto;


public class AuthRequest {
    private String authType; // Authentication type: username_password, GoogleAuth, MicrosoftAuth, etc.
    private String username; // For username-password auth
    private String password; // For username-password auth or token for OAuth

    public String getAuthType() {
        return this.authType;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    AuthRequest(String authType, String username, String password) {
        this.authType = authType;
        this.username = username;
        this.password = password;
    }
}
