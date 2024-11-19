package dev.devature.penguin_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "label")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String text;

    @ManyToMany(mappedBy = "labels")
    private Set<Issue> issues;

    public Label(Long id, String text, Set<Issue> issues) {
        this.id = id;
        this.text = text;
        this.issues = issues;
    }
}
