package controllers;

import models.Allergy;
import models.Ingredient;
import models.Menu;
import models.Recipe;
import models.wrapper_models.Allergies;
import models.wrapper_models.Ingredients;
import models.wrapper_models.Menus;
import models.wrapper_models.Recipes;
import org.sql2o.Sql2o;
import repositories.MenuRepository;
import repositories.repositoryInterfaces.IMenuRepository;

import java.util.Collection;

import static jsonUtil.JsonUtil.fromJson;
import static jsonUtil.JsonUtil.json;
import static spark.Spark.*;

/**
 * Created by Kaempe on 29-03-2017.
 */
public class MenuController
{
    private IMenuRepository menuRepository;

    public MenuController(Sql2o sql2o){

        menuRepository = new MenuRepository(sql2o);

        get("/menus", (req, res) ->
        {
            String publisherId = req.attribute("publisherId");

            Collection<Menu> menus = menuRepository.getAll(publisherId);
            if (menus.size() != 0){
                res.status(200);
                return new Menus(menus);
            }
            res.status(204);
            return new String("No Menus found in the database for the publisher logged in");
        }, json());

        get("/menus/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }

            String publisherId = req.attribute("publisherId");
            Menu menu = menuRepository.get(id,publisherId);

            if (menu != null) {
                res.status(200);
                return menu;
            }
            res.status(204);
            return new String("No menu with id "+ id +" found for the publisher");
        }, json());

        get("/menus/:id/recipes", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }

            String publisherId = req.attribute("publisherId");
            if (!menuRepository.isPublishersMenu(id,publisherId)){
                return new String("Menu with id "+ id +" does not belong to the publisher");
            }

            Collection<Recipe> recipes = menuRepository.getRecipesFor(id);

            if (recipes != null) {
                res.status(200);
                return new Recipes(recipes);
            }
            res.status(204);
            return new String("No recipes found for menu with id "+ id);
        }, json());

        get("/menus/:id/ingredients", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }

            String publisherId = req.attribute("publisherId");
            if (!menuRepository.isPublishersMenu(id,publisherId)){
                return new String("Menu with id "+ id +" does not belong to the publisher");
            }

            Collection<Ingredient> ingredients = menuRepository.getIngredientFor(id);

            if (ingredients != null) {
                res.status(200);
                return new Ingredients(ingredients);
            }
            res.status(204);
            return new String("No ingredients found for menu with id "+ id);
        }, json());

        get("/menus/:id/allergies", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }

            String publisherId = req.attribute("publisherId");
            if (!menuRepository.isPublishersMenu(id,publisherId)){
                return new String("Menu with id "+ id +" does not belong to the publisher");
            }

            Collection<Allergy> allergies = menuRepository.getAllergiesFor(id);

            if (allergies != null) {
                res.status(200);
                return new Allergies(allergies);
            }
            res.status(204);
            return new String("No allergies found for menu with id "+ id);
        }, json());

        post("/menus", (req, res) -> {
            Menu menu;
            try{
                menu = fromJson(req.body(),Menu.class);
            }catch (Exception e)
            {
                res.status(400);
                return new String("Invalid Request Body ");
            }
            menu.setPublisherName(req.attribute("publisherName"));
            menu.setPublisherId(req.attribute("publisherId"));

            int id = menuRepository.create(menu);

            if (id != 0)
            {
                res.status(200);
                return id;
            }
            res.status(400);
            return new String("Menu not created");
        }, json());

        put("/menus/:id", (req, res) -> {
            int id ;
            Menu menu;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }
            try {
                menu = fromJson(req.body(), Menu.class);
            }catch (Exception e)
            {
                res.status(400);
                return new String("Invalid Request Body ");
            }

            menu.setPublisherName(req.attribute("publisherName"));
            menu.setPublisherId(req.attribute("publisherId"));
            menu.setMenuId(id);

            boolean result = menuRepository.update(menu);

            if (result)
            {
                res.status(200);
                return new String("Menu " + id + " Updated");
            }
            res.status(400);
            return new String("Menu not updated");
        }, json());

        delete("/menus/:id", (req, res) -> {
            int id ;
            try{
                id = Integer.parseInt(req.params(":id"));
            }catch (Exception e)
            {
                res.status(400);
                return new String("the id must be an integer");
            }

            String publisherId = req.attribute("publisherId");

            boolean result = menuRepository.delete(id,publisherId);
            if (result)
            {
                res.status(200);
                return new String("Menu " + id + " Deleted");
            }
            res.status(500);
            return new String("Could not delete Menu with id " + id);
        },json());
    }
}
