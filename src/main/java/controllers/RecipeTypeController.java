package controllers;

import models.RecipeType;
import models.wrapper_models.RecipeTypes;
import org.sql2o.Sql2o;
import repositories.RecipeTypeRepository;
import repositories.repositoryInterfaces.IRecipeTypeRepository;

import java.util.Collection;

import static jsonUtil.JsonUtil.fromJson;
import static jsonUtil.JsonUtil.json;
import static jsonUtil.JsonUtil.toJson;
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
                return id;
            }
            res.status(400);
            return new String("recipeTypes not created");
        }, json());

        put("/recipeTypes/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            RecipeType recipeType = fromJson(req.body(),RecipeType.class);
            recipeType.setRecipeTypeId(id);
            boolean result = recipeTypeRepository.update(recipeType);

            if (result)
            {
                res.status(200);
                return new String("recipeType " + id + " Updated");
            }
            res.status(400);
            return new String("recipeType not updated");
        }, json());

        delete("/recipeTypes/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            boolean result = recipeTypeRepository.delete(id);
            if (result)
            {
                res.status(200);
                return result;
            }
            res.status(500);
            return new String("Could not delete recipeType with id " + id);
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
