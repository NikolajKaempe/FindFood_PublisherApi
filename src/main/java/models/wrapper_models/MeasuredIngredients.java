package models.wrapper_models;

import models.MeasuredIngredient;

import java.util.Collection;

/**
 * Created by Kaempe on 25-04-2017.
 */
public class MeasuredIngredients
{
    private Collection<MeasuredIngredient> ingredients;

    public MeasuredIngredients(Collection<MeasuredIngredient> ingredients){
        this.ingredients = ingredients;
    }

    public Collection<MeasuredIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<MeasuredIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
