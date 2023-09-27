package com.aleovas.naturalkiller;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by omar on 7/31/18.
 */

public class Infection {
    public enum microbe {Virus, Bacteria, Protozoa, Parasite, Fungus, None};
    public microbe type=microbe.None;
    public ArrayList<Disease> diseases=new ArrayList<>();
    public String name="no infectious agent";
    public int incubationLBound, incubationUBound;
    public Infection(){
        incubationUBound=-1;
        incubationUBound=-1;
    }
    public void addDiseases(Disease... ds){
        for(Disease d:ds)addDisease(d);
    }
    private void addDisease(Disease x){
        boolean diseaseNeedsGeneration=true;
        for(Disease d:diseases) if (x.getName() == d.getName()) diseaseNeedsGeneration = false;
        if(diseaseNeedsGeneration)diseases.add(x);
    }
}
