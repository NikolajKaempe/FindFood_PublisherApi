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
                    "FROM Allergies WHERE published = 1";
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
        if(!this.exists(id)){
            throw new IllegalArgumentException("No allergy with id " + id + " found.");
        }
        Allergy allergy;
        String sql =
                "SELECT allergyId, allergyName, allergyDescription " +
                    "FROM Allergies " +
                        "WHERE allergyId = :id " +
                        "AND published = 1";
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
                "INSERT INTO Allergies (allergyName, allergyDescription, published) " +
                    "VALUES (:allergyName, :allergyDescription, :published)";
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
        Allergy model;
        String sql =
                "SELECT allergyId FROM Allergies " +
                    "WHERE allergyId = :id " +
                    "AND published = 1";
        try{
            Connection con = sql2o.open();
            model = con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Allergy.class);
            if (model != null) return true;
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
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
}
