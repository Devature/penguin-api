package dev.devature.penguin_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User userId;

    @Column(nullable = false, name = "organization_id")
    private Long organizationId;

    @Column(name="rank_id")
    private Integer rankId;

    @Column(name="team")
    private Long teamId;

    public Member(Long id, User userId, Long organizationId, Integer rankId, Long teamId) {
        this.id = id;
        this.userId = userId;
        this.organizationId = organizationId;
        this.rankId = rankId;
        this.teamId = teamId;
    }

    // For MockMVC Jackson
    public Member() { }
}
