package dev.devature.penguin_api.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "team")
public class Team {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "manager")
    private Member member;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, name = "organization_id")
    private Organization organization;
}
