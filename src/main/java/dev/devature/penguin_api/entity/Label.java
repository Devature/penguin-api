package dev.devature.penguin_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "label")
public class Label {
    @Id
    private Long id;

    private String text;

    @ManyToMany(mappedBy = "labels")
    private Set<Issue> issues;
}
