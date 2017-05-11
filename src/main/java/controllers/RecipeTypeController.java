package controllers;

import models.RecipeType;
import models.wrapper_models.RecipeTypes;
import org.sql2o.Sql2o;
import repositories.RecipeTypeRepository;
import repositories.repositoryInterfaces.IRecipeTypeRepository;

import java.util.Collection;

import static jsonUtil.JsonUtil.fromJson;
import static jsonUtil.JsonUtil.json;
import static spark.Spark.*;

/**
 * Created by Kaempe on 19-03-2017.
 */
public class RecipeTypeController
{
    private IRecipeTypeRepository recipeTypeRepository;

    public RecipeTypeController(Sql2o sql2o)
    {
        recipeTypeRepository = new RecipeTypeRepository(sql2o);

        get("/recipeTypes", (req, res) ->
        {
            Collection<RecipeType> recipeTypes = recipeTypeRepository.getAll();
            if (recipeTypes.size() != 0){
                res.status(200);
                return  new RecipeTypes(recipeTypes);
            }
            res.status(204);
            return new String("No recipeTypes found in the database");
        }, json());

        get("/recipeTypes/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            RecipeType recipeType = recipeTypeRepository.get(id);

            if (recipeType!= null) {
                res.status(200);
                return recipeType;
            }
            res.status(204);
            return new String("No recipeType with id "+ id +" found");
        }, json());

        post("/recipeTypes", (req, res) -> {
            RecipeType recipeType = fromJson(req.body(),RecipeType.class);

            int id = recipeTypeRepository.create(recipeType);

            if (id != 0)
            {
                res.status(200);
                return new String("RecipeType has been requested. \nAdmins need to publish it before usable.");
            }
            res.status(400);
            return new String("recipeTypes not created");
        }, json());
    }
}
