import controllers.*;
import jsonUtil.JsonUtil;
import models.Allergy;
import org.sql2o.Sql2o;
import repositories.IngredientRepository;
import repositories.RecipeRepository;

import java.util.ArrayList;
import java.util.Collection;

import static spark.Spark.port;

/**
 * Created by Kaempe on 19-02-2017.
 */
public class Main{
    public final static String DB_URL = "mysql://80.255.6.114:3306/FindFood_Publisher";
    //public final static String DB_URL = "mysql://localhost:3306/FindFood_Publisher";
    public final static String DB_USER = "FF_Publisher";
    public final static String DB_PASS = "yQjS6yiA";

    public static void main( String[] args) {
        port(4569);
        Sql2o sql2o = new Sql2o(DB_URL, DB_USER, DB_PASS);
        new AllergyController(sql2o);
        new IngredientController(sql2o);
        new RecipeTypeController(sql2o);
        new MealTypeController(sql2o);
        new RecipeController(sql2o);
        new MenuController(sql2o);
    }
}

