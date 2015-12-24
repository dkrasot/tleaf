package tleaf.data;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import tleaf.entity.Tweet;

import java.util.List;

public interface TweetRepository {

    long count();

    @CachePut(value = "myCache", key = "#result", unless = "#result == null") //#result.id IS NULL
    Tweet save(Tweet tweet);

    @CacheEvict("myCache")
    void delete(long tweetId);

    // cache disabled IF text contains NoCache && ID <= 10
    @Cacheable(value = "myCache",
            unless = "#result.message.contains('NoCache')",
            condition = "#id > 10")
    Tweet findOne(long id);

    List<Tweet> findRecentTweets();

    List<Tweet> findRecentTweets(int count);

}
