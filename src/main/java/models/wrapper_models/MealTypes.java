package models.wrapper_models;

import models.MealType;

import java.util.Collection;

/**
 * Created by Kaempe on 27-03-2017.
 */
public class MealTypes
{
    private Collection<MealType> mealTypes;

    public MealTypes(Collection<MealType> mealTypes){
        this.mealTypes = mealTypes;
    }

    public Collection<MealType> getMealTypes() {
        return mealTypes;
    }

    public void setMealTypes(Collection<MealType> mealTypes) {
        this.mealTypes = mealTypes;
    }
}
