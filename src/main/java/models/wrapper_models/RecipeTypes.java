package models.wrapper_models;

import models.Recipe;
import models.RecipeType;

import java.util.Collection;

/**
 * Created by Kaempe on 27-03-2017.
 */
public class RecipeTypes
{
    private Collection<RecipeType> recipeTypes;

    public RecipeTypes(Collection<RecipeType> recipeTypes){
        this.recipeTypes = recipeTypes;
    }

    public Collection<RecipeType> getRecipeTypes() {
        return recipeTypes;
    }

    public void setRecipeTypes(Collection<RecipeType> recipeTypes) {
        this.recipeTypes = recipeTypes;
    }
}
