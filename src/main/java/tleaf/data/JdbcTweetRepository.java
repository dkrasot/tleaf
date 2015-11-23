package tleaf.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tleaf.Tweet;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcTweetRepository implements TweetRepository {
    private JdbcOperations jdbc;

    @Autowired
    public JdbcTweetRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void save(Tweet tweet) {
        jdbc.update("INSERT INTO Tweets (message, created_at)"+
                " VALUES (?,?)",
                tweet.getMessage(),
                tweet.getCreationDate());
    }

    @Override
    public Tweet findOne(long tweetId) {
        return jdbc.queryForObject(
                "SELECT id, message, created_at" +
                        " FROM Tweets" +
                        " WHERE id = ?",
                new TweetRowMapper(), tweetId);
    }

    @Override
    public List<Tweet> findTweets(long max, int count) {
        return jdbc.query(
                "select id, message, created_at" +
                        " from Tweets" +
                        " where id < ?" +
                        " order by created_at desc limit ?",
// TODO? Get away hardcode + check count >0 ??limit 20 -> limit count??
                new TweetRowMapper(), max, count);
    }

    private static class TweetRowMapper implements RowMapper<Tweet> {
        public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Tweet(rs.getLong("id"), rs.getString("message"), rs.getDate("created_at"));
        }
    }
}
