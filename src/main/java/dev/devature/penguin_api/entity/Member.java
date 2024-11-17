package dev.devature.penguin_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "organization_id")
    private Organization organization;

    @Column(name="rank_id")
    private Integer rankId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "team")
    private Team team;

    public Member(Long id, User user, Organization organizationId, Integer rankId, Team team) {
        this.id = id;
        this.user = user;
        this.organization = organizationId;
        this.rankId = rankId;
        this.team = team;
    }

    // For MockMVC Jackson
    public Member() { }
}
