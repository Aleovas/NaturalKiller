package com.aleovas.naturalkiller;

import com.aleovas.naturalkiller.Symptom;

import java.util.ArrayList;

import static com.aleovas.naturalkiller.MainActivity.array;
import static com.aleovas.naturalkiller.MainActivity.getRandom;

/**
 * Created by omar on 7/31/18.
 */

public class SymptomContainer {
    public static Symptom abdominalPain=new Symptom("abdominal pain");
    public static Symptom anxiety=new Symptom("anxiety", "extreme nervousness");
    public static Symptom carpopedal=new Symptom("hand spasms", "hand spasms", "hand spasms","spasms", "spasms", "carpopedal spasm", "tetany in the hand");
    public static Symptom cough=new Symptom("a cough");
    public static Symptom constipation=new Symptom("constipation", "infrequent bowel movements");
    public static Symptom depression=new Symptom(array("depression"),array("depressed"));
    public static Symptom decreasedAppetite=new Symptom("decreased appetite");
    public static Symptom decreasedLibido=new Symptom("decreased libido");
    public static Symptom diarrhea=new Symptom("diarrhea", "frequent bowel movement");
    public static Symptom dizziness=new Symptom(array("dizziness", "light-headedness"),array("light-headed","dizzy"));
    public static Symptom fatigue=new Symptom(array("fatigue", "weakness"),array("tired"));
    public static Symptom feelingCold=new Symptom("feeling cold", "cold intolerance");
    public static Symptom headache=new Symptom("headaches");
    public static Symptom galactorrhea=new Symptom("galactorrhea","milk discharge");
    public static Symptom increasedAppetite=new Symptom("increased appetite", "increased appetite", "increased hunger");
    public static Symptom insomnia=new Symptom("insomnia", "inability to sleep");
    public static Symptom impotence=new Symptom("impotence", "erectile dysfunction");
    public static Symptom muscleWeakness=new Symptom("muscle weakness");
    public static Symptom menstrualIrregularities=new Symptom("menstrual irregularities");
    public static Symptom nausea=new Symptom(array("nausea", "nausea and vomiting", "frequent vomiting"),array("nauseous"));
    public static Symptom neckStiffness=new Symptom("a stiff neck", "neck stiffness");
    public static Symptom pagophagia=new Symptom("cravings to eat ice");
    public static Symptom paleStool=new Symptom("pale stools");
    public static Symptom pallor=new Symptom(array("pallor", "paleness"),array("pale"));
    public static Symptom pica=new Symptom("cravings to eat dirt");
    public static Symptom polyuria =new Symptom("frequent urination");
    public static Symptom poorAppetite=new Symptom("poor appetite");
    public static Symptom seizures =new Symptom("seizures", "convulsions");
    public static Symptom steatorrhea=new Symptom("foul-smelling feces", "foul-smelling feces", "foul-smelling feces",
            "fatty feces", "oily feces", "steatorrhea");
    public static Symptom stomachache =new Symptom("stomach pain", "a stomachache","abdominal pain");
    public static Symptom somnolence=new Symptom("feeling sleepy", "sleeping alot");
    public static Symptom sweating=new Symptom("sweating");
    public static Symptom tingling=new Symptom("tingling");
    public static Symptom tremor=new Symptom("tremors");
    public static Symptom visualDisturbances=new Symptom("visual disturbances");
    public static Symptom voiceDeepining=new Symptom("voice deepining");
    public static Symptom weightGain=new Symptom("weight gain");
    public static Symptom weightLoss=new Symptom("rapid weight-loss", "severe weight loss", "unwanted weight-loss",
            "severe unwanted weight-loss", "rapid unwanted weight-loss", "weight loss");
    public static void init(){
        diarrhea.addQueries("The diarrhea is watery",
                "The diarrhea is bloody",
                "The diarrhea is mucoid",
                "The diarrhea is dysenteric");
        visualDisturbances.addQueries("The visual disturbances manifested as Bitemporal Hemianopia", "Nonspecific visual disturbances");
        menstrualIrregularities.addQueries("Amenorrhea", "Oligomenorrhea", "Polymenorrhea");
    }


}
