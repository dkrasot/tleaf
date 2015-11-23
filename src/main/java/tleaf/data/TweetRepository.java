package tleaf.data;

import tleaf.Tweet;

import java.util.List;

public interface TweetRepository {

    List<Tweet> findTweets(long max, int count);

    Tweet findOne(long tweetId);

    void save(Tweet tweet);
}
