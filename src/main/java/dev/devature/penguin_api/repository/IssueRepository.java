package dev.devature.penguin_api.repository;

import dev.devature.penguin_api.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    /**
     * @param id Delete by issue ID.
     * @return The rows that were delete by ID.
     */
    @Query("DELETE FROM Issue i WHERE i.id = :id")
    long removeById(Long id);
}
