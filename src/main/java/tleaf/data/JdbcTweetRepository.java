package tleaf.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tleaf.entity.Tweet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcTweetRepository implements TweetRepository {
    private JdbcOperations jdbc;
    private final String SELECT_COUNT_TWEETS = "SELECT COUNT(*) FROM Tweets";
    private final String SELECT_TWEET_BY_ID = "SELECT id, message, created_at FROM Tweets WHERE id = ?";
    private final String SELECT_TWEETS = "SELECT id, message, created_at FROM Tweets ORDER BY created_at DESC";
    private final String SELECT_TWEETS_LIMIT = SELECT_TWEETS+" LIMIT ?";
    private final String INSERT_TWEET = "INSERT INTO Tweets (message, created_at) VALUES (?,?)";
    private final String DELETE_TWEET = "DELETE FROM Tweets WHERE id = ?";

    @Autowired
    public JdbcTweetRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public long count() {
        return jdbc.queryForObject(SELECT_COUNT_TWEETS, Long.class);
    }

    @Override
    public Tweet save(Tweet tweet) {
        jdbc.update(INSERT_TWEET, tweet.getMessage(), tweet.getCreationDate());
        return tweet;
    }

    @Override
    public void delete(long tweetId) {
        jdbc.update(DELETE_TWEET, tweetId);
    }

    @Override
    public Tweet findOne(long id) { // OR findByTweetId ?
        return jdbc.queryForObject(SELECT_TWEET_BY_ID, new TweetRowMapper(), id);
    }

//    @Override
//    public List<Tweet> findRecentTweets(long max, int count) { ... " where id < ?"  , max...

    @Override
    public List<Tweet> findRecentTweets() {
        return jdbc.query(SELECT_TWEETS, new TweetRowMapper());
    }

    @Override
    public List<Tweet> findRecentTweets(int count) {
        return jdbc.query(SELECT_TWEETS_LIMIT, new TweetRowMapper(), count);
    }

    private static class TweetRowMapper implements RowMapper<Tweet> {
        public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Tweet(rs.getLong("id"), rs.getString("message"), rs.getDate("created_at"));
        }
    }
}
