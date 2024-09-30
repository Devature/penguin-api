package dev.devature.penguin_api.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    private final JdbcTemplate jdbcTemplate;

    /**
     * @param jdbcTemplate a JdbcTemplate to use for database access
     */
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @param email the username to lookup in the database
     * @return a single row corresponding to the provided username, or null
     * if the query finds one or more matches, or no matches
     */
    public String getHashedPasswordByUsername(String email) {
        String sql = "SELECT password FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, String.class);
        } catch (Exception e) {
            System.out.println("Error fetching hashed password: " + e.getMessage());
            return null;
        }
    }
}
