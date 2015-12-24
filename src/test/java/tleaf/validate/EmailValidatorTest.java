package tleaf.validate;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import tleaf.entity.Profile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class EmailValidatorTest {

    private static Validator validator;

//    @Before
    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void checkInvalidEmail() throws Exception {
        Profile profile = new Profile("12345", "12345", "123@123");

        Set<ConstraintViolation<Profile>> constraintViolations = validator.validate(profile);
        Assert.assertEquals(1, constraintViolations.size());// size > 0 means that we have constraint violation(s)
        Assert.assertEquals("Please provide a valid email address", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void checkValidationAfterFieldChanging() throws Exception {
        Profile profile = new Profile("12345", "12345", "123@123.com");

        //check Valid Email
        Set<ConstraintViolation<Profile>> constraintViolations = validator.validate(profile);
        Assert.assertEquals(0, constraintViolations.size());

        //updated to Invalid
        profile.setEmail("123@123");
        constraintViolations = validator.validate(profile);
        Assert.assertEquals(1, constraintViolations.size());

        //updated to Valid
        profile.setEmail("123@123.com");
        constraintViolations = validator.validate(profile);
        Assert.assertEquals(0, constraintViolations.size());
    }
}
