package dev.devature.penguin_api.repository;

import dev.devature.penguin_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

    @Query("SELECT u.password FROM User u WHERE u.email = :email")
    String getHashedPasswordByEmail(@Param("email") String email);

    /**
     * @param email Takes in an email to process the request.
     * @return Return a {@code User} object from the database.
     */
    public User findByEmail(String email);
}