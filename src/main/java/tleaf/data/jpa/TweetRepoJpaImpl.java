package tleaf.data.jpa;

import tleaf.Tweet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class TweetRepoJpaImpl implements TweetRepoCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Tweet> findRecent() {
        return findRecent(20);
    }

    @Override
    public List<Tweet> findRecent(int count) {
        return (List<Tweet>) em.createQuery("SELECT s FROM Tweets s ORDER BY s.creationDate");
    }
}
