package tleaf.data;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import tleaf.Tweet;

import java.util.List;

public interface TweetRepository {

    long count();

    @CachePut(value = "ehCache", key = "#result.id")
    Tweet save(Tweet tweet);

    @CacheEvict("ehCache")
    void delete(long tweetId);

    // cache disabled IF text contains NoCache && ID <= 10
    @Cacheable(value = "ehCache",
            unless = "#result.message.contains('NoCache')",
            condition = "#id > 10")
    Tweet findOne(long id);

    @Cacheable("ehCache")
    List<Tweet> findRecentTweets();

    @Cacheable("ehCache")
    List<Tweet> findRecentTweets(int count);

}
