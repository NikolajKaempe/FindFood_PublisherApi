package models;

import java.util.Collection;

/**
 * Created by Kaempe on 28-03-2017.
 */
public class Menu
{
    private int menuId;
    private String menuName;
    private String menuDescription;
    private String menuImageFilePath;
    private MealType mealType;
    private Collection<Recipe> recipes;

    public Menu(){}

    public Menu(String name, String description, String imageFilePath,
                  MealType mealType, Collection<Recipe> recipes){
        this.menuId= 0;
        this.menuName= name;
        this.menuDescription = description;
        this.menuImageFilePath = imageFilePath;
        this.mealType = mealType;
        this.recipes = recipes;
    }

    public Menu(int id,String name, String description, String imageFilePath,
                MealType mealType, Collection<Recipe> recipes){
        this.menuId= id;
        this.menuName= name;
        this.menuDescription = description;
        this.menuImageFilePath = imageFilePath;
        this.mealType = mealType;
        this.recipes = recipes;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public String getMenuImageFilePath() {
        return menuImageFilePath;
    }

    public void setMenuImageFilePath(String menuImageFilePath) {
        this.menuImageFilePath = menuImageFilePath;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public Collection<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Collection<Recipe> recipes) {
        this.recipes = recipes;
    }
}
