package com.aleovas.naturalkiller.Diseases.Endo;

import com.aleovas.naturalkiller.Disease;
import com.aleovas.naturalkiller.MainActivity;


public class Hyperparathyroidism extends Disease {
    public Hyperparathyroidism(Type type){
        super();
        malePreference=0.25f;
        ageDeviation=40;
        age=40;
        this.type=type;
        addTraits("hyperparathyroidism");
        switch (type){
            case Primary:
                name="Primary Hyperparathyroidism";
                addTraits("hypercalcemia", "hypophosphatemia");
                break;
            case Secondary:
                name="Secondary Hyperparathyroidism";
                addTraits("hypocalcemia", "renalFailure", "hyperphosphatemia");
                break;
            case Tertiary:
                name="Tertiary Hyperparathyroidism";
                addTraits("hypercalcemia", "renalFailure", "hypophosphatemia");
                break;
        }
    }
    public Hyperparathyroidism(){
        this(Type.values()[MainActivity.rangeGenInt(0,2)]);
    }
}
