package tleaf.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import tleaf.Profile;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

//@Repository
public class HibernateProfileRepository implements ProfileRepository {

    private SessionFactory sessionFactory;

    @Inject
    public HibernateProfileRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public long count() {
        return findAll().size();
    }

    @Override
    public Profile save(Profile profile) {
        Serializable id = currentSession().save(profile);
        return new Profile((Long) id,
                profile.getUsername(),
                profile.getPassword(),
                profile.getEmail());
    }

    @Override
    public Profile findByUsername(String username) {
        return (Profile) currentSession()
                .createCriteria(Profile.class)
                .add(Restrictions.eq("username", username))
                .list().get(0);
    }

    public List<Profile> findAll(){
        return (List<Profile>) currentSession()
                .createCriteria(Profile.class)
                .list();
    }
}