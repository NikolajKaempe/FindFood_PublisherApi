package repositories.repositoryInterfaces;

import models.MealType;

/**
 * Created by Kaempe on 17-03-2017.
 */
public interface IMealTypeRepository extends IRepository<MealType>
{
    void failDeleteIfRelationsExist(int id);
}
