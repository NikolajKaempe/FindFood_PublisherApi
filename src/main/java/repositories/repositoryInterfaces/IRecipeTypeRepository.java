package repositories.repositoryInterfaces;

import models.RecipeType;

/**
 * Created by Kaempe on 17-03-2017.
 */
public interface IRecipeTypeRepository extends IRepository<RecipeType>
{
    void failDeleteIfRelationsExist(int id);
}
