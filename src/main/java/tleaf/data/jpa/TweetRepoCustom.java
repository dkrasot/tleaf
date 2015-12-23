package tleaf.data.jpa;

import tleaf.Tweet;

import java.util.List;

public interface TweetRepoCustom {

    List<Tweet> findRecent();
    List<Tweet> findRecent(int count);
}
