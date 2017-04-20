package models.wrapper_models;

import models.Allergy;

import java.util.Collection;

/**
 * Created by Kaempe on 27-03-2017.
 */
public class Allergies
{
    private Collection<Allergy> allergies;

    public Allergies(Collection<Allergy> allergies){
        this.allergies = allergies;
    }

    public Collection<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(Collection<Allergy> allergies) {
        this.allergies = allergies;
    }
}
