package controllers;

import models.Allergy;
import models.wrapper_models.Allergies;
import org.sql2o.Sql2o;
import repositories.AllergyRepository;
import repositories.repositoryInterfaces.IAllergyRepository;

import java.util.Collection;

import static jsonUtil.JsonUtil.*;
import static spark.Spark.*;

/**
 * Created by Kaempe on 27-02-2017.
 */
public class AllergyController
{
    private IAllergyRepository allergyRepository;

    public AllergyController(Sql2o sql2o)
    {
        allergyRepository = new AllergyRepository(sql2o);

        get("/allergies", (req, res) ->
        {
            Collection<Allergy> allergies = allergyRepository.getAll();
            if (allergies.size() != 0){
                res.status(200);
                return new Allergies(allergies);
            }
            res.status(204);
            return new String("No allergies found in the database");
        }, json());

        get("/allergies/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            Allergy allergy = allergyRepository.get(id);

            if (allergy != null) {
                res.status(200);
                return allergy;
            }
            res.status(204);
            return new String("No allergy with id "+ id +" found");
        }, json());

        post("/allergies", (req, res) -> {
            Allergy allergy = fromJson(req.body(),Allergy.class);

            int id = allergyRepository.create(allergy);

            if (id != 0)
            {
                res.status(200);
                return id;
            }
            res.status(400);
            return new String("Allergy not created");
        }, json());

        put("/allergies/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            Allergy allergy = fromJson(req.body(),Allergy.class);
            allergy.setAllergyId(id);
            boolean result = allergyRepository.update(allergy);

            if (result)
            {
                res.status(200);
                return new String("allergy " + id + " Updated");
            }
            res.status(400);
            return new String("allergy not updated");
        }, json());

        delete("/allergies/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            boolean result = allergyRepository.delete(id);
            if (result)
            {
                res.status(200);
                return result;
            }
            res.status(500);
            return new String("Could not delete allergy with id " + id);
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
