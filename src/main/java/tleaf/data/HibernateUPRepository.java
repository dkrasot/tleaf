package tleaf.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import tleaf.UserProfile;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

//@Repository
public class HibernateUPRepository implements UserProfileRepository {

    private SessionFactory sessionFactory;

    @Inject
    public HibernateUPRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public long count() {
        return findAll().size();
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        Serializable id = currentSession().save(userProfile);
        return new UserProfile((Long) id,
                userProfile.getUsername(),
                userProfile.getPassword(),
                userProfile.getEmail());
    }

    @Override
    public UserProfile findByUsername(String username) {
        return (UserProfile) currentSession()
                .createCriteria(UserProfile.class)
                .add(Restrictions.eq("username", username))
                .list().get(0);
    }

    public List<UserProfile> findAll(){
        return (List<UserProfile>) currentSession()
                .createCriteria(UserProfile.class)
                .list();
    }
}
