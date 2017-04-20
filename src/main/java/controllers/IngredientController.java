package controllers;

import models.Allergy;
import models.Ingredient;
import models.wrapper_models.Allergies;
import models.wrapper_models.Ingredients;
import org.sql2o.Sql2o;
import repositories.IngredientRepository;
import repositories.repositoryInterfaces.IIngredientRepository;

import java.util.Collection;

import static jsonUtil.JsonUtil.fromJson;
import static jsonUtil.JsonUtil.json;
import static jsonUtil.JsonUtil.toJson;
import static spark.Spark.*;

/**
 * Created by Kaempe on 15-03-2017.
 */
public class IngredientController
{
    private IIngredientRepository ingredientRepository;

    public IngredientController(Sql2o sql2o)
    {
        this.ingredientRepository = new IngredientRepository(sql2o);

        get("/ingredients", (req, res) ->
        {
            Collection<Ingredient> ingredients = ingredientRepository.getAll();

            if (ingredients.size() > 0){
                res.status(200);
                return  new Ingredients(ingredients);
            }
            res.status(204);
            return new String("No ingredients found in the database");
        }, json());

        get("/ingredients/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            Ingredient ingredient = ingredientRepository.get(id);

            if (ingredient != null) {
                res.status(200);
                return ingredient;
            }
            res.status(204);
            return new String("No ingredient with id "+ id +" found");
        }, json());

        get("/ingredients/:id/allergies", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }

            Collection<Allergy> allergies = ingredientRepository.getAllergiesFor(id);

            if (allergies.size() > 0) {
                res.status(200);
                return new Allergies(allergies);
            }
            res.status(204);
            return new String("No allergies found for ingredient with id "+ id);
        }, json());

        post("/ingredients", (req, res) -> {
            Ingredient ingredient = fromJson(req.body(),Ingredient.class);

            int id = ingredientRepository.create(ingredient);

            if (id != 0)
            {
                res.status(200);
                return id;
            }
            res.status(400);
            return new String("Ingredient not created");
        }, json());

        put("/ingredients/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            Ingredient ingredient = fromJson(req.body(),Ingredient.class);
            ingredient.setIngredientId(id);
            boolean result = ingredientRepository.update(ingredient);

            if (result)
            {
                res.status(200);
                return new String("ingredient " + id + " Updated");
            }
            res.status(400);
            return new String("ingredient not updated");
        }, json());

        delete("/ingredients/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            boolean result = ingredientRepository.delete(id);
            if (result)
            {
                res.status(200);
                return result;
            }
            res.status(500);
            return new String("Could not delete ingredient with id " + id);
        },json());

        before((req,res) -> {
            //TODO GET VERIFICATION FOR ADMIN/PUBLISHER
            res.header("MyVal", "Hello World"); // Dummy -> REMOVE
        });

        after((req, res) -> res.type("application/json"));

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(e.getMessage()));
            res.type("application/json");
        });
    }
}
