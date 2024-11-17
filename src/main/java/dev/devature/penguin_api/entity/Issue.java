package dev.devature.penguin_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="issue")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "issue_label",
            joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name ="label_id")
    )
    private Set<Label> labels;

    @Column(nullable = false)
    private Long column_id;

    @Column(nullable = false)
    private Long organization_id;

    @Column(nullable = false, unique = true)
    private String title;
    private String summary;
    private Integer story_points;
    private Long status_id;

    private Long assignee_id;
    private Timestamp due_date;
    private Long parent_issue_id;

    @Column(nullable = false, updatable = false)
    private Timestamp created_at;
    private Timestamp updated_at;

    @Column(nullable = false, name="created_by")
    private Long createdByID;

    // For MockMVC Jackson
    public Issue() { }

    public Issue(Long id, Long column_id, String title) {
        this.id = id;
        this.column_id = column_id;
        this.title = title;
    }

    public Issue(Long id, Long column_id, String title, String summary, Integer story_points, Long status_id,
                 Long assignee_id, Timestamp due_date, Long parent_issue_id, Timestamp created_at, Timestamp updated_at,
                 Long created_by) {
        this.id = id;
        this.column_id = column_id;
        this.title = title;
        this.summary = summary;
        this.story_points = story_points;
        this.status_id = status_id;
        this.assignee_id = assignee_id;
        this.due_date = due_date;
        this.parent_issue_id = parent_issue_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.createdByID = created_by;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return Objects.equals(id, issue.id) && Objects.equals(column_id, issue.column_id) &&
                Objects.equals(title, issue.title) && Objects.equals(summary, issue.summary) &&
                Objects.equals(story_points, issue.story_points) && Objects.equals(status_id, issue.status_id) &&
                Objects.equals(assignee_id, issue.assignee_id) && Objects.equals(due_date, issue.due_date) &&
                Objects.equals(parent_issue_id, issue.parent_issue_id) && Objects.equals(created_at, issue.created_at)
                && Objects.equals(updated_at, issue.updated_at) && Objects.equals(createdByID, issue.createdByID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, column_id, title, summary, story_points, status_id, assignee_id, due_date,
                parent_issue_id, created_at, updated_at, createdByID);
    }
}
