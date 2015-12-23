package tleaf.data.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tleaf.Profile;
import tleaf.data.ProfileRepository;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Repository
@org.springframework.context.annotation.Profile("orm-hibernate")
public class HibernateProfileRepository implements ProfileRepository {

    private SessionFactory sessionFactory;

    @Autowired//@Inject
    public HibernateProfileRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
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
    @Override
    public Profile findOne(long id) {
        return (Profile) currentSession().get(Profile.class, id);
    }

    @Override
    public List<Profile> findAll(){
        return (List<Profile>) currentSession()
                .createCriteria(Profile.class)
                .list();
    }


}
