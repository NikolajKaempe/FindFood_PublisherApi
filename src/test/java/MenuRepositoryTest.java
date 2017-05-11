import models.MealType;
import models.Menu;
import models.Recipe;
import org.junit.Assert;
import org.junit.Test;
import org.sql2o.Sql2o;
import repositories.MenuRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kaempe on 28-03-2017.
 */
public class MenuRepositoryTest
{
    /*
    public final static String DB_URL = "jdbc:mysql://80.255.6.114:3306/AirshipOne";
    public final static String DB_USER = "AirshipOneUser";
    public final static String DB_PASS = "123456";
    Sql2o sql2o = new Sql2o(DB_URL, DB_USER, DB_PASS);

    /*
    @Test
    public void testFetchAllRecipesWithRecipeType(){
        //Arrange
        MenuRepository menuRepository = new MenuRepository(sql2o);
        Collection<Menu> menus;

        //Act
        menus = menuRepository.getAll();
        for (Menu menu : menus){

            System.out.println("\nmenu name: " + menu.getMenuName());
            System.out.println("menu description: " + menu.getMenuDescription());
            System.out.println("menu imageFilePath: " + menu.getMenuImageFilePath());
            System.out.println("mealType: " + menu.getMealType().getMealTypeName());

            for (Recipe recipe : menu.getRecipes()){
                System.out.println("\n\trecipe name: " + recipe.getRecipeName());
                System.out.println("\trecipe description: " + recipe.getRecipeDescription());
                System.out.println("\trecipe imageFilePath: " + recipe.getRecipeImageFilePath());
                System.out.println("\trecipeType: " + recipe.getRecipeType().getRecipeTypeName());
            }
        }

        //Assert
        Assert.assertEquals(menus.size(), 2);
    }
    */

    /*
    @Test
    public void TestCreateValidMenu(){
        //Arrange
        MenuRepository menuRepository = new MenuRepository(sql2o);
        int id;

        //Act
        MealType mealType = new MealType(1,"name not used in relations");
        Collection<Recipe> recipes = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        recipe1.setRecipeId(1);
        recipe2.setRecipeId(2);
        recipes.add(recipe1);
        recipes.add(recipe2);
        Menu menu = new Menu("Cool TestMenu","Its a nice TestMenu", "Crazy picture path",mealType,recipes);

        id = menuRepository.create(menu);

        //Assert
        Assert.assertNotEquals(id,0);
    }
    */
}
