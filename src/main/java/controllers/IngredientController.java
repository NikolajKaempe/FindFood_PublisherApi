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
                return new String("Ingredient has been requested. \nAdmins need to publish it before usable.");
            }
            res.status(400);
            return new String("Ingredient not created");
        }, json());
    }
}
