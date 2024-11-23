package dev.devature.penguin_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "users")
public class AppUser {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    private String name;

    @JsonIgnore
    private String google_auth_token;

    @JsonIgnore
    private String google_refresh_token;

    @JsonIgnore
    private String microsoft_auth_token;

    @JsonIgnore
    private String microsoft_refresh_token;

    @JsonIgnore
    private Timestamp created_at;

    @JsonIgnore
    private Timestamp last_access;

    private String role;

    private String settings;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members")
    private Set<Organization> organizations;

    @OneToMany(mappedBy = "users")
    private List<Member> memberList;

    /**
     * For the use of MockMVC Jackson testing.
     */
    public AppUser() {}

    public AppUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AppUser(Long id, String email, String password, String name, String google_auth_token,
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
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id) && Objects.equals(email, appUser.email) &&
                Objects.equals(password, appUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }
}
