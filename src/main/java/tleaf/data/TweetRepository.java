package tleaf.data;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import tleaf.entity.Tweet;

import java.util.List;

public interface TweetRepository {

    long count();


    //@PreAuthorize("(hasRole('ROLE_USER') AND #tweet.message.length() <= 140) OR hasRole('ROLE_ADMIN')")
    //void addTweet(Tweet tweet);

    @CachePut(value = "myCache", key = "#result", unless = "#result == null") //#result.id IS NULL
    Tweet save(Tweet tweet);

    @CacheEvict("myCache")
    void delete(long tweetId);

    @Secured("ROLE_USER") // = @PreAuthorize("hasRole('ROLE_USER')")
    // cache disabled IF text contains NoCache && ID <= 10
    @Cacheable(value = "myCache",
            unless = "#result.message.contains('NoCache')",
            condition = "#id > 10")
    Tweet findOne(long id);

    //@PostAuthorize("returnObject.profile.username == principal.username") // Profile in Tweet class
    //Tweet findTweetById(long id); //same as findOne

    List<Tweet> findRecentTweets();

    List<Tweet> findRecentTweets(int count);

}
