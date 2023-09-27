package com.aleovas.naturalkiller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;

import static com.aleovas.naturalkiller.MainActivity.frequency;
import static com.aleovas.naturalkiller.MainActivity.gender;
import static com.aleovas.naturalkiller.MainActivity.getFuzzyTime;
import static com.aleovas.naturalkiller.MainActivity.getRandom;
import static com.aleovas.naturalkiller.MainActivity.needsHistory;
import static com.aleovas.naturalkiller.MainActivity.prop;
import static com.aleovas.naturalkiller.MainActivity.rangeGenInt;
import static com.aleovas.naturalkiller.MainActivity.rnd;
import static com.aleovas.naturalkiller.SymptomContainer.abdominalPain;
import static com.aleovas.naturalkiller.SymptomContainer.carpopedal;
import static com.aleovas.naturalkiller.SymptomContainer.constipation;
import static com.aleovas.naturalkiller.SymptomContainer.depression;
import static com.aleovas.naturalkiller.SymptomContainer.fatigue;
import static com.aleovas.naturalkiller.SymptomContainer.galactorrhea;
import static com.aleovas.naturalkiller.SymptomContainer.headache;
import static com.aleovas.naturalkiller.SymptomContainer.impotence;
import static com.aleovas.naturalkiller.SymptomContainer.menstrualIrregularities;
import static com.aleovas.naturalkiller.SymptomContainer.nausea;
import static com.aleovas.naturalkiller.SymptomContainer.neckStiffness;
import static com.aleovas.naturalkiller.SymptomContainer.polyuria;
import static com.aleovas.naturalkiller.SymptomContainer.seizures;
import static com.aleovas.naturalkiller.SymptomContainer.tingling;
import static com.aleovas.naturalkiller.SymptomContainer.visualDisturbances;

/**
 * Created by omar on 7/31/18.
 */

public class Disease {
    public enum Type {Primary, Secondary, Tertiary}
    public Type type=Type.Primary;
    protected Symptom[] symptoms;
    public Infection[] infections;
    public Infection infection;
    public enum InfectionType {Viral,Bacterial,Fungal}
    public InfectionType infectionType;
    String[] specialComments={};
    public ArrayList<String> traits=new ArrayList<>();
    public float malePreference=0.5f;
    public int getAge() {
        return age;
    }
    protected int age=45;
    boolean chronic=false;
    public float ageDeviation=90;
    public boolean bimodalDistribution=false;
    public int age2=45;
    public float ageDeviation2=90;
    ArrayList<Symptom> undiscovered=new ArrayList<>();
    ArrayList<Symptom> discovered=new ArrayList<>();
    ArrayList<String> hisotries=new ArrayList<>();
    public boolean hereditary=false;
    public boolean xlinked=false;
    protected String name="Malingering";
    public int onset=-1;
    protected boolean init=false;
    public HashMap<String, String> historyDescriptions=new HashMap<>();
    ArrayList<Disease> comorbidities=new ArrayList<>();
    private ArrayList<String> comorbidityOf=new ArrayList<>();
    public int comorbidityDepth=2;
    public Disease(Symptom[] symptoms, Infection[] infections){
        this.infections=infections;
        this.symptoms=symptoms;
        age=45;
    }
    public Disease(){
        infections=new Infection[0];
        symptoms=new Symptom[0];
    }
    public void init(){
        if(infections.length>0&&infection==null){
            infection=infections[rnd.nextInt(infections.length)];
            infection.addDiseases(this);
        }
        if(infection!=null&&infection.incubationUBound!=-1)onset=rangeGenInt(infection.incubationLBound,infection.incubationUBound);
        if(needsHistory){
            populateHistoryDescriptions();
            needsHistory=false;
        }
        setTraits();
        historyGenerator();
        symptomGenerator();
        comorbidityGenerator();
        reset();
        init=true;
    }

    private void symptomGenerator() {
        if(checkTraits("hypercalcemia"))addSymptoms(constipation, seizures, depression, polyuria, fatigue, abdominalPain);
        if(checkTraits("hypocalcemia"))addSymptoms(carpopedal, tingling);
        if(checkTraits("highICP"))addSymptoms(headache,nausea,prop(0.2)?visualDisturbances.type(0):null,prop(0.7)?neckStiffness:null);
        if(checkTraits("amenorrhea/impotence")&&MainActivity.age>=18&&!checkTraits("transgender")){
            if(gender== MainActivity.Gender.Male)addSymptoms(impotence);else addSymptoms(menstrualIrregularities.type(rangeGenInt(0,1)));
        }
        if(checkTraits("prolactinemia")&&gender==MainActivity.Gender.Female)addSymptoms(galactorrhea);
    }

    private void historyGenerator() {
        boolean m=gender== MainActivity.Gender.Male||(gender== MainActivity.Gender.Nonbinary&&checkHistory("AMAB")&&prop(0.5));
        boolean f=(gender== MainActivity.Gender.Female||checkHistory("AFAB"))&&!checkHistory("AMAB");
        int age=MainActivity.age;
        if(age>12&&age<70&&rnd.nextFloat()<=(m?0.22:0.15))hisotries.add("athletic");
        if(age>=16&&age<20&&f&&rnd.nextFloat()<=0.05)hisotries.add("pregnant");
        if(age>=20&&age<30&&f&&rnd.nextFloat()<=0.15)hisotries.add("pregnant");
        if(age>=20&&age<30&&f&&rnd.nextFloat()<=0.2)hisotries.add("previousPregnancy");
        if(age>=30&&age<=45&&f&&rnd.nextFloat()<=0.35)hisotries.add("pregnant");
        if(age>=30&&age<=45&&f&&rnd.nextFloat()<=0.5)hisotries.add("previousPregnancy");
        if(age>45&&f&&rnd.nextFloat()<=0.75)hisotries.add("previousPregnancy");
        if(rnd.nextFloat()<=0.1)hisotries.add(getRandom("chicken","beef","salad","custard","cannedFood","mansaf","rice","seaFood"));
        if(checkTraits("hypercalcemia")&&prop(0.3))addHistory("gallstone");
        if(checkTraits("hypercalcemia")&&prop(0.5))addHistory("pepticUlcer");
        if(checkTraits("hypercalcemia")&&prop(0.4))addHistory("renalstone");
        if(checkTraits("hypocalcemia")&&prop(0.25))addHistory("cataracts");
        if(age>55&&prop(0.2))addHistory("cataracts");
    }
    public int getSymptomNo(){
//        if(!init)init();
        return symptoms.length;
    }
    private void setTraits(){
        if(infection!=null) {
            if (infection.type == Infection.microbe.Bacteria)
                addTraits("bacterial", chronic?"mononuclear":"neutrophilia", "infection", "inflammation", "highFever");
            if (infection.type == Infection.microbe.Fungus)
                addTraits("fungal", "mononuclear", "infection", "inflammation");
            if (infection.type == Infection.microbe.Virus)
                addTraits("viral", "mononuclear", "infection", "inflammation");
            if(checkTraits("hypercalcemia")&&prop(0.3))addTraits("gallstone");
            if(checkTraits("hypercalcemia")&&prop(0.3))addTraits("renalstone");
        }
        if(traits.contains("granulocytosis"))addTraits("neutrophilia", "basophilia", "eosinophilia");
        if(checkTraits("fever")){
            addTraits("tachycardia");
            if(rnd.nextFloat()<=0.6)addTraits("tachypnea");
        }
        if(checkTraits("hyperthyroidism"))addTraits("tachycardia");
        if(checkTraits("hypothyroidism"))addTraits("bradycardia");
        if(checkTraits("prolactinemia"))addTraits("amenorrhea/impotence");
        if(checkTraits("cushing"))addTraits("hyperglycemia");
    }
    public Disease(Symptom[] symptoms, Infection[] infections,String[] comments){
        this(symptoms,infections);
        specialComments=comments;
    }
    public Disease(Symptom[] symptoms, Infection[] infections,String name){
        this(symptoms,infections);
        this.name=name;
    }
    public String getSymptom(){
        if(undiscovered.size()>0){
            Symptom s=null;
            for(Symptom x:undiscovered) if (x.required) s = x;
            if(s==null)s=undiscovered.get(rnd.nextInt(undiscovered.size()));
            discovered.add(s);
            undiscovered.remove(s);
            return s.getRandomString();
        }else return "has been complaining of no symptoms";
    }
    String getSymptomFragment(){
        if(!init)init();
        if(undiscovered.size()>0){
            Symptom s=undiscovered.get(rnd.nextInt(undiscovered.size()));
            discovered.add(s);
            undiscovered.remove(s);
            return s.getRandomStringFragment();
        }else return "ERROR";
    }
    public void addTraits(String... ts){
        traits.addAll(Arrays.asList(ts));
    }
    boolean checkTraits(String... ts){
        for(String s:ts)if(traits.contains(s))return true;
        return false;
    }
    boolean checkHistory(String... ts){
        for(String s:ts)if(hisotries.contains(s))return true;
        return false;
    }
    String getHistory(String tag){
        if(historyDescriptions.containsKey(tag))return historyDescriptions.get(tag);else return tag;
    }
    protected void addHistory(String s){
        if(!hisotries.contains(s))hisotries.add(s);
    }
    protected void addSymptoms(Symptom... ts){
        ArrayList<Symptom> sym=new ArrayList<>(Arrays.asList(symptoms));
        for(Symptom s:ts)if(s!=null&&!contains(sym, s))sym.add(s);
        symptoms=sym.toArray(new Symptom[0]);
    }
    boolean contains(ArrayList<Symptom> symptoms, Symptom symptom){
        for(Symptom s:symptoms)if(symptom.match(s))return true;
        return false;
    }
    @Override
    public String toString() {
        return getName();
    }
    public String getName() {
        return name;
    }
    Disease reset(){
        undiscovered.clear();
        undiscovered.addAll(Arrays.asList(symptoms));
        init=false;
        return this;
    }
    private void populateHistoryDescriptions() {
        historyDescriptions.put("athletic",header()+(rnd.nextFloat()<=0.3?"is athletic":getSport()));
        historyDescriptions.put("pregnant",header()+ "is "+(rnd.nextBoolean()?"pregnant":"expecting"));
        historyDescriptions.put("previousPregnancy",header()+"has previously given birth");
        historyDescriptions.put("chicken",header()+getFoodString("chicken"));
        historyDescriptions.put("beef",header()+getFoodString("beef"));
        historyDescriptions.put("salad",header()+getFoodString("a salad"));
        historyDescriptions.put("custard",header()+getFoodString("custard"));
        historyDescriptions.put("cannedFood",header()+getFoodString("canned food"));
        historyDescriptions.put("mansaf",header()+getFoodString("mansaf"));
        historyDescriptions.put("rice",header()+getFoodString("rice"));
        historyDescriptions.put("seaFood",header()+getFoodString(getRandom("sea food", "fish", "oysters", "shellfish", "shrimps")));
        historyDescriptions.put("renalstone",header()+"has had "+(rnd.nextBoolean()?"kidney ":"renal ")+"stones in the past");
        historyDescriptions.put("gallstone",header()+"has had gallstones in the past");
        historyDescriptions.put("cataracts",header()+"has cataracts");
        historyDescriptions.put("thyroidectomy",header()+getRandom("has had a thyroidectomy","has had %gp thyroid gland removed","has previously had "+getRandom("hyperthyroidism","thyroid cancer")));
        historyDescriptions.put("pepticUlcer",header()+"has had peptic ulcers in the past");
        historyDescriptions.put("AMAB",header()+"was assigned male at birth");
        historyDescriptions.put("AFAB",header()+"was assigned female at birth");
        for(String k:historyDescriptions.keySet()){
            if(historyDescriptions.get(k).contains("%gsc")&&gender== MainActivity.Gender.Nonbinary){
                historyDescriptions.put(k,historyDescriptions.get(k).replaceAll(" is "," are "));
                historyDescriptions.put(k,historyDescriptions.get(k).replaceAll(" has "," have "));
                historyDescriptions.put(k,historyDescriptions.get(k).replaceAll(" was "," were "));
            }
        }
    }
    private String getSport(){
        ArrayList<String> strings=new ArrayList<>();
        String[] plays={"football","soccer","baseball","hockey", "tennis", "basketball", "volleyball"};
        String[] is={"a swimmer", "a climber", "a hiker", "a cycler", "a marathon runner"};
        boolean pre=rnd.nextBoolean();
        for(String s:plays)strings.add((pre?getRandom(frequency)+" ":"")+"plays "+s+(!pre?" "+getRandom(frequency):""));
        for(String s:is)strings.add("is "+s);
        return getRandom(strings);
    }
    private String getFoodString(String s){
        String temp="";
        boolean pre=rnd.nextBoolean();
        boolean undefinedIncubation=false;
        if(onset<0){
            undefinedIncubation=true;
        }
        if(rnd.nextFloat()>=0.9)undefinedIncubation=true;
        temp+=((pre&&undefinedIncubation)?"recently ":"")+
                (rnd.nextBoolean()?"went to a restaurant where "+(rnd.nextBoolean()?(s+" was being served"):("%gs "+(rnd.nextBoolean()?"had ":"ate ")+s))
                        :(rnd.nextBoolean()?"had ":"ate ")+s)+
                (((undefinedIncubation&&!pre)?" recently":undefinedIncubation?"":(" "+getFuzzyTime(onset))));
        return temp;
    }
    private String header(){
        return prop(0.5)?"The patient ":"%gsc ";
    }
    public void addComorbidity(Disease d){
        if(comorbidityDepth>0&&!comorbidityOf.contains(d.name)){
            d.comorbidityDepth=this.comorbidityDepth-1;
            d.comorbidityOf.add(name);
            d.comorbidityOf.addAll(this.comorbidityOf);
            comorbidities.add(d);
        }
    }
    private void comorbidityGenerator(){
        for(Disease d:comorbidities){
            if(!d.init)d.init();
            addSymptoms(d.symptoms);
            addTraits(d.traits.toArray(new String[0]));
        }
    }



}
