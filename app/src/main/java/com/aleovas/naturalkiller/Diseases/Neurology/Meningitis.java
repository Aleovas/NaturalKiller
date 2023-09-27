package com.aleovas.naturalkiller.Diseases.Neurology;

import com.aleovas.naturalkiller.Disease;

import static com.aleovas.naturalkiller.MainActivity.getRandom;
import static com.aleovas.naturalkiller.MainActivity.rangeGenInt;
import static com.aleovas.naturalkiller.MainActivity.rnd;

public class Meningitis extends Disease {
    public enum MeningitisType {acuteBacterialMeningitis,viralMeningitis,tuberculousMeningitis,fungalMeningitis,acuteSyphiliticMeningitis}
    public MeningitisType meningitisType;
    public Meningitis(){
        super();
        meningitisType=MeningitisType.values()[rangeGenInt(0,4)];
        Init();
    }
    public Meningitis(MeningitisType type){
        super();
        meningitisType=type;
        Init();
    }
    public void Init(){
        addTraits("highICP");
        addTraits("fever");
        switch (meningitisType){
            case acuteBacterialMeningitis:
                addTraits("highGradeFever");
                name="Acute Bacterial Meningitis";
                break;
            case viralMeningitis:
                name="Viral Meningitis";
                break;
            case tuberculousMeningitis:
                name="Tuberculous Meningitis";
                break;
            case fungalMeningitis:
                name="Fungal Meningitis";
                break;
            case acuteSyphiliticMeningitis:
                name="Acute Syphilitic Meningitis";
                break;
        }
        addTraits(meningitisType.name());
    }
}
