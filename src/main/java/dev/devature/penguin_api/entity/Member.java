package dev.devature.penguin_api.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(nullable = false, name = "organization_id")
    private Organization organization;

    @Column(name="rank_id")
    private Integer rankId;

    public Member(Long id, AppUser appUser, Organization organizationId, Integer rankId) {
        this.id = id;
        this.appUser = appUser;
        this.organization = organizationId;
        this.rankId = rankId;
    }

    // For MockMVC Jackson
    public Member() { }
}
