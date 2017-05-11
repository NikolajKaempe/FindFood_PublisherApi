package controllers;

import models.MealType;
import models.wrapper_models.MealTypes;
import org.sql2o.Sql2o;
import repositories.MealTypeRepository;
import repositories.repositoryInterfaces.IMealTypeRepository;

import java.util.Collection;

import static jsonUtil.JsonUtil.fromJson;
import static jsonUtil.JsonUtil.json;
import static spark.Spark.*;
import static spark.Spark.after;

/**
 * Created by Kaempe on 19-03-2017.
 */
public class MealTypeController
{
    private IMealTypeRepository mealTypeRepository;

    public MealTypeController(Sql2o sql2o)
    {
        this.mealTypeRepository = new MealTypeRepository(sql2o);

        get("/mealTypes", (req, res) ->
        {
            Collection<MealType> mealTypes = mealTypeRepository.getAll();
            if (mealTypes.size() != 0){
                res.status(200);
                return  new MealTypes(mealTypes);
            }
            res.status(204);
            return new String("No mealTypes found in the database");
        }, json());

        get("/mealTypes/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            MealType mealType = mealTypeRepository.get(id);

            if (mealType!= null) {
                res.status(200);
                return mealType;
            }
            res.status(204);
            return new String("No mealType with id "+ id +" found");
        }, json());

        post("/mealTypes", (req, res) -> {
            MealType mealType = fromJson(req.body(),MealType.class);

            int id = mealTypeRepository.create(mealType);

            if (id != 0)
            {
                res.status(200);
                return new String("MealType has been requested. \nAdmins need to publish it before usable.");
            }
            res.status(400);
            return new String("mealType not created");
        }, json());
    }
}
