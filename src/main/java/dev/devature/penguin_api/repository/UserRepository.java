package dev.devature.penguin_api.repository;

import dev.devature.penguin_api.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<AppUser, Long> {

    /**
     * @param email Takes in an email to process the request.
     * @return Return a {@code User} object from the database.
     */
    public AppUser findByEmail(String email);
}