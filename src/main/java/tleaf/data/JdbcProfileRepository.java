package tleaf.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tleaf.Profile;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcProfileRepository implements ProfileRepository {
    private JdbcOperations jdbc;

    @Autowired
    public JdbcProfileRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Profile save(Profile profile) {
        jdbc.update("INSERT INTO Profile (username, password, email) " +
                        " VALUES (?,?,?)",
                profile.getUsername(),
                profile.getPassword(),
                profile.getEmail()
        );
        return profile;
    }

    @Override
    public Profile findByUsername(String username) {
        return jdbc.queryForObject(
                "SELECT id, username, password, email FROM Profile WHERE username = ? ",
                new ProfileRowMapper(),
                username);
    }

    private static class ProfileRowMapper implements RowMapper<Profile> {
        public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Profile(
                    rs.getLong("id"),
                    rs.getString("username"),
                    null,
                    rs.getString("email"));
        }
    }
}
