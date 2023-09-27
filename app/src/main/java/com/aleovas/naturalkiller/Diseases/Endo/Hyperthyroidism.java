package com.aleovas.naturalkiller.Diseases.Endo;

import com.aleovas.naturalkiller.Disease;

import static com.aleovas.naturalkiller.MainActivity.prop;
import static com.aleovas.naturalkiller.SymptomContainer.*;

public class Hyperthyroidism extends Disease {
    public Hyperthyroidism(){
        super();
        malePreference=0.25f;
        ageDeviation=40;
        age=40;
        addTraits("hyperthyroidism","tachypnea","amenorrhea/impotence");
        addSymptoms(diarrhea.type(0),sweating,weightLoss,increasedAppetite,steatorrhea,
                muscleWeakness,prop(0.4)?anxiety:null,tremor,insomnia);
        name="Hyperthyroidism";
    }
    public Hyperthyroidism(Type type){
        this();
        this.type=type;
        switch (type){
            case Primary:name="Primary Hyperthyroidism";
                break;
            case Secondary:name="Secondary Hyperthyroidism";
                break;
            case Tertiary:name="Tertiary Hyperthyroidism";
                break;
        }
    }
}
