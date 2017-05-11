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
                    "FROM Ingredients WHERE published = 1";
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
                        "WHERE ingredientId = :id " +
                        "AND published = 1";
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
                "INSERT INTO Ingredients (ingredientName, ingredientDescription, published) " +
                    "VALUES (:ingredientName, :ingredientDescription, :published)";
        String sqlRelations =
                "INSERT INTO IngredientAllergies (allergyId, ingredientId) " +
                    "VALUES (:allergyId, :id )";
        try{
            Connection con = sql2o.beginTransaction();
            id = Integer.parseInt(con.createQuery(sql, true)
                    .bind(model)
                    .addParameter("published",false)
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
    public boolean exists(int id)
    {
        String sql =
                "SELECT ingredientId FROM Ingredients " +
                    "WHERE ingredientId = :id " +
                    "AND published = 1";
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
                    "WHERE allergyId = :id " +
                    "AND published = 1";
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
}
