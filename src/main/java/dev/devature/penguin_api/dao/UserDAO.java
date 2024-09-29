package dev.devature.penguin_api.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getHashedPasswordByUsername(String username) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, String.class);
        } catch (Exception e) {
            System.out.println("Error fetching hashed password: " + e.getMessage());
            return null;
        }
    }
}
