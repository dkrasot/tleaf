package tleaf.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tleaf.entity.Profile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcProfileRepository implements ProfileRepository {
    private JdbcOperations jdbc;
    private final String SELECT_PROFILES = "SELECT * FROM Profile";
    private final String SELECT_COUNT_PROFILES = "SELECT COUNT(*) FROM Profile";
    private final String SELECT_PROFILE_BY_USERNAME = "SELECT id, username, password, email FROM Profile WHERE username = ?";
    private final String SELECT_PROFILE_BY_ID = "SELECT id, username, password, email FROM Profile WHERE id = ?";
    private final String INSERT_PROFILE = "INSERT INTO Profile (username, password, email) VALUES (?,?,?)";

    @Autowired
    public JdbcProfileRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public long count() {
        return jdbc.queryForObject(SELECT_COUNT_PROFILES, Long.class);
    }

    @Override
    public Profile save(Profile profile) {
        jdbc.update(INSERT_PROFILE, profile.getUsername(), profile.getPassword(), profile.getEmail());
        return findByUsername(profile.getUsername()); // now select for GETTING result.ID; WAS: return profile WITHOUT SELECT from REPO
    }

    @Override
    public Profile findByUsername(String username) {
        return jdbc.queryForObject(SELECT_PROFILE_BY_USERNAME, new ProfileRowMapper(), username);
    }

    @Override
    public Profile findOne(long id) {
//        try {
        return jdbc.queryForObject(SELECT_PROFILE_BY_ID, new ProfileRowMapper(), id);
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
    }

    @Override
    public List<Profile> findAll() {
        return jdbc.query(SELECT_PROFILES, new ProfileRowMapper());
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
