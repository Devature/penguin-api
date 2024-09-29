package dev.devature.penguin_api.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String salt;

    private String name;

    private String google_auth_token;

    private String google_refresh_token;

    private String microsoft_auth_token;

    private String microsoft_refresh_token;

    private Timestamp created_at;

    private Timestamp last_access;

    private String role;

    private String settings;


    /**
     * For the use of MockMVC Jackson testing.
     */
    User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(Long id, String email, String password, String salt, String name, String google_auth_token,
                String google_refresh_token, String microsoft_auth_token, String microsoft_refresh_token,
                Timestamp created_at, Timestamp last_access, String role, String settings) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.google_auth_token = google_auth_token;
        this.google_refresh_token = google_refresh_token;
        this.microsoft_auth_token = microsoft_auth_token;
        this.microsoft_refresh_token = microsoft_refresh_token;
        this.created_at = created_at;
        this.last_access = last_access;
        this.role = role;
        this.settings = settings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoogle_auth_token() {
        return google_auth_token;
    }

    public void setGoogle_auth_token(String google_auth_token) {
        this.google_auth_token = google_auth_token;
    }

    public String getGoogle_refresh_token() {
        return google_refresh_token;
    }

    public void setGoogle_refresh_token(String google_refresh_token) {
        this.google_refresh_token = google_refresh_token;
    }

    public String getMicrosoft_auth_token() {
        return microsoft_auth_token;
    }

    public void setMicrosoft_auth_token(String microsoft_auth_token) {
        this.microsoft_auth_token = microsoft_auth_token;
    }

    public String getMicrosoft_refresh_token() {
        return microsoft_refresh_token;
    }

    public void setMicrosoft_refresh_token(String microsoft_refresh_token) {
        this.microsoft_refresh_token = microsoft_refresh_token;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getLast_access() {
        return last_access;
    }

    public void setLast_access(Timestamp last_access) {
        this.last_access = last_access;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) && Objects.equals(salt, user.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, salt);
    }
}
