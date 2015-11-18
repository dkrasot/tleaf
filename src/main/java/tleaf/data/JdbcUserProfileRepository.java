package tleaf.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tleaf.UserProfile;


import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcUserProfileRepository implements UserProfileRepository {
    private JdbcOperations jdbc;

    @Autowired
    public JdbcUserProfileRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        jdbc.update("INSERT INTO UserProfile (username, password, email) "+
                    " VALUES (?,?,?)",
                userProfile.getUsername(),
                userProfile.getPassword(),
                userProfile.getEmail()
        );
        return userProfile;
    }

    @Override
    public UserProfile findByUsername(String username) {
        return jdbc.queryForObject(
                "SELECT id, username, password, email FROM UserProfile WHERE username = ? ",
                new UserProfileRowMapper(),
                username);
    }

    private static class UserProfileRowMapper implements RowMapper<UserProfile> {
        public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserProfile(
                    rs.getLong("id"),
                    rs.getString("username"),
                    null,
                    rs.getString("email"));
        }
    }
}
