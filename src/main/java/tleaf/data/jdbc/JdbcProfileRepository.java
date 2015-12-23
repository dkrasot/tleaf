package tleaf.data.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tleaf.Profile;
import tleaf.data.ProfileRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@org.springframework.context.annotation.Profile("jdbc")
public class JdbcProfileRepository implements ProfileRepository {
    private JdbcOperations jdbc;

    @Autowired
    public JdbcProfileRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public long count() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM Profile", Long.class);
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
                new ProfileRowMapper(), username);
    }

    @Override
    public Profile findOne(long id) {
        return jdbc.queryForObject("SELECT id, username, password, email FROM Profile WHERE id = ?",
                new ProfileRowMapper(), id);
    }

    @Override
    public List<Profile> findAll() {
        return jdbc.query("SELECT * FROM Profile", new ProfileRowMapper());
    }

    private static class ProfileRowMapper implements RowMapper<Profile> {
        public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Profile(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password"),//TODO hide password? : //null,
                    rs.getString("email"));
        }
    }
}
