package repositories;

import models.RecipeType;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import repositories.repositoryInterfaces.IRecipeTypeRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kaempe on 17-03-2017.
 */
public class RecipeTypeRepository implements IRecipeTypeRepository{

    private Sql2o sql2o;

    public RecipeTypeRepository(Sql2o sql2o)
    {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<RecipeType> getAll() {
        Collection<RecipeType> recipeTypes;
        String sql =
                "SELECT recipeTypeId, recipeTypeName " +
                    "FROM RecipeTypes " +
                    "WHERE published = 1";

        try{
            Connection con = sql2o.open();
            recipeTypes = con.createQuery(sql)
                    .executeAndFetch(RecipeType.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return recipeTypes;
    }

    @Override
    public RecipeType get(int id) {
        if (!this.exists(id)){
            throw new IllegalArgumentException("No recipeType found with id " + id);
        }

        RecipeType recipeType;
        String sql =
                "SELECT recipeTypeId, recipeTypeName " +
                    "FROM RecipeTypes " +
                        "WHERE recipeTypeId = :id ";
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
    public int create(RecipeType model) {
        int id;
        failIfInvalid(model);
        String sql =
                "INSERT INTO RecipeTypes (recipeTypeName, published) " +
                        "VALUES (:recipeTypeName, :published)";
        try{
            Connection con = sql2o.open();
            id = Integer.parseInt(con.createQuery(sql, true)
                    .bind(model)
                    .addParameter("published",false)
                    .executeUpdate().getKey().toString());
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
        return id;
    }

    @Override
    public boolean exists(int id) {
        RecipeType recipeType;

        String sql = "SELECT recipeTypeId " +
                "FROM RecipeTypes " +
                    "WHERE recipeTypeId = :id " +
                    "AND published = 1";
        try{
            Connection con = sql2o.open();
            recipeType = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(RecipeType.class);
            if (recipeType != null) return true;
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void failIfInvalid(RecipeType recipeType)
    {
        if (recipeType == null)
        {
            throw new IllegalArgumentException("recipeType cannot be null");
        }
        if (recipeType.getRecipeTypeName() == null || recipeType.getRecipeTypeName().length() < 1) {
            throw new IllegalArgumentException("Parameter `name` cannot be empty");
        }
    }
}
