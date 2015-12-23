package tleaf.data.jpahibernate;

import org.springframework.stereotype.Repository;
import tleaf.Tweet;
import tleaf.data.TweetRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@org.springframework.context.annotation.Profile("orm-jpa-hibernate")
public class JpaHibTweetRepo implements TweetRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public List<Tweet> findRecentTweets() {
        return findRecentTweets(20);
    }

    @Override
    public List<Tweet> findRecentTweets(int count) {
        return (List<Tweet>) em.createQuery("SELECT s FROM Tweets s ORDER BY s.created_at desc").setMaxResults(count).getResultList();
    }

    @Override
    public Tweet findOne(long tweetId) {
        return em.find(Tweet.class, tweetId);
    }

    @Override
    public Tweet save(Tweet tweet) {
        em.persist(tweet);
        return tweet;
    }

    public List<Tweet> findByProfileId(long profileId) {
        return (List<Tweet>) em.createQuery(
                "SELECT s FROM Tweets s, Profile p " +
                " WHERE s.profile = p and p.id=? " +
                " ORDER BY s.created_at DESC")
                .setParameter(1, profileId)
                .getResultList();
    }

    @Override
    public void delete(long tweetId) {
        em.remove(findOne(tweetId));
    }

    public List<Tweet> findAll() {
        return (List<Tweet>) em.createQuery("SELECT s FROM Tweets s").getResultList();
    }
}
