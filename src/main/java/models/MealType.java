package models;

/**
 * Created by Kaempe on 17-03-2017.
 */
public class MealType
{
    private int mealTypeId;
    private String mealTypeName;

    public MealType(){

    }

    public MealType(String name){
        this.mealTypeId = 0;
        this.mealTypeName = name;
    }

    public MealType(int id, String name){
        this.mealTypeId = id;
        this.mealTypeName = name;
    }

    public int getMealTypeId() {
        return mealTypeId;
    }

    public void setMealTypeId(int mealTypeId) {
        this.mealTypeId = mealTypeId;
    }

    public String getMealTypeName() {
        return mealTypeName;
    }

    public void setMealTypeName(String mealTypeName) {
        this.mealTypeName = mealTypeName;
    }
}
