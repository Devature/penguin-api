package dev.devature.penguin_api.repository;

import dev.devature.penguin_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

    /**
     * @param email Takes in an email to process the request.
     * @return Return a {@code User} object from the database.
     */
    public User findByEmail(String email);
}