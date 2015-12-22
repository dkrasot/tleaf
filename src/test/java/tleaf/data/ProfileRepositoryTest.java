package tleaf.data;

import org.junit.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import tleaf.Profile;
import tleaf.config.DataConfig;
import org.junit.Test;
import tleaf.Profile;
import tleaf.validate.EmailValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.List;

public class ProfileRepositoryTest {

    //@Ignore
    @Test
    public void checkConnectionToDatasource() throws Exception {
        ProfileRepository repo = new JdbcProfileRepository(new JdbcTemplate(new DataConfig().dataSource()));

        Profile profile01 = new Profile("username01","password01","e01@email.com");
        System.out.println("test data 1 - " + profile01);
        Profile profile02 = new Profile("username02","password02","e02@email.com");
        System.out.println("test data 2 - " + profile02);

        //System.out.println("saving 1st: " + repo.save(profile01));

        Assert.assertEquals("Saving of profile01 is OK", profile01, repo.save(profile01));
        Assert.assertEquals("Saving of profile02 is OK", profile02, repo.save(profile02));
        Assert.assertEquals("Count of profiles is valid", 2, repo.count());
        //Assert.assertEquals(profile01, repo.findOne(1L));
        //Assert.assertEquals(profile02, repo.findOne(2L));
        //Assert.assertEquals(profile02, repo.findByUsername("username02"));
        //System.out.println("saving 2nd: " + repo.save(profile02));

        System.out.println("Count of profiles is: " + repo.count());

        System.out.println("1st profile is: "+repo.findOne(1L));
        System.out.println("Profile with un01: "+ repo.findByUsername("username01"));

        System.out.println("View list of profiles:\n");
        List<Profile> listOfAllProfiles = repo.findAll();
        listOfAllProfiles.forEach(profile -> System.out.println("list elem"+profile));

        //Profile checkFindById = repo.findOne(1L);
        //Profile checkFindByUsername = repo.findByUsername("username01");

        //TODO test Hibernate datasources
        //ProfileRepository hiberRepo = new HibernateProfileRepository();


    }

    @Ignore
    @Test(expected = DuplicateKeyException.class)
    public void testDuplicateKeyException() {
        Profile profile01 = new Profile("username01","password01","e01@email.com");
        Profile profile02 = new Profile("username02","password02","e02@email.com");
        ProfileRepository repo2 = new JdbcProfileRepository(new JdbcTemplate(new DataConfig().dataSource()));

        Assert.assertEquals("Saving of profile01 is OK", profile01, repo2.save(profile01));
        System.out.println("Saving 1st again - It must throwing DuplicateKeyException: " + repo2.save(profile01));
    }
}
