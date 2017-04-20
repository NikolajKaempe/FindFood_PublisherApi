/**
 * Created by Kaempe on 28-02-2017.
 */
import models.Allergy;
import models.Ingredient;
import org.junit.Assert;
import org.junit.Test;
import org.sql2o.Sql2o;
import repositories.AllergyRepository;
import repositories.IngredientRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IngredientRepositoryTest
{
    /*
    public final static String DB_URL = "jdbc:mysql://80.255.6.114:3306/AirshipOneTesting";
    public final static String DB_USER = "AirshipOneUser";
    public final static String DB_PASS = "123456";
    Sql2o sql2o = new Sql2o(DB_URL, DB_USER, DB_PASS);


    @Test
    public void testFetchSpecificIngredientWithAllergies()
    {
        //Arrange
        IngredientRepository ingredientRepo = new IngredientRepository(sql2o);
        Ingredient ingredient;

        //Act
        ingredient = ingredientRepo.get(1);
        System.out.println("\ningredient name: " + ingredient.getIngredientName());
        System.out.println("ingredient description: " + ingredient.getIngredientDescription());
        for(Allergy allergy : ingredient.getAllergies()){
            System.out.println("\tallergy name: " + allergy.getAllergyName());
            System.out.println("\tallergy description: " + allergy.getAllergyDescription());
        }

        //Assert
        Assert.assertEquals(ingredient.getIngredientName(), "Bread");
    }

    @Test
    public void testUpdateIngredientWithAllergy() {
        //Arrange
        IngredientRepository ingredientRepo = new IngredientRepository(sql2o);
        AllergyRepository allergyRepo = new AllergyRepository(sql2o);
        Allergy allergy;
        Ingredient ingredient;

        //Act
        Collection<Allergy> allergies = new ArrayList<>();
        ingredient = ingredientRepo.get(1);
        allergy = allergyRepo.get(2);
        allergies.add(allergy);
        allergy = allergyRepo.get(3);
        allergies.add(allergy);
        ingredient.setAllergies(allergies);
        System.out.println("Updating ingredient: " + ingredient.getIngredientName());
        ingredientRepo.update(ingredient);
        ingredient = ingredientRepo.get(1);
        System.out.println("\ningredient name: " + ingredient.getIngredientName());
        System.out.println("ingredient description: " + ingredient.getIngredientDescription());

        //Assert
        for(Allergy ingredientAllergy : ingredient.getAllergies()){
            System.out.println("\tallergy name: " + ingredientAllergy.getAllergyName());
            System.out.println("\tallergy description: " + ingredientAllergy.getAllergyDescription());
        }
        Assert.assertEquals(ingredient.getIngredientName(), "Bread");
    }

    @Test
    public void testDeleteIngredientWithAllergy() {
        //Arrange
        IngredientRepository ingredientRepo = new IngredientRepository(sql2o);

        //Act
        boolean validResult = ingredientRepo.delete(2);

        //Assert
        Assert.assertEquals(validResult,true);

    }

    @Test
    public void testFetchAllIngredientsWithAllergies()
    {
        //Arrange
        IngredientRepository ingredientRepo = new IngredientRepository(sql2o);
        Collection<Ingredient> ingredients;

        //Act
        ingredients = ingredientRepo.getAll();
        for ( Ingredient ingredient : ingredients ) {
            System.out.println("\ningredient name: " + ingredient.getIngredientName());
            System.out.println("ingredient description: " + ingredient.getIngredientDescription());
            for(Allergy allergy : ingredient.getAllergies()){
                System.out.println("\tallergy name: " + allergy.getAllergyName());
                System.out.println("\tallergy description: " + allergy.getAllergyDescription());
            }
        }

        //Assert
        Assert.assertNotSame(ingredients.size(), 0);
    }
*/
}
