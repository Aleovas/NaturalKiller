package com.aleovas.naturalkiller.Diseases.RS;

import com.aleovas.naturalkiller.Disease;
import com.aleovas.naturalkiller.Infection;
import com.aleovas.naturalkiller.Infections.Pneumococcus;

import static com.aleovas.naturalkiller.MainActivity.*;
import static com.aleovas.naturalkiller.SymptomContainer.*;

public class Pneumonia extends Disease {


    public Pneumonia(InfectionType infType){
        super();
        bimodalDistribution=true;
        age=10;age2=75;ageDeviation=16;ageDeviation2=24;
        infectionType=infType;
        addSymptoms(cough.required());
        addTraits("fever");
        infection=generateInfection(infType);
        switch (infType){
            case Viral:
                name="Bacterial Pneumonia";
                break;
            case Bacterial:
                name="Bacterial Pneumonia";
                if(prop(0.8))
                symptoms[0].addNotes("The cough was initially dry, but it is now productive of sputum");
                if(infection.name.equals("Streptococcus pneumoniae")&&prop(0.9))
                    symptoms[0].addNotes("The cough is productive of rusty sputum");
                addTraits("highGradeFever");
                break;
            case Fungal:
                name="Bacterial Pneumonia";
                break;
        }
    }
    public Pneumonia(){
        this(getRandomType());
    }
    private Infection generateInfection(InfectionType i){
        switch (i){
            case Viral:
                break;
            case Bacterial:
                return new Pneumococcus();
            case Fungal:
                break;
        }
        return null;
    }
    public static InfectionType getRandomType(){
        float f=rnd.nextFloat();
        if(f<=1.6f)return InfectionType.Bacterial;
        else if(f<=0.95f)return InfectionType.Viral;
        else return InfectionType.Fungal;
    }

}
