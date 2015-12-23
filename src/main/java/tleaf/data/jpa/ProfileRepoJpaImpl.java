package tleaf.data.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ProfileRepoJpaImpl implements ProfileSweeper {

    @PersistenceContext
    private EntityManager em;

    public int eliteSweep() {
        String update = "UPDATE Profile profile " +
                "SET profile.status = 'Elite' " +
                "WHERE profile.status = 'Newbie' " + //TODO add STATUS to Profile table
                "AND profile.id IN (" +
                "SELECT s FROM Profile s WHERE ("+
                " SELECT COUNT(tweets) FROM s.tweets tweets) > 10000" +
                ")";
        return em.createQuery(update).executeUpdate();
    }
}
