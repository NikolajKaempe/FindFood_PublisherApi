package controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;
import models.Allergy;
import models.MeasuredIngredient;
import models.Recipe;
import models.wrapper_models.Allergies;
import models.wrapper_models.MeasuredIngredients;
import models.wrapper_models.Recipes;
import org.sql2o.Sql2o;
import repositories.RecipeRepository;
import repositories.repositoryInterfaces.IRecipeRepository;

import java.util.Collection;
import java.util.HashMap;

import static jsonUtil.JsonUtil.fromJson;
import static jsonUtil.JsonUtil.json;
import static jsonUtil.JsonUtil.toJson;
import static spark.Spark.*;

/**
 * Created by Kaempe on 22-03-2017.
 */
public class RecipeController
{
    private IRecipeRepository recipeRepository;

    public RecipeController(Sql2o sql2o)
    {
        recipeRepository = new RecipeRepository(sql2o);

        get("/recipes", (req, res) ->
        {
            String publisherId =  req.attribute("publisherId");

            Collection<Recipe> recipes = recipeRepository.getAll(publisherId);
            if (recipes.size() != 0){
                res.status(200);
                return  new Recipes(recipes);
            }
            res.status(204);
            return new String("No recipes found for the publisher in the database");
        }, json());

        get("/recipes/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }

            String publisherId =  req.attribute("publisherId");
            Recipe recipe = recipeRepository.get(id,publisherId);

            if (recipe != null) {
                res.status(200);
                return recipe;
            }
            res.status(204);
            return new String("No recipe with id "+ id +" found for the publisher");
        }, json());

        get("/recipes/:id/allergies", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }

            String publisherId =  req.attribute("publisherId");
            if (!recipeRepository.isPublishersRecipe(id,publisherId)){
                return new String("No recipe with id "+ id +" owned by the publisher");
            }

            Collection<Allergy> allergies = recipeRepository.getAllergiesFor(id);

            if (allergies.size() > 0) {
                res.status(200);
                return new Allergies(allergies);
            }
            res.status(204);
            return new String("No allergies found for recipe with id "+ id);
        }, json());

        get("/recipes/:id/ingredients", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }

            String publisherId =  req.attribute("publisherId");
            if (!recipeRepository.isPublishersRecipe(id,publisherId)){
                return new String("No recipe with id "+ id +" found for the publisher");
            }

            Collection<MeasuredIngredient> ingredients = recipeRepository.getMeasuredIngredientsFor(id);

            if (ingredients.size() > 0) {
                res.status(200);
                return new MeasuredIngredients(ingredients);
            }
            res.status(204);
            return new String("No allergies found for recipe with id "+ id);
        }, json());

        post("/recipes", (req, res) -> {
            Recipe recipe;
            try{
                recipe = fromJson(req.body(),Recipe.class);
            }catch (Exception e)
            {
                res.status(400);
                return new String("Invalid Request Body ");
            }
            recipe.setPublisherName(req.attribute("publisherName"));
            recipe.setPublisherId(req.attribute("publisherId"));

            int id = recipeRepository.create(recipe);

            if (id != 0)
            {
                res.status(200);
                return id;
            }
            res.status(400);
            return new String("Recipe not created");
        }, json());

        put("/recipes/:id", (req, res) -> {
            int id ;
            Recipe recipe;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            try {
                recipe = fromJson(req.body(), Recipe.class);
            }catch (Exception e)
            {
                res.status(400);
                return new String("Invalid Request Body ");
            }

            recipe.setPublisherName(req.attribute("publisherName"));
            recipe.setPublisherId(req.attribute("publisherId"));
            recipe.setRecipeId(id);

            boolean result = recipeRepository.update(recipe);

            if (result)
            {
                res.status(200);
                return new String("Recipe " + id + " Updated");
            }
            res.status(400);
            return new String("Recipe not updated");
        }, json());

        delete("/recipes/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }

            String publisherId = req.attribute("publisherId");

            boolean result = recipeRepository.delete(id,publisherId);
            if (result)
            {
                res.status(200);
                return new String("Recipe with id " + id + " deleted");
            }
            res.status(500);
            return new String("Could not delete Recipe with id " + id);
        },json());
    }
}
