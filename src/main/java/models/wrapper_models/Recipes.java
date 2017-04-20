package models.wrapper_models;

import models.Recipe;

import java.util.Collection;

/**
 * Created by Kaempe on 27-03-2017.
 */
public class Recipes
{
    private Collection<Recipe> recipes;

    public Recipes(Collection<Recipe> recipes){
        this.recipes = recipes;
    }

    public Collection<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Collection<Recipe> recipes) {
        this.recipes = recipes;
    }
}
