import models.Allergy;
import models.MeasuredIngredient;
import models.Recipe;
import models.RecipeType;
import org.junit.Assert;
import org.junit.Test;
import org.sql2o.Sql2o;
import repositories.RecipeRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kaempe on 21-03-2017.
 */
public class RecipeRepositoryTest {
/*
    public final static String DB_URL = "jdbc:mysql://80.255.6.114:3306/AirshipOne";
    public final static String DB_USER = "AirshipOneUser";
    public final static String DB_PASS = "123456";
    Sql2o sql2o = new Sql2o(DB_URL, DB_USER, DB_PASS);

    @Test
    public void testCreateValidRecipe(){
        //Arrange
        Recipe recipe;
        RecipeType recipeType;
        Collection<MeasuredIngredient> ingredients = new ArrayList<>();
        Collection<Allergy> allergyRelations;
        RecipeRepository recipeRepository = new RecipeRepository(sql2o);
        int recipeId;

        //Act
        recipeType = new RecipeType(2,"dosent matter for relations");
        ingredients.add(new MeasuredIngredient(50,"Gram",2,"ingredientNameDoesNotMatterHere","nor does the description"));
        ingredients.add(new MeasuredIngredient(10,"Stk",3,"ingredientNameDoesNotMatterHere","nor does the description"));
        recipe = new Recipe("UnitTestRecipe","Cool Recipe", "SomePicture", recipeType, ingredients);

        recipeId = recipeRepository.create(recipe);
        System.out.println("Recipe created with id " + recipeId);
        allergyRelations = recipeRepository.getAllergiesFor(recipeId);

        //Assert
        for (Allergy allergy: allergyRelations) {
            System.out.println(allergy.getAllergyName() + " has id " + allergy.getAllergyId());
        }

        Assert.assertNotEquals(allergyRelations.size(),0);
    }

    /*

    @Test
    public void testFetchAllRecipesWithRecipeType(){
        //Arrange
        RecipeRepository recipeRepository = new RecipeRepository(sql2o);
        Collection<Recipe> recipes;

        //Act
        recipes = recipeRepository.getAll();
        for (Recipe recipe : recipes){

            System.out.println("\nrecipe name: " + recipe.getRecipeName());
            System.out.println("recipe description: " + recipe.getRecipeDescription());
            System.out.println("recipe imageFilePath: " + recipe.getRecipeImageFilePath());
            System.out.println("recipeType: " + recipe.getRecipeType().getRecipeTypeName());

            for (MeasuredIngredient measuredIngredient : recipe.getMeasuredIngredients()){
                System.out.println("\tingredient name:" + measuredIngredient.getIngredient().getIngredientName());
                System.out.println("\tingredient measure:" + measuredIngredient.getMeasure());
                System.out.println("\tingredient amount" + measuredIngredient.getAmount());
            }
        }

        //Assert
        Assert.assertEquals(recipes.size(), 2);
    }

    @Test
    public void testFetchSpecificRecipeWithRecipeType(){
        //Arrange
        RecipeRepository recipeRepository = new RecipeRepository(sql2o);
        Recipe recipe;

        //Act
        recipe = recipeRepository.get(1);
        System.out.println("\nrecipe name: " + recipe.getRecipeName());
        System.out.println("recipe description: " + recipe.getRecipeDescription());
        System.out.println("recipe imageFilePath: " + recipe.getRecipeImageFilePath());
        System.out.println("recipeType: " + recipe.getRecipeType().getRecipeTypeName());

        for (MeasuredIngredient measuredIngredient : recipe.getMeasuredIngredients()){
            System.out.println("\tingredient name:" + measuredIngredient.getIngredient().getIngredientName());
            System.out.println("\tingredient measure:" + measuredIngredient.getMeasure());
            System.out.println("\tingredient amount" + measuredIngredient.getAmount());
        }

        recipe = recipeRepository.get(2);
        System.out.println("\nrecipe name: " + recipe.getRecipeName());
        System.out.println("recipe description: " + recipe.getRecipeDescription());
        System.out.println("recipe imageFilePath: " + recipe.getRecipeImageFilePath());
        System.out.println("recipeType: " + recipe.getRecipeType().getRecipeTypeName());

        for (MeasuredIngredient measuredIngredient : recipe.getMeasuredIngredients()){
            System.out.println("\tingredient name:" + measuredIngredient.getIngredient().getIngredientName());
            System.out.println("\tingredient measure:" + measuredIngredient.getMeasure());
            System.out.println("\tingredient amount" + measuredIngredient.getAmount());
        }
        //Assert
        Assert.assertEquals(recipe.getRecipeName(), "Nasty Recipe");
        Assert.assertEquals(recipe.getRecipeType().getRecipeTypeName(), "Brunch");
    }
    */
}
