package models;

/**
 * Created by Kaempe on 23-02-2017.
 */
public class Allergy
{
    private int allergyId;
    private String allergyName;
    private String allergyDescription;

    public Allergy(){

    }

    public Allergy(String name, String description){
        this(0,name,description);
    }

    public Allergy(int id, String name, String description){
        allergyId = id;
        this.allergyName = name;
        this.allergyDescription = description;
    }

    public int getAllergyId() {
        return allergyId;
    }

    public void setAllergyId(int allergyId) {
        this.allergyId = allergyId;
    }

    public String getAllergyName() {
        return allergyName;
    }

    public void setAllergyName(String allergyName) {
        this.allergyName = allergyName;
    }

    public String getAllergyDescription() {
        return allergyDescription;
    }

    public void setAllergyDescription(String allergyDescription) {
        this.allergyDescription = allergyDescription;
    }
}
