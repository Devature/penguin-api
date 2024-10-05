package dev.devature.penguin_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Setter
@Getter
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

    public User(Long id, String email, String password, String name, String google_auth_token,
                String google_refresh_token, String microsoft_auth_token, String microsoft_refresh_token,
                Timestamp created_at, Timestamp last_access, String role, String settings) {
        this.id = id;
        this.email = email;
        this.password = password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }
}
