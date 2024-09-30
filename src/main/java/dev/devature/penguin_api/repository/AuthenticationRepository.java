package dev.devature.penguin_api.repository;

import dev.devature.penguin_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<User, Long> {

    @Query("SELECT password FROM user WHERE username = :username")
    String getHashedPasswordByUsername(@Param("username") String username);

}
