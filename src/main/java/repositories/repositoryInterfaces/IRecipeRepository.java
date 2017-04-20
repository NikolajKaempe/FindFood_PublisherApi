package repositories.repositoryInterfaces;

import models.*;

import java.util.Collection;

/**
 * Created by Kaempe on 20-03-2017.
 */
public interface IRecipeRepository extends IRepository<Recipe>
{
    RecipeType getRecipeTypeFor(int id);
    Collection<MeasuredIngredient> getMeasuredIngredientsFor(int id);
    Ingredient getIngredientFor(int id);
    Collection<Allergy> getAllergiesFor(int id);
    void failDeleteIfRelationsExist(int id);
    boolean isIngredientValid(int id);
    boolean isRecipeTypeValid(int recipeTypeId);
}
