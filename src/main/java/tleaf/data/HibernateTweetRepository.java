package tleaf.data;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import tleaf.Tweet;

import java.io.Serializable;
import java.util.List;

//@Repository
public class HibernateTweetRepository implements TweetRepository {

    private SessionFactory sessionFactory;

    @Autowired//@Inject
    public HibernateTweetRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    private Criteria tweetCriteria() {
        return currentSession()
                .createCriteria(Tweet.class)
                .addOrder(Order.desc("creationDate"));//postedTime
    }

    @Override
    public long count() {
        return findAll().size();
    }

    //... add to interface ?
    public List<Tweet> findAll() {
        return (List<Tweet>) tweetCriteria().list();
    }

    @Override
    public Tweet save(Tweet tweet) {
        Serializable id = currentSession().save(tweet);
        return new Tweet((Long) id,
                tweet.getMessage(),
                tweet.getCreationDate());
    }

    @Override
    public Tweet findOne(long tweetId) {
        return (Tweet) currentSession().get(Tweet.class, tweetId);
    }

    @Override
    public List<Tweet> findRecentTweets() {
        return findRecentTweets(20);
    }

    @Override
    public List<Tweet> findRecentTweets(int count) {
        return (List<Tweet>) tweetCriteria()
                .setMaxResults(count)
                .list();
    }

    @Override
    public void delete(long tweetId) {
        currentSession().delete(findOne(tweetId));
    }
}
