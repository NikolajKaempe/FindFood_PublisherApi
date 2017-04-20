package repositories.repositoryInterfaces;

import models.Allergy;

import java.util.List;

/**
 * Created by Kaempe on 23-02-2017.
 */
public interface IAllergyRepository extends IRepository<Allergy>
{

    void failDeleteIfRelationsExist(int id);
}
