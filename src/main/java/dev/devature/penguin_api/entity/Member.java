package dev.devature.penguin_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.HashMap;

@Getter
@Setter
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "member")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long user_id;
    private HashMap<Integer, Long> organization;

    private Integer rank_id;
    private Integer team;
}
