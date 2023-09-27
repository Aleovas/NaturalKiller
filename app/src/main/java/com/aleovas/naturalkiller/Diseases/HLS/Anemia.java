package com.aleovas.naturalkiller.Diseases.HLS;

import com.aleovas.naturalkiller.Disease;

import static com.aleovas.naturalkiller.MainActivity.prop;
import static com.aleovas.naturalkiller.MainActivity.rnd;
import static com.aleovas.naturalkiller.SymptomContainer.dizziness;
import static com.aleovas.naturalkiller.SymptomContainer.fatigue;
import static com.aleovas.naturalkiller.SymptomContainer.pagophagia;
import static com.aleovas.naturalkiller.SymptomContainer.paleStool;
import static com.aleovas.naturalkiller.SymptomContainer.pallor;
import static com.aleovas.naturalkiller.SymptomContainer.pica;

public class Anemia extends Disease {
    public enum AnemiaType {IDA, Megaloblastic, Sideroblastic, Aplastic, Hypovolemic, Myelopthisic, Hemolytic, Sickle, Thalassemia};
    public AnemiaType anemiaType;
    public Anemia(AnemiaType type){
        super();
        anemiaType=type;
        addSymptoms(fatigue, prop(0.4)?paleStool:null, pallor, prop(0.2)?pagophagia:null, prop(0.3)?pica:null, dizziness);
        addTraits("anemia");
        name="Anemia";
        switch (type){
            case IDA:
                malePreference=0.4f;
                addTraits("ida", "microcytic");
                name="Iron Deficiency Anemia";
                break;
            case Megaloblastic:
                addTraits("macrocytic", "megaloblastic", rnd.nextBoolean()?"folateDef":"B12def");
                name="Megaloblastic Anemia";
                break;
            case Sideroblastic:
                name="Sideroblastic Anemia";
                break;
            case Aplastic:
                name="Aplastic Anemia";
                break;
            case Hypovolemic:
                addTraits("hypotension");
                name="Acute Blood Loss";
                break;
            case Myelopthisic:
                name="Myelophthisic Anemia";
                break;
            case Hemolytic:
                name="Hemolytic Anemia";
                break;
            case Sickle:
                name="Sickle Cell Anemia";
                break;
            case Thalassemia:
                name="Thalassemia";
                break;
        }
    }
}
