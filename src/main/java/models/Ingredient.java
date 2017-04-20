package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Kaempe on 24-02-2017.
 */
public class Ingredient
{
    private int ingredientId;
    private String ingredientName;
    private String ingredientDescription;
    private Collection<Allergy> allergies;

    public Ingredient(){
        this.allergies = new ArrayList<>();
    }

    public Ingredient(String name, String description){
        this(0,name,description);
    }

    public Ingredient(int id, String name, String description){
        this.ingredientId = id;
        this.ingredientName = name;
        this.ingredientDescription = description;
        this.allergies = new ArrayList<>();
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientDescription() {
        return ingredientDescription;
    }

    public void setIngredientDescription(String ingredientDescription) {
        this.ingredientDescription = ingredientDescription;
    }

    public Collection<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(Collection<Allergy> allergies) {
        this.allergies = allergies;
    }
}
