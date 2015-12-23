package tleaf.data.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tleaf.Tweet;
import tleaf.data.TweetRepository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@org.springframework.context.annotation.Profile("jdbc")
public class JdbcTweetRepository implements TweetRepository {
    private JdbcOperations jdbc;

    @Autowired
    public JdbcTweetRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    public long count() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM Tweets", Long.class);
    }

    @Override
    public Tweet save(Tweet tweet) {
        jdbc.update("INSERT INTO Tweets (message, created_at)"+
                " VALUES (?,?)",
                tweet.getMessage(),
                tweet.getCreationDate());
        return tweet;
    }

    @Override
    public Tweet findOne(long tweetId) {
        // OR findByTweetId ?
        return jdbc.queryForObject(
                "SELECT id, message, created_at" +
                        " FROM Tweets" +
                        " WHERE id = ?",
                new TweetRowMapper(), tweetId);
    }

//    @Override
//    public List<Tweet> findRecentTweets(long max, int count) { ... " where id < ?"  , max...

    @Override
    public List<Tweet> findRecentTweets() {
        return jdbc.query(
                "select id, message, created_at" +
                        " FROM Tweets" +
                        " order by created_at desc",
                new TweetRowMapper());
    }

    @Override
    public List<Tweet> findRecentTweets(int count) {
        return jdbc.query(
                "select id, message, created_at" +
                        " FROM Tweets" +
                        " order by created_at desc limit ?",
                new TweetRowMapper(), count);
    }

    private static class TweetRowMapper implements RowMapper<Tweet> {
        public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Tweet(rs.getLong("id"), rs.getString("message"), rs.getDate("created_at"));
        }
    }

    @Override
    public void delete(long tweetId) {
        jdbc.update("DELETE FROM Tweets WHERE id = ?", tweetId);
    }
}
