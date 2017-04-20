package models.wrapper_models;

import models.Ingredient;

import java.util.Collection;

/**
 * Created by Kaempe on 27-03-2017.
 */
public class Ingredients
{
    private Collection<Ingredient> ingredients;

    public Ingredients(Collection<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    public Collection<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
