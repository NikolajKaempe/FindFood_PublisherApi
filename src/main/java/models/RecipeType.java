package models;

/**
 * Created by Kaempe on 17-03-2017.
 */
public class RecipeType
{
    private int recipeTypeId;
    private String recipeTypeName;

    public RecipeType(){
    }

    public RecipeType(String name){
        this.recipeTypeId = 0;
        this.recipeTypeName = name;
    }

    public RecipeType(int id, String name)
    {
        this.recipeTypeId = id;
        this.recipeTypeName = name;
    }

    public int getRecipeTypeId() {
        return recipeTypeId;
    }

    public void setRecipeTypeId(int recipeTypeId) {
        this.recipeTypeId = recipeTypeId;
    }

    public String getRecipeTypeName() {
        return recipeTypeName;
    }

    public void setRecipeTypeName(String recipeTypeName) {
        this.recipeTypeName = recipeTypeName;
    }
}
