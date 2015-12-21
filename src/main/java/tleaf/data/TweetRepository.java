package tleaf.data;

import tleaf.Tweet;

import java.util.List;

public interface TweetRepository {

    long count();

    Tweet save(Tweet tweet);

    Tweet findOne(long tweetId);

    List<Tweet> findRecentTweets();

    List<Tweet> findRecentTweets(int count);

    void delete(long tweetId);

}
