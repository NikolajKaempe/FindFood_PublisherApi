package repositories.repositoryInterfaces;

import models.*;

import java.util.Collection;

/**
 * Created by Kaempe on 28-03-2017.
 */
public interface IMenuRepository
{
    Collection<Menu> getAll(String publisherId);
    Menu get(int id,String publisherId);
    int create(Menu model);
    boolean update(Menu model);
    boolean delete(int id,String publisherId);
    boolean exists(int id);
    void failIfInvalid(Menu model);
    MealType getMealTypeFor(int id);
    Collection<Recipe> getRecipesFor(int id);
    Collection<Ingredient> getIngredientFor(int id);
    Collection<Allergy> getAllergiesFor(int id);
    boolean isRecipeValid(int recipeId);
    boolean isMealTypeValid(int mealTypeId);
    boolean ispublished(int id);
    boolean isPublishersMenu(int id, String publisherId);
}
