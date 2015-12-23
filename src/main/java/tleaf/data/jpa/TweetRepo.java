package tleaf.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import tleaf.Tweet;

import java.util.List;

@org.springframework.context.annotation.Profile("orm-jpa")
public interface TweetRepo extends JpaRepository<Tweet, Long>, TweetRepoCustom {
    List<Tweet> findByProfileId(long profileId);
}
