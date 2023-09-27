package com.aleovas.naturalkiller.Infections;

import com.aleovas.naturalkiller.Disease;
import com.aleovas.naturalkiller.Diseases.Neurology.Meningitis;
import com.aleovas.naturalkiller.Diseases.RS.Pneumonia;
import com.aleovas.naturalkiller.Infection;

import static com.aleovas.naturalkiller.Diseases.Neurology.Meningitis.MeningitisType.acuteBacterialMeningitis;

public class Pneumococcus extends Infection {
    public Pneumococcus(){
        type=microbe.Bacteria;
        diseases.add(new Pneumonia(Disease.InfectionType.Bacterial));
        diseases.add(new Meningitis(acuteBacterialMeningitis));
        name="Streptococcus pneumoniae";
        incubationLBound=24;
        incubationUBound=96;
    }
}
