package com.aleovas.naturalkiller.Diseases.Endo;

import com.aleovas.naturalkiller.Disease;

import static com.aleovas.naturalkiller.MainActivity.prop;
import static com.aleovas.naturalkiller.SymptomContainer.*;

public class Hypothyroidism extends Disease {
    public Hypothyroidism(){
        super();
        malePreference=0.25f;
        ageDeviation=40;
        age=40;
        addTraits("hypothyroidism");
        addSymptoms(constipation,feelingCold,prop(0.7)?weightGain:null,voiceDeepining,
                decreasedAppetite,depression, somnolence,prop(0.7)?fatigue:null,
                prop(0.3)?decreasedLibido:null,menstrualIrregularities.type(2));
        name="Hypothyroidism";
    }
    public Hypothyroidism(Type type){
        this();
        this.type=type;
        switch (type){
            case Primary:name="Primary Hypothyroidism";
                break;
            case Secondary:name="Secondary Hypothyroidism";
                break;
            case Tertiary:name="Tertiary Hypothyroidism";
                break;
        }
    }
}
