package com.aleovas.naturalkiller.Diseases.Endo;

import com.aleovas.naturalkiller.Disease;

import static com.aleovas.naturalkiller.MainActivity.prop;
import static com.aleovas.naturalkiller.MainActivity.rangeGenInt;
import static com.aleovas.naturalkiller.MainActivity.rnd;
import static com.aleovas.naturalkiller.SymptomContainer.*;

public class PitAdenoma extends Disease {
    public enum AdenomaType {Prolactinoma, Somatotroph, Corticotroph, Thyrotroph, Gonadotroph, Nonfunctioning}
    public AdenomaType adenomaType;
    public PitAdenoma(AdenomaType type){
        super();
        malePreference=0.5f;
        ageDeviation=20;
        age=40;
        adenomaType=type;
        switch (adenomaType){
            case Prolactinoma:
                name="Prolactinoma";
                if(prop(0.7))addComorbidity(new PitAdenoma(AdenomaType.Somatotroph));
                addTraits("prolactinemia");
                if(prop(0.3))addComorbidity(new Hypothyroidism(Type.Secondary));
                break;
            case Somatotroph:
                name="Somatotroph Pituitary Adenoma";
                if(prop(0.7))addComorbidity(new PitAdenoma(AdenomaType.Prolactinoma));
                if(prop(0.3))addComorbidity(new Hypothyroidism(Type.Secondary));
                break;
            case Corticotroph:
                name="Cushing Disease";
                addTraits("cushing");
                break;
            case Thyrotroph:
                name="Thyrotroph Pituitary Adenoma";
                addComorbidity(new Hyperthyroidism(Type.Secondary));
                break;
            case Gonadotroph:
                name="Gonadotroph Pituitary Adenoma";
                break;
            case Nonfunctioning:
                name="Nonfunctioning Pituitary Adenoma";
                addTraits("hypopituitarism");
                if(prop(0.3))addComorbidity(new Hypothyroidism(Type.Secondary));
                break;
        }
        addTraits("highICP");
        addSymptoms(visualDisturbances.type(0));
    }
    public PitAdenoma(){
        this(getRandomType());
    }
    public static AdenomaType getRandomType(){
        float f=rnd.nextFloat();
        if(f<=0.3)return AdenomaType.Prolactinoma;
        else if(f<=0.5) return AdenomaType.Somatotroph;
        else if(f<=0.63) return AdenomaType.Corticotroph;
        else if(f<=0.73) return AdenomaType.Gonadotroph;
        else if(f<=0.75) return AdenomaType.Thyrotroph;
        else return AdenomaType.Nonfunctioning;
    }
}
