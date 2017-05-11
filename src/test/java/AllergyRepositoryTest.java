/**
 * Created by Kaempe on 24-02-2017.
 */

import junit.framework.Assert;
import models.Allergy;
import org.junit.Test;
import org.sql2o.Sql2o;
import repositories.AllergyRepository;

public class AllergyRepositoryTest {
/*
    public final static String DB_URL = "mysql://80.255.6.114:3306/FindFood_Publisher";
    public final static String DB_USER = "FF_Publisher";
    public final static String DB_PASS = "yQjS6yiA";
    Sql2o sql2o = new Sql2o(DB_URL, DB_USER, DB_PASS);

    @Test//(expected = IllegalArgumentException.class)
    public void testCreateInValidAllergy() {
        //Arrange
        AllergyRepository allergyRepo = new AllergyRepository(sql2o);
        int response;
        Allergy allergy;

        //Act
        allergy = new Allergy("ValidTestAllergy","its a very valid allergy");
        System.out.println(allergy.getAllergyName());
        response = allergyRepo.create(allergy);

        //Assert
        Assert.assertNotSame(response, 0);
    }
/*
    @Test
    public void testCreateAllergyWithSqlInjections() {
        //Arrange
        AllergyRepository allergyRepo = new AllergyRepository(sql2o);
        int response;
        Allergy allergy;

        //Act
        allergy = new Allergy("ValidTestAllergy","its a very valid allergy");
        response = allergyRepo.create(allergy);

        //Assert
        Assert.assertNotSame(response, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAllergyWithInvalidName() {
        //Arrange
        AllergyRepository allergyRepo = new AllergyRepository(sql2o);
        Allergy allergy;

        //Act
        allergy = new Allergy("","its a very bad allergy");
        allergyRepo.create(allergy);

        //Assert
        // Catch IllegalArgumentException on test-tag -> @Test(expected = IllegalArgumentException.class)
    }

    @Test
    public void testFetchAllAllergies() {
        //Arrange
        AllergyRepository allergyRepo = new AllergyRepository(sql2o);
        Allergy allergy;

        //Act
        allergy = new Allergy("","its a very bad allergy");
        allergyRepo.create(allergy);

        //Assert
        // Catch IllegalArgumentException on test-tag -
    }
    */
}
