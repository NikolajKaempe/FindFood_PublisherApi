package repositories.repositoryInterfaces;

import models.*;

import java.util.Collection;

/**
 * Created by Kaempe on 20-03-2017.
 */
public interface IRecipeRepository
{
    Collection<Recipe> getAll(String publisherId );
    Recipe get(int id,String publisherId);
    int create(Recipe model);
    boolean update(Recipe model);
    boolean delete(int id,String publisherId);
    boolean exists(int id);
    void failIfInvalid(Recipe model);
    RecipeType getRecipeTypeFor(int id);
    Collection<MeasuredIngredient> getMeasuredIngredientsFor(int id);
    Ingredient getIngredientFor(int id);
    Collection<Allergy> getAllergiesFor(int id);
    void failDeleteIfRelationsExist(int id);
    boolean isIngredientValid(int id);
    boolean isRecipeTypeValid(int recipeTypeId);
    boolean isPublished(int id);
    boolean isPublishersRecipe(int id, String publisherId);

}
