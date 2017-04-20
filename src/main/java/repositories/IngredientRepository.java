package repositories;

import models.Allergy;
import models.Ingredient;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import repositories.repositoryInterfaces.IIngredientRepository;

import java.util.ArrayList;
import java.util.Collection;

public class IngredientRepository implements IIngredientRepository
{
    private Sql2o sql2o;

    public IngredientRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Ingredient> getAll() {
        Collection<Ingredient> ingredients;
        String sql =
                "SELECT ingredientId, ingredientName, ingredientDescription " +
                    "FROM Ingredients";
        try{
            Connection con = sql2o.open();
            ingredients = con.createQuery(sql)
                    .executeAndFetch(Ingredient.class);
           //ingredients.forEach(ingredient -> ingredient.setAllergies(this.getAllergiesFor(ingredient.getIngredientId())));
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return ingredients;
    }

    @Override
    public Ingredient get(int id) {
        if (!this.exists(id)){
            throw new IllegalArgumentException("No ingredient found with id " + id);
        }
        Ingredient ingredient;
        String sql =
                "SELECT ingredientId, ingredientName, ingredientDescription " +
                        "FROM Ingredients " +
                        "WHERE ingredientId = :id";
        try{
            Connection con = sql2o.open();
            ingredient = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Ingredient.class);
            ingredient.setAllergies(this.getAllergiesFor(id));
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return ingredient;
    }

    @Override
    public int create(Ingredient model) {
        int id;
        failIfInvalid(model);
        String sql =
                "INSERT INTO Ingredients (ingredientName, ingredientDescription) " +
                        "VALUES (:ingredientName, :ingredientDescription)";
        String sqlRelations =
                "INSERT INTO IngredientAllergies (allergyId, ingredientId) " +
                        "VALUES (:allergyId, :id )";
        try{
            Connection con = sql2o.beginTransaction();
            id = Integer.parseInt(con.createQuery(sql, true)
                    .bind(model)
                    .executeUpdate().getKey().toString());
            model.getAllergies().forEach(allergy ->
                    con.createQuery(sqlRelations)
                            .addParameter("allergyId",allergy.getAllergyId())
                            .addParameter("id",id)
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
    public boolean update(Ingredient model)
    {
        if (!this.exists(model.getIngredientId())){
            throw new IllegalArgumentException("No ingredient found with id: " + model.getIngredientId());
        }
        failIfInvalid(model);

        String sql =
                "UPDATE Ingredients SET " +
                        "ingredientName = :ingredientName, " +
                        "ingredientDescription = :ingredientDescription " +
                        "WHERE ingredientId = :ingredientId";
        String sqlRelationsToDelete =
                "DELETE FROM IngredientAllergies WHERE " +
                        "ingredientId = :id";
        String sqlRelationsToUpdate =
                "INSERT INTO IngredientAllergies (allergyId, ingredientId) " +
                "VALUES (:allergyId, :id )";
        try{
            Connection con = sql2o.beginTransaction();
            con.createQuery(sql)
                    .bind(model)
                    .executeUpdate();
            con.createQuery(sqlRelationsToDelete)
                    .addParameter("id",model.getIngredientId())
                    .executeUpdate();
            model.getAllergies().forEach(allergy ->
                con.createQuery(sqlRelationsToUpdate)
                        .addParameter("allergyId",allergy.getAllergyId())
                        .addParameter("id",model.getIngredientId())
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
    public boolean delete(int id)
    {
        if (!this.exists(id)){
            throw new IllegalArgumentException("No Ingredient found with id: " + id);
        }
        failDeleteIfRelationsExist(id);
        Connection con;
        String sqlRelationsToDelete =
                "DELETE FROM IngredientAllergies WHERE " +
                        "ingredientId = :id";

        String sql =
               "DELETE FROM Ingredients WHERE " +
                        "ingredientId = :id ";
        try{
            con = sql2o.beginTransaction();
            con.createQuery(sqlRelationsToDelete)
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
    public boolean exists(int id)
    {
        String sql =
                "SELECT ingredientId FROM Ingredients " +
                        "WHERE ingredientId = :id";
        try{
            Connection con = sql2o.open();
            Ingredient ingredient = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Ingredient.class);
            if (ingredient != null) return true;
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void failIfInvalid(Ingredient ingredient)
    {
        if (ingredient == null)
        {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }
        if (ingredient.getIngredientName() == null || ingredient.getIngredientDescription().length() < 1) {
            throw new IllegalArgumentException("Parameter `name` cannot be empty");
        }
        if (ingredient.getIngredientDescription() == null || ingredient.getIngredientDescription().length() < 1) {
            throw new IllegalArgumentException("Parameter `description` cannot be empty");
        }
        if(ingredient.getAllergies() == null){
            throw new IllegalArgumentException("An ingredient must contain Allergies");
        }

        if (ingredient.getAllergies().size() < 1)
        {
            throw new IllegalArgumentException("An ingredient must contain at least one Allergy");
        }
        for (Allergy allergy : ingredient.getAllergies()) {
            if (!this.isRelationValid(allergy.getAllergyId())){
                throw new IllegalArgumentException("allergy with id " + allergy.getAllergyId() + " dos'ent exist");
            }
        }
    }

    @Override
    public Collection<Allergy> getAllergiesFor(int id)
    {
        Collection<Allergy> allergies;
        String sql =
                "SELECT allergyId, allergyName, allergyDescription " +
                    "FROM Allergies " +
                    "WHERE allergyId IN (" +
                        "SELECT allergyId FROM IngredientAllergies " +
                        "WHERE ingredientId = :id" +
                    ")";
        try{
            Connection con = sql2o.open();
            allergies = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(Allergy.class);
        }catch (Exception e)
        {
            throw new IllegalArgumentException("No allergies found for ingredient with id "+ id);
        }

        return allergies;
    }

    @Override
    public boolean isRelationValid(int relationId){
        int id;
        String sql =
                "SELECT allergyId FROM Allergies " +
                        "WHERE allergyId = :id";
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
    public void failDeleteIfRelationsExist(int id)
    {
        Collection<Integer> relations;
        String sql = "SELECT ingredientId " +
                "FROM MeasuredIngredients " +
                "WHERE ingredientId = :id" +
                ")";
        try{
            Connection con = sql2o.open();
            relations = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(Integer.class);
        }catch (Exception e)
        {
            throw new IllegalArgumentException("Ingredient not deleted. Problems with Recipe associations");
        }
        if (!relations.isEmpty()) throw new IllegalArgumentException("Ingredient not deleted. Used in one or more Recipes");
    }
}
