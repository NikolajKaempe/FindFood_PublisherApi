package repositories;

import models.MealType;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import repositories.repositoryInterfaces.IMealTypeRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kaempe on 17-03-2017.
 */
public class MealTypeRepository implements IMealTypeRepository
{
    private Sql2o sql2o;

    public MealTypeRepository(Sql2o sql2o) { this.sql2o = sql2o; }

    @Override
    public Collection<MealType> getAll() {
        Collection<MealType> mealTypes;
        String sql =
                "SELECT mealTypeId, mealTypeName " +
                    "FROM MealTypes " +
                    "WHERE published = 1";
        try{
            Connection con = sql2o.open();
            mealTypes = con.createQuery(sql)
                    .executeAndFetch(MealType.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return mealTypes;
    }

    @Override
    public MealType get(int id) {
        if (!this.exists(id)){
            throw new IllegalArgumentException("No mealType found with id " + id);
        }

        MealType mealType;
        String sql = "SELECT mealTypeId, mealTypeName " +
                "FROM MealTypes " +
                "WHERE mealTypeId = :id";
        try{
            Connection con = sql2o.open();
            mealType = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(MealType.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return mealType;
    }

    @Override
    public int create(MealType model) {
        int id;
        failIfInvalid(model);
        String sql =
                "INSERT INTO MealTypes (mealTypeName, published) " +
                        "VALUES (:mealTypeName, :published)";
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
    public boolean exists(int id)
    {
        MealType mealType;

        String sql = "SELECT mealTypeId " +
                "FROM MealTypes " +
                    "WHERE mealTypeId = :id " +
                    "AND published = 1";
        try{
            Connection con = sql2o.open();
            mealType = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(MealType.class);
            if (mealType != null) return true;
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void failIfInvalid(MealType mealType)
    {
        if (mealType == null)
        {
            throw new IllegalArgumentException("mealType cannot be null");
        }
        if (mealType.getMealTypeName() == null || mealType.getMealTypeName().length() < 1) {
            throw new IllegalArgumentException("Parameter `name` cannot be empty");
        }
    }
}
