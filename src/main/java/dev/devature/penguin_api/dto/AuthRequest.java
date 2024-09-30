package dev.devature.penguin_api.dto;


public class AuthRequest {
    private String authType; // Authentication type: Email, GoogleAuth, MicrosoftAuth, etc.
    private String email; // For email-password auth
    private String password; // For email-password auth or token for OAuth

    public String getAuthType() {
        return this.authType;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthRequest(){}

    public AuthRequest(String authType, String email, String password) {
        this.authType = authType;
        this.email = email;
        this.password = password;
    }
}
