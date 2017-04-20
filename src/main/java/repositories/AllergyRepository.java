package repositories;

import models.Allergy;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import repositories.repositoryInterfaces.IAllergyRepository;

import java.util.ArrayList;
import java.util.Collection;

public class AllergyRepository implements IAllergyRepository
{
    private Sql2o sql2o;

    public AllergyRepository(Sql2o sql2o)
    {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Allergy> getAll() {
        Collection<Allergy> allergies;
        String sql =
                "SELECT allergyId, allergyName, allergyDescription " +
                        "FROM Allergies ";
        try{
            Connection con = sql2o.open();
            allergies = con.createQuery(sql)
                    .executeAndFetch(Allergy.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return allergies;
    }

    @Override
    public Allergy get(int id) {
        Allergy allergy;
        String sql =
                "SELECT allergyId, allergyName, allergyDescription " +
                        "FROM Allergies " +
                        "WHERE allergyId = :id";
        try{
            Connection con = sql2o.open();
            allergy = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Allergy.class);
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("No Allergy with id " + id + " found");
        }

        return allergy;
    }

    @Override
    public int create(Allergy model) {
        int id;
        this.failIfInvalid(model);
        String sql =
                "INSERT INTO Allergies (allergyName, allergyDescription) " +
                        "VALUES (:allergyName, :allergyDescription)";
        try{
            Connection con = sql2o.open();
            id = Integer.parseInt(con.createQuery(sql, true)
                    .bind(model)
                    .executeUpdate().getKey().toString());
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
        return id;
    }

    @Override
    public boolean update(Allergy model) {
        if (!this.exists(model.getAllergyId())){
            throw new IllegalArgumentException("No allergy found with id: " + model.getAllergyId());
        }
        this.failIfInvalid(model);
        String sql =
                "UPDATE Allergies SET " +
                        "allergyName = :allergyName, " +
                        "allergyDescription = :allergyDescription " +
                        "WHERE allergyId = :allergyId";
        try{
            Connection con = sql2o.open();
            con.createQuery(sql)
                    .bind(model)
                    .executeUpdate();
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        if (!this.exists(id)){
            throw new IllegalArgumentException("No allergy found with id: " + id);
        }
        failDeleteIfRelationsExist(id);

        String sql =
                "DELETE FROM Allergies " +
                        "WHERE AllergyId = :id";
        try{
            Connection con = sql2o.open();
            con.createQuery(sql)
                    .addParameter("id",id)
                    .executeUpdate();
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
        Allergy model = this.get(id);
        if (model == null){
            return false;
        }
        return true;
    }

    @Override
    public void failIfInvalid(Allergy model)
    {
        if (model == null)
        {
            throw new IllegalArgumentException("Allergy cannot be null");
        }
        if (model.getAllergyName() == null || model.getAllergyName().length() < 1) {
            throw new IllegalArgumentException("Parameter `name` cannot be empty");
        }
        if (model.getAllergyDescription() == null || model.getAllergyDescription().length() < 1) {
            throw new IllegalArgumentException("Parameter `description` cannot be empty");
        }
    }

    @Override
    public void failDeleteIfRelationsExist(int id)
    {
        Collection<Integer> relations;
        String sql = "SELECT allergyId " +
                "FROM IngredientAllergies " +
                "WHERE allergyId = :id";
        try{
            Connection con = sql2o.open();
            relations = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(Integer.class);
        }catch (Exception e)
        {
            throw new IllegalArgumentException("Allergy not deleted. Problems with ingredients associations");
        }
        if (relations.size() < 1) throw new IllegalArgumentException("Allergy not deleted. Used in one or more ingredients");
    }
}
