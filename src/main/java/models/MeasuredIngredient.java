package models;

/**
 * Created by Kaempe on 20-03-2017.
 */
public class MeasuredIngredient
{
    private int measuredIngredientId;
    private Ingredient ingredient;
    private double amount;
    private String measure;

    public MeasuredIngredient(){}

    public MeasuredIngredient(double amount, String measure, int ingredientId){
        this.ingredient = new Ingredient(ingredientId,"","");
        this.amount = amount;
        this.measure = measure;
    }

    public MeasuredIngredient(double amount, String measure, Ingredient ingredient){
        this.ingredient = ingredient;
        this.amount = amount;
        this.measure = measure;
    }

    public MeasuredIngredient(int measuredIngredientId, double amount, String measure, Ingredient ingredient){
        this.measuredIngredientId = measuredIngredientId;
        this.ingredient = ingredient;
        this.amount = amount;
        this.measure = measure;
    }

    public int getMeasuredIngredientId() {
        return measuredIngredientId;
    }

    public void setMeasuredIngredientId(int measuredIngredientId) {
        this.measuredIngredientId = measuredIngredientId;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
