package dev.devature.penguin_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @Setter
    private Users owner;

    @Setter
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "organization_members",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id"))
    private Set<Users> members;

    public Organization() {
        this.members = new HashSet<>();
    }
}
