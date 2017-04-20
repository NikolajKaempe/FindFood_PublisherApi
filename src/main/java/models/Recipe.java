package models;

import java.util.Collection;

/**
 * Created by Kaempe on 20-03-2017.
 */
public class Recipe {

    private int recipeId;
    private String recipeName;
    private String recipeDescription;
    private String recipeImageFilePath;
    private RecipeType recipeType;
    private Collection<MeasuredIngredient> measuredIngredients;

    public Recipe(){}

    public Recipe(String name, String description, String imageFilePath,
                  RecipeType recipeType, Collection<MeasuredIngredient> measuredIngredients){
        this.recipeId = 0;
        this.recipeName = name;
        this.recipeDescription = description;
        this.recipeImageFilePath = imageFilePath;
        this.recipeType = recipeType;
        this.measuredIngredients = measuredIngredients;
    }

    public Recipe(int id, String name, String description, String imageFilePath,
                  RecipeType recipeType, Collection<MeasuredIngredient> measuredIngredients){
        this.recipeId = id;
        this.recipeName = name;
        this.recipeDescription = description;
        this.recipeImageFilePath = imageFilePath;
        this.recipeType = recipeType;
        this.measuredIngredients = measuredIngredients;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public String getRecipeImageFilePath() {
        return recipeImageFilePath;
    }

    public void setRecipeImageFilePath(String recipeImageFilePath) {
        this.recipeImageFilePath = recipeImageFilePath;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(RecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public Collection<MeasuredIngredient> getMeasuredIngredients() {
        return measuredIngredients;
    }

    public void setMeasuredIngredients(Collection<MeasuredIngredient> measuredIngredients) {
        this.measuredIngredients = measuredIngredients;
    }
}
