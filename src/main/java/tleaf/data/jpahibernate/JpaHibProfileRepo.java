package tleaf.data.jpahibernate;

import org.springframework.stereotype.Repository;
import tleaf.Profile;
import tleaf.data.ProfileRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@org.springframework.context.annotation.Profile("orm-jpa-hibernate")
public class JpaHibProfileRepo implements ProfileRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public Profile save(Profile profile) {
        em.persist(profile);
        return profile;
    }

    @Override
    public Profile findOne(long id) {
        return em.find(Profile.class, id);
    }

    @Override
    public Profile findByUsername(String username) {
        return (Profile) em.createQuery("SELECT s FROM Profile s WHERE s.username=?").setParameter(1, username).getSingleResult();
    }

    @Override
    public List<Profile> findAll() {
        return (List<Profile>) em.createQuery("SELECT s FROM Profile s").getResultList();
    }
}
