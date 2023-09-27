package com.aleovas.naturalkiller.Diseases.HLS;

import com.aleovas.naturalkiller.Disease;
import com.aleovas.naturalkiller.Infections.Salmonella;
import com.aleovas.naturalkiller.Symptom;
import com.aleovas.naturalkiller.Infection;

import static com.aleovas.naturalkiller.SymptomContainer.*;

/**
 * Created by omar on 7/31/18.
 */

public class Typhoid extends Disease{
    static Symptom[] symptoms={diarrhea.type(0),headache,poorAppetite, stomachache};
    static Infection salmonella=new Salmonella();
    public Typhoid() {
        super(symptoms, new Infection[]{salmonella}, "Salmonella Gastroenteritis");
        addTraits("fever");
        addHistory("chicken");
    }
}
