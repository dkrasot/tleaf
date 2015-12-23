package tleaf.data;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import tleaf.Tweet;

import java.util.List;

public interface TweetRepository {

    long count();

    @CachePut(value = "ehCache", key = "#result.id")
    Tweet save(Tweet tweet);

    @Cacheable("ehCache")
    Tweet findOne(long tweetId);

    List<Tweet> findRecentTweets();

    List<Tweet> findRecentTweets(int count);

    void delete(long tweetId);

}
