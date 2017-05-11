package repositories;

import models.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import repositories.repositoryInterfaces.IRecipeRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Kaempe on 20-03-2017.
 */
public class RecipeRepository implements IRecipeRepository {

    private Sql2o sql2o;

    public RecipeRepository(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Recipe> getAll(String publisherId) {
        Collection<Recipe> recipes;
        String sql =
                "SELECT recipeId, recipeName, recipeDescription, recipeImageFilePath, publisherName, published, publisherId " +
                        "FROM Recipes WHERE publisherId = :publisherId";
        try{
            Connection con = sql2o.open();
            recipes = con.createQuery(sql)
                    .addParameter("publisherId",publisherId)
                    .executeAndFetch(Recipe.class);
            recipes.forEach(recipe -> recipe.setRecipeType(this.getRecipeTypeFor(recipe.getRecipeId())));
            recipes.forEach(recipe -> recipe.setMeasuredIngredients(this.getMeasuredIngredientsFor(recipe.getRecipeId())));
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return recipes;
    }

    @Override
    public Recipe get(int id, String publisherId) {
        if (!this.exists(id)){
            throw new IllegalArgumentException("No recipe found with id " + id);
        }
        if(!isPublishersRecipe(id,publisherId)){
            throw new IllegalArgumentException("Recipe not owned by the publisher that is logged in");
        }

        Recipe recipe;
        String sql =
                "SELECT recipeId, recipeName, recipeDescription, recipeImageFilePath, publisherName, published, publisherId " +
                        "FROM Recipes WHERE recipeId = :id";
        try{
            Connection con = sql2o.open();
            recipe = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Recipe.class);
            recipe.setRecipeType(this.getRecipeTypeFor(recipe.getRecipeId()));
            recipe.setMeasuredIngredients(this.getMeasuredIngredientsFor(recipe.getRecipeId()));
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return recipe;
    }

    @Override
    public int create(Recipe model) {
        int id;
        Collection<Integer> allergyRelations = new HashSet<>();
        this.failIfInvalid(model);
        String sql =
                "INSERT INTO Recipes (recipeName, recipeDescription, recipeImageFilePath, recipeTypeId, published, publisherName, publisherId) " +
                        "VALUES (:recipeName, :recipeDescription, :recipeImageFilePath, :recipeTypeId, :published, :publisherName, :publisherId)";
        String sqlIngredientRelations =
                "INSERT INTO MeasuredIngredients (recipeId, ingredientId, amount, measure) " +
                        "VALUES (:recipeId, :ingredientId, :amount, :measure)";

        String sqlGetAllergiesForIngredients =
                "SELECT allergyId FROM IngredientAllergies WHERE " +
                        "ingredientId = :id";

        String sqlAllergyRelationsToUpdate =
                "INSERT INTO RecipeAllergies (allergyId, recipeId) " +
                        "VALUES (:allergyId, :recipeId)";

        try{
            Connection con = sql2o.beginTransaction();
            id = Integer.parseInt(con.createQuery(sql, true)
                    .bind(model)
                    .addParameter("recipeTypeId",model.getRecipeType().getRecipeTypeId())
                    .addParameter("published",false)
                    .executeUpdate().getKey().toString());
            model.getMeasuredIngredients().forEach(ingredient ->
                    con.createQuery(sqlIngredientRelations)
                            .addParameter("recipeId",id)
                            .addParameter("ingredientId",ingredient.getIngredient().getIngredientId())
                            .addParameter("amount",ingredient.getAmount())
                            .addParameter("measure",ingredient.getMeasure())
                            .executeUpdate());
            model.getMeasuredIngredients().forEach(ingredient ->
                    allergyRelations.addAll(
                            con.createQuery(sqlGetAllergiesForIngredients)
                                    .addParameter("id",ingredient.getIngredient().getIngredientId())
                                    .executeAndFetch(Integer.class)
                    ));
            allergyRelations.forEach(allergyId ->
                    con.createQuery(sqlAllergyRelationsToUpdate)
                            .addParameter("allergyId",allergyId)
                            .addParameter("recipeId",id)
                            .executeUpdate());
            con.commit();
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }

        return id;
    }

    @Override
    public boolean update(Recipe model) {
        if (!this.exists(model.getRecipeId())){
            throw new IllegalArgumentException("No recipe found with id: " + model.getRecipeId());
        }
        if(!this.isPublishersRecipe(model.getRecipeId(),model.getPublisherId())){
            throw new IllegalArgumentException("Recipe not owned by the publisher that is logged in");
        }
        if(this.isPublished(model.getRecipeId())){
            throw new IllegalArgumentException("Cannot update a published recipe");
        }

        this.failIfInvalid(model);

        Collection<Integer> allergyRelations = new HashSet<>();

        String sql =
                "UPDATE Recipes SET " +
                        "recipeName = :recipeName, " +
                        "recipeDescription = :recipeDescription, " +
                        "recipeImageFilePath = :recipeImageFilePath, " +
                        "published = :published, " +
                        "publisherName = :publisherName, " +
                        "publisherId = :publisherId, " +
                        "recipeTypeId = :recipeTypeId " +
                        "WHERE recipeId = :recipeId";

        String sqlIngredientRelationsToDelete =
                "DELETE FROM MeasuredIngredients WHERE " +
                        "recipeId = :id";

        String sqlIngredientRelationsToUpdate =
                "INSERT INTO MeasuredIngredients (recipeId, ingredientId, amount, measure) " +
                        "VALUES (:recipeId, :ingredientId, :amount, :measure )";

        String sqlAllergyRelationsToDelete =
                "DELETE FROM RecipeAllergies WHERE " +
                        "recipeId = :id";

        String sqlGetAllergiesForIngredients =
                "SELECT allergyId FROM IngredientAllergies WHERE " +
                        "ingredientId = :id";

        String sqlAllergyRelationsToUpdate =
                "INSERT INTO RecipeAllergies (allergyId, recipeId) " +
                        "VALUES (:allergyId, :recipeId)";

        try{
            Connection con = sql2o.beginTransaction();
            con.createQuery(sql)
                    .bind(model)
                    .addParameter("published",false)
                    .addParameter("recipeTypeId",model.getRecipeType().getRecipeTypeId())
                    .addParameter("recipeId",model.getRecipeId())
                    .executeUpdate();
            con.createQuery(sqlIngredientRelationsToDelete)
                    .addParameter("id",model.getRecipeId())
                    .executeUpdate();
            model.getMeasuredIngredients().forEach(ingredient ->
                    con.createQuery(sqlIngredientRelationsToUpdate)
                            .addParameter("recipeId",model.getRecipeId())
                            .addParameter("ingredientId",ingredient.getIngredient().getIngredientId())
                            .addParameter("amount",ingredient.getAmount())
                            .addParameter("measure",ingredient.getMeasure())
                            .executeUpdate());
            con.createQuery(sqlAllergyRelationsToDelete)
                    .addParameter("id",model.getRecipeId())
                    .executeUpdate();
            model.getMeasuredIngredients().forEach(ingredient ->
                    allergyRelations.addAll(
                        con.createQuery(sqlGetAllergiesForIngredients)
                            .addParameter("id",ingredient.getIngredient().getIngredientId())
                            .executeAndFetch(Integer.class)
                    ));
            allergyRelations.forEach(allergyId ->
                con.createQuery(sqlAllergyRelationsToUpdate)
                    .addParameter("allergyId",allergyId)
                    .addParameter("recipeId",model.getRecipeId())
                    .executeUpdate());
            con.commit();
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id, String publisherId) {
        if (!this.exists(id)){
            throw new IllegalArgumentException("No Recipe found with id: " + id);
        }
        if(!this.isPublishersRecipe(id,publisherId)){
            throw new IllegalArgumentException("Recipe not owned by the publisher that is logged in");
        }
        if(this.isPublished(id)){
            throw new IllegalArgumentException("Cannot delete a published recipe");
        }

        failDeleteIfRelationsExist(id);
        Connection con;

        String sqlIngredientRelationsToDelete =
                "DELETE FROM MeasuredIngredients WHERE " +
                        "recipeId = :id";

        String sqlAllergyRelationsToDelete =
                "DELETE FROM RecipeAllergies WHERE " +
                        "recipeId = :id";

        String sql =
                "DELETE FROM Recipes WHERE " +
                        "recipeId = :id ";


        try{
            con = sql2o.beginTransaction();
            con.createQuery(sqlIngredientRelationsToDelete)
                    .addParameter("id",id)
                    .executeUpdate();
            con.createQuery(sqlAllergyRelationsToDelete)
                    .addParameter("id",id)
                    .executeUpdate();
            con.createQuery(sql)
                    .addParameter("id",id)
                    .executeUpdate();
            con.commit();
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean exists(int id) {
        Recipe recipe;

        String sql = "SELECT recipeId " +
                "FROM Recipes " +
                "WHERE recipeId = :id";
        try{
            Connection con = sql2o.open();
            recipe = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Recipe.class);
            if (recipe != null) return true;
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void failIfInvalid(Recipe recipe){
        try{
            if ( recipe == null)
            {
                throw new IllegalArgumentException("recipe cannot be null");
            }
            if (recipe.getRecipeName() == null || recipe.getRecipeName().length() < 1) {
                throw new IllegalArgumentException("Parameter `name` cannot be empty");
            }
            if (recipe.getRecipeDescription() == null || recipe.getRecipeDescription().length() < 1) {
                throw new IllegalArgumentException("Parameter `description` cannot be empty");
            }
            if (recipe.getRecipeType() == null ) {
                throw new IllegalArgumentException("Parameter `recipeType` cannot be null");
            }
            if (recipe.getMeasuredIngredients() == null){
                throw new IllegalArgumentException("Recipe must have at least 1 ingredient!");
            }
            if (!this.isRecipeTypeValid(recipe.getRecipeType().getRecipeTypeId()))
            {
                throw new IllegalArgumentException("Parameter `recipeType` dos'ent exist");
            }
            if(recipe.getPublisherName() == null || recipe.getPublisherName() == ""){
                throw new IllegalArgumentException("invalid publisher");
            }
            if(recipe.getPublisherId() == null || recipe.getPublisherId() == ""){
                throw new IllegalArgumentException("invalid publisherId");
            }
            for (MeasuredIngredient measuredIngredient : recipe.getMeasuredIngredients()){
                if (measuredIngredient.getAmount() <= 0){
                    throw new IllegalArgumentException("Parameter `measuredIngredient` amount must be greater than 0");
                }
                if (measuredIngredient.getMeasure() == null || measuredIngredient.getMeasure().length() < 1){
                    throw new IllegalArgumentException("Parameter `measure` cannot be empty");
                }
                if (measuredIngredient.getIngredient() == null){
                    throw new IllegalArgumentException("Parameter `ingredient` cannot be null");
                }
                if(!this.isIngredientValid(measuredIngredient.getIngredient().getIngredientId())){
                    throw new IllegalArgumentException("Ingredient with id " +
                            measuredIngredient.getIngredient().getIngredientId() + " dos'ent exist");
                }
            }
        }catch (Exception e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public RecipeType getRecipeTypeFor(int id){
        RecipeType recipeType;
        String sql =
                "SELECT recipeTypeId, recipeTypeName " +
                    "FROM RecipeTypes " +
                    "WHERE recipeTypeId IN (" +
                        "SELECT recipeTypeId FROM Recipes " +
                        "WHERE recipeId = :id" +
                    ")";
        try{
            Connection con = sql2o.open();
            recipeType = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(RecipeType.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return recipeType;
    }

    @Override
    public Collection<MeasuredIngredient> getMeasuredIngredientsFor(int id){
        Collection<MeasuredIngredient> ingredients;
        String sql =
                "SELECT measuredIngredientId, amount, measure FROM MeasuredIngredients " +
                        "WHERE recipeId = :id";
        try{
            Connection con = sql2o.open();
            ingredients = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(MeasuredIngredient.class);
            ingredients.forEach(ingredient -> ingredient.setIngredient(getIngredientFor(id)));
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return ingredients;
    }

    @Override
    public Ingredient getIngredientFor(int id){
        Ingredient ingredient;
        String sql =
                "SELECT ingredientId, ingredientName, ingredientDescription " +
                    "FROM Ingredients " +
                    "WHERE ingredientId IN (" +
                        "SELECT ingredientId FROM MeasuredIngredients " +
                        "WHERE recipeId= :id" +
                    ")";
        try{
            Connection con = sql2o.open();
            ingredient = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Ingredient.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return ingredient;
    }

    @Override
    public Collection<Allergy> getAllergiesFor(int id){
        Collection<Allergy> allergies;
        String sql =
                "SELECT allergyId, allergyName, allergyDescription " +
                    "FROM Allergies " +
                    "WHERE allergyId in (" +
                        "SELECT allergyId from RecipeAllergies WHERE " +
                        "recipeId = :id" +
                    ")";
        try{
            Connection con = sql2o.open();
            allergies = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(Allergy.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return allergies;
    }

    @Override
    public boolean isIngredientValid(int relationId){
        int id;
        String sql =
                "SELECT ingredientId FROM Ingredients " +
                        "WHERE ingredientId = :id";
        try{
            Connection con = sql2o.open();
            id = con.createQuery(sql)
                    .addParameter("id",relationId)
                    .executeAndFetchFirst(Integer.class);
            if (id > 0) return true;
            return false;
        }catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean isRecipeTypeValid(int recipeTypeId){
        int id;
        String sql =
                "SELECT recipeTypeId FROM RecipeTypes " +
                        "WHERE recipeTypeId = :id";
        try{
            Connection con = sql2o.open();
            id = con.createQuery(sql)
                    .addParameter("id",recipeTypeId)
                    .executeAndFetchFirst(Integer.class);
            if (id > 0) return true;
            return false;
        }catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean isPublished(int id) {
        Recipe recipe;

        String sql = "SELECT recipeId " +
                "FROM Recipes " +
                "WHERE recipeId = :id " +
                "AND published = 1";
        try{
            Connection con = sql2o.open();
            recipe = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Recipe.class);
            if (recipe != null) return true;
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isPublishersRecipe(int id, String publisherId){
        Recipe recipe;

        String sql = "SELECT recipeId " +
                "FROM Recipes " +
                "WHERE recipeTypeId = :id " +
                "AND publisherId = :publisherId";
        try{
            Connection con = sql2o.open();
            recipe = con.createQuery(sql)
                    .addParameter("id",id)
                    .addParameter("publisherId",publisherId)
                    .executeAndFetchFirst(Recipe.class);
            if (recipe != null) return true;
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void failDeleteIfRelationsExist(int id){
        Collection<Integer> relations;
        String sql = "SELECT recipeId " +
                "FROM MenuRecipes " +
                "WHERE recipeId = :id";
        try{
            Connection con = sql2o.open();
            relations = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(Integer.class);
        }catch (Exception e)
        {
            throw new IllegalArgumentException("Recipe not deleted. Problems with menu associations");
        }
        if (!relations.isEmpty()) throw new IllegalArgumentException("Recipe not deleted. Used in one or more Menus");
    }

}
