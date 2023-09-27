package com.aleovas.naturalkiller;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.Preference;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aleovas.naturalkiller.Diseases.Endo.PitAdenoma;

import java.util.ArrayList;

import static com.aleovas.naturalkiller.MainActivity.age;
import static com.aleovas.naturalkiller.MainActivity.easyMode;
import static com.aleovas.naturalkiller.MainActivity.easyinessModifier;
import static com.aleovas.naturalkiller.MainActivity.formatStringArraylist;
import static com.aleovas.naturalkiller.MainActivity.gender;
import static com.aleovas.naturalkiller.MainActivity.getRandom;
import static com.aleovas.naturalkiller.MainActivity.header;
import static com.aleovas.naturalkiller.MainActivity.intCheck;
import static com.aleovas.naturalkiller.MainActivity.rangeGen;
import static com.aleovas.naturalkiller.MainActivity.rangeGenInt;
import static com.aleovas.naturalkiller.MainActivity.rnd;
import static com.aleovas.naturalkiller.MainActivity.round;
import static com.aleovas.naturalkiller.MainActivity.array;

public class Tests {
    Disease d;
    float temperature=0;
    float k=-1, Na =-1,hco3=-1;
    Boolean m=gender== MainActivity.Gender.Male;
    public ArrayList<Test> labs=new ArrayList<>();
    public ArrayList<Test> exams=new ArrayList<>();
    public ArrayList<Test> imagingTechs=new ArrayList<>();
    public Test history;
    Context context;
    public Tests(Disease d, Context context){
        this.d =d;
        this.context=context;
        initLabs();
        initExams();
        initImaging();
        history=new Test("History", this::getHistory);
        if(!this.d.init) this.d.init();
    }
    private void initExams() {
        //exams.add(new Test("Temperature",()->getTemperature()));
        exams.add(new Test("Vitals", this::getVitals));
        exams.add(new Test("More symptoms?", this::getMoreSymptoms));
        exams.add(new Test("Ask about symptoms?", this::getSymptomDetails,true));
//        exams.add(new Test("Palpation",()->palpate()));
        exams.add(new Test("Stethoscope", this::stethoscopeText, this::stethoscopeSoundView));
    }

    private void initLabs(){
        labs.add(new Test("CBC",()->CBCtext(d)));
        labs.add(new Test("Ions and Metals", this::ionConcentrations));
//        labs.add(new Test("Liver Function Tests",()->LFT()));
//        labs.add(new Test("Kidney Function Tests",()->KFT()));
//        labs.add(new Test("Sugar Tests",()->sugarTests()));
        labs.add(new Test("CSF Examination", this::CSFExam));
    }
    private void initImaging(){
        imagingTechs.add(new Test("MRI",this::mriText,this::mriView));
    }
    String CBCtext(Disease d){
//        String temp="";
//        for(Value v:CBC(d))temp+=v+"\n";
//        return temp;
        return valuesToString(CBC(d));
    }
    interface TestGenerator {
        String genText();
    }
    interface TestGeneratorGenerator{
        ArrayList<Test> getTests();
    }
    interface ViewGenerator{
        View getView();
    }
    public class Test{
        public String name;
        TestGenerator generator;
        ViewGenerator viewGenerator;
        TestGeneratorGenerator testGeneratorGenerator;
        Float price;
        String text;
        View v;
        private boolean generated=false;
        private boolean generatedView=false;
        boolean infiniteGeneration=false;
        boolean isTestGenerator=false;
        Test(String name, TestGenerator testGenerator, float price, ViewGenerator view){
            this.name=name;
            this.price=price;
            generator= testGenerator;
            text="";
            this.viewGenerator=view;
        }
        public Test(String name, TestGeneratorGenerator generator, float price){
            this.name=name;
            this.testGeneratorGenerator=generator;
            isTestGenerator=true;
            this.price=price;
        }
        Test(String name, TestGenerator testGenerator){
            this(name, testGenerator,0, null);
        }
        Test(String name, TestGenerator testGenerator, boolean infiniteGeneration){
            this(name, testGenerator,0, null);
            this.infiniteGeneration=infiniteGeneration;
        }
        Test(String name, TestGeneratorGenerator testGenerator, boolean infiniteGeneration){
            this(name, testGenerator, 0);
            this.infiniteGeneration=infiniteGeneration;
        }
        Test(String name, TestGenerator testGenerator, ViewGenerator view){
            this(name, testGenerator,0, view);
        }
        String getText(){
            if(generated&&!infiniteGeneration)return text;
            text=generator.genText();
            generated=true;
            return text;
        }
        public View getView(){
            if(generatedView&&!infiniteGeneration)return v;
            v=viewGenerator.getView();
            generatedView=true;
            return v;
        }
    }
    public class Value{
        String name, unit;
        float value, lowerBound=-1, upperBound=-1;
        Value(String n, float v, String u){
            name=n;value=v;unit=u;
        }
        Value(String n, float v, String u, float lb, float ub){
            name=n;value=v;unit=u;lowerBound=lb;upperBound=ub;
        }

        @Override
        public String toString() {
            String star="";
            if(easyMode){
                switch (easyinessModifier){
                    case 2:
                        if(lowerBound!=-1&&value<lowerBound)star=" ↓↓↓";
                        else if(upperBound!=-1&&value>upperBound)star=" ↑↑↑";
                        break;
                    case 1:star=!(lowerBound==-1&&upperBound==-1)&&(value<lowerBound||value>upperBound)?" ***":"";
                        break;
                    default: break;
                }
            }
            return name+" = "+intCheck(round(value,2))+unit+star;
        }
    }
    String[] getNames(ArrayList<Test> tests){
        ArrayList<String> names=new ArrayList<>();
        for(Test t:tests)names.add(t.name);
        return names.toArray(new String[0]);
    }
    private String getHistory(){
        String temp="";
        for(String s: d.hisotries)temp+="• "+ d.getHistory(s)+"\n";
        if(temp.equals(""))temp="Nothing of note";
        return temp;
    }
    private ArrayList<Value> CBC(Disease d){
        ArrayList<Value> values=new ArrayList<>();
        float rbc, hgb,mcv,hct,neut,lymph,mono,eos,baso;
        boolean anemic=d.checkTraits("anemia");
        boolean polycythemic=d.checkTraits("polycythemia");
        boolean lymphocytosis=d.checkTraits("lymphocytosis", "mononuclear");
        boolean monocytosis=d.checkTraits("monocytosis", "mononuclear");
        boolean neutrophilia=d.checkTraits("neutrophilia");
        boolean neutropenic=d.checkTraits("neutropenic");
        boolean eosinophilia=d.checkTraits("eosinophilia", "allergic");
        boolean basophilia=d.checkTraits("basophilia", "allergic");
        boolean leukocytosis=neutrophilia||lymphocytosis||monocytosis||d.checkTraits("inflammation");
        rbc=anemic?(m?rangeGen(2,4.3):rangeGen(1.6,3.5)):polycythemic?(m?rangeGen(6,10):rangeGen(5.5,9)):m?rangeGen(4.3,5.9):rangeGen(3.5,5.5);
        mcv=d.checkTraits("microcytic")?rangeGen(60,80):d.checkTraits("macrocytic")?rangeGen(100,160):rangeGen(80,100);
        hct=mcv*rbc/10;
        hgb=((anemic?(m?rangeGen(6,13):rangeGen(5,12)):polycythemic?(m?rangeGen(17.5,28):rangeGen(16,25)):m?rangeGen(13.5,17.5):rangeGen(12,16))+hct*2/3)/3;
        if(neutrophilia){
            neut=rangeGenInt(68,82);
            baso=basophilia?rangeGenInt(1,3):rangeGen(0,0.75);
            eos=eosinophilia?rangeGenInt(4,10):rangeGenInt(1,2);
            mono=monocytosis?rangeGenInt(7,15):rangeGenInt(3,4);
            lymph=100-neut-((int)baso)-eos-mono;
        }else if(neutropenic){
            neut=rangeGenInt(30,50);
            baso=basophilia?rangeGenInt(3,7):rangeGen(0,0.75);
            eos=eosinophilia?rangeGenInt(9,19):rangeGenInt(4,6);
            mono=monocytosis?rangeGenInt(9,19):rangeGenInt(5,10);
            lymph=100-neut-((int)baso)-eos-mono;
        }else{
            lymph=lymphocytosis?rangeGenInt(34,48):rangeGenInt(25,33);
            baso=basophilia?rangeGenInt(1,5):rangeGen(0,0.75);
            eos=eosinophilia?rangeGenInt(7,15):rangeGenInt(1,3);
            mono=monocytosis?rangeGenInt(7,15):rangeGenInt(3,7);
            neut=100-lymph-((int)baso)-eos-mono;
        }
        values.add(new Value("RBC",rbc," million/mm³",m?4.3f:3.5f,m?5.9f:5.5f));
        values.add(new Value("HGB",hgb," g/dL",m?13.5f:12,18));
        values.add(new Value("HGB A1c",d.checkTraits("hyperglycemia")?rangeGenInt(7,10):rangeGenInt(3,6),"%",3,6));
        values.add(new Value("HCT",hct,"%",m?34.4f:28,m?59:55));
        values.add(new Value("MCV",mcv," fl",80,100));
        values.add(new Value("MCH",hgb*10/rbc," Pg",25.4f,34.6f));
        values.add(new Value("MCHC",hgb*100/hct," g/dL",31,36));
        values.add(new Value("Platelets",d.checkTraits("thrombocytopenia")?rangeGenInt(20,150):d.checkTraits("thrombocythemia")?rangeGenInt(450,750):rangeGenInt(150,450),"×10³/µL",150,450));
        values.add(new Value("RDW",d.checkTraits("anisocytosis")?rangeGen(14.6,25):rangeGen(11.5,14.5),"%",11.5f,14.5f));
        values.add(new Value("RET",d.checkTraits("reticulocytosis", "hemolytic")?rangeGen(2.6,5):rangeGen(0.5,1.5),"%",0.5f,1.5f));
        values.add(new Value("ESR",d.checkTraits("inflammation")?(m?rangeGenInt(15,25):rangeGenInt(20,30)):rangeGenInt(0,15)," mm/h",0,15));
        values.add(new Value("WBC",d.checkTraits("leukomoid")?rangeGen(50,100):leukocytosis?rangeGen(12,40):rangeGen(4.5,11),"×10³/µL",4.5f,11));
        values.add(new Value("NEUT",neut,"%"));
        values.add(new Value("LYMPH",lymph,"%"));
        values.add(new Value("MONO",mono,"%"));
        values.add(new Value("EOS",eos,"%"));
        values.add(new Value("BASO",baso,"%"));
        return values;
    }
    private void calculateTemp(){
        if(temperature==0){
            double base=37.5;
            if(d.checkTraits("fever")){
                base+=rangeGen(0.5,1);
                if(d.checkTraits("highGradeFever"))base+=rangeGen(0.5,2);
            }else {
                base-=rnd.nextFloat()*1.5;
            }
            if(d.checkTraits("hyperthyroidism"))base+=rangeGen(0,1.5);
            if(d.checkTraits("hypothyroidism"))base-=rangeGen(0,1);
            temperature= (float) base;
        }
    }
    public String getVitals(){
        calculateTemp();
        ArrayList<Value> values=new ArrayList<>();
        int hr, bps, bpd, rr, rrl, rru, hrl,hru;
        values.add(new Value("Temperature",temperature,"°C",36,37.5f));
        hrl=(age<=0?80:age<=2?80:age<=4?80:age<=6?75:age<=9?70:65)
                -((int)((d.checkHistory("athletic")||d.checkTraits("bradycardia"))?rnd.nextFloat()*0.3+0.7:0)*25);
        hru=(age<=0?170:age<=2?130:age<=4?120:age<=6?115:age<=9?110:90)
                -((int)((d.checkHistory("athletic")||d.checkTraits("bradycardia"))?rnd.nextFloat()*0.3+0.7:0)*25);
        hr= d.checkTraits("fibrillation")?rangeGenInt(200,220):
                d.checkTraits("flutter")?rangeGenInt(hru+20,200):
                        d.checkTraits("tachycardia")?rangeGenInt(hru,hru+20):
                                rangeGenInt(hrl,hru);
        values.add(new Value("Heart Rate",hr," BPM",hrl,hru));
        bps= d.checkTraits("hypertension")?rangeGenInt(140,180):
                d.checkTraits("hypotension")?rangeGenInt(75,90):
                        rangeGenInt(110,125);
        bpd= d.checkTraits("hypertension")?rangeGenInt(90,120):
                d.checkTraits("hypotension")?rangeGenInt(40,60):
                        rangeGenInt(75,85);
        bps+=d.checkTraits("hyperthyroidism")?rangeGenInt(8,12):d.checkTraits("hypothyroidism")?-rangeGenInt(8,12):0;
        bpd-=d.checkTraits("hyperthyroidism")?rangeGenInt(8,12):d.checkTraits("hypothyroidism")?-rangeGenInt(8,12):0;
        values.add(new Value("Systolic BP",bps,"mmHg",110,125));
        values.add(new Value("Diastolic BP",bpd,"mmHg",75,85));
        rrl=age<=-6?24:age<=0?30:age<=5?20:12;
        rru=age<=-6?30:age<=0?60:age<=5?30:20;
        rr= d.checkTraits("tachypnea")?rangeGenInt(rru,rru+10): d.checkTraits("bradypnea")?rangeGenInt(rrl-6,rrl):rangeGenInt(rrl,rru);
        values.add(new Value("Respiratory rate",rr," BPM",rrl,rru));
        String temp="";
        for(Value v:values)temp+=v+"\n";
        return temp.trim();
    }
    private String getMoreSymptoms() {
        ArrayList<String> syms=new ArrayList<>();
        int symNo= (int) Math.min(Math.round(d.undiscovered.size()*0.3+easyinessModifier*0.2),4);
        if (symNo==0)return getRandom(getRandom("None", "There are no "+getRandom("more", "other")+" symptoms")+" that %gs can recall",
                header()+getRandom("can't recall having any more symptoms",
                        "doesn't seem to "+getRandom("remember","recall")+" having any "+getRandom("more", "other")+" symptoms"));
        for(int i=0;i<symNo;i++){
            ArrayList<String> options=new ArrayList<>();
            Symptom s=d.undiscovered.get(rnd.nextInt(d.undiscovered.size()));
            options.add(header()+getRandom("also ")+s.getRandomString());
            options.add(header()+getRandom("forgot to mention that %gs ","just remembered that %gs ")
                    +getRandom("suffers from ", "has been complaining of ")+s.getRandomStringFragment());
            syms.add(getRandom(options));
            d.discovered.add(s);
            d.undiscovered.remove(s);
        }
        return formatStringArraylist(syms);
    }

    private ArrayList<Test> getSymptomDetails(){
        ArrayList<Test> tests=new ArrayList<>();
        ArrayList<Symptom> inquirableSymptoms=new ArrayList<>();
        for(Symptom s:d.discovered) if(s.inquirable) inquirableSymptoms.add(s);
        for(Symptom s:inquirableSymptoms){
            tests.add(new Test("Get details about \""+s.getChosenTerm()+"\"", s::getQuery));
        }
        return tests;
    }
    public String palpate(){
        //TODO
        return "";
    }
    public String stethoscopeText(){
        //TODO
        return "";
    }
    public View stethoscopeSoundView(){
        //TODO
        return null;
    }
    public String ionConcentrations(){
        ArrayList<Value> values=new ArrayList<>();
        float cl, ca, mg, p,fe, fer, tran, tibc, anionGap;
        if(k==-1)k=d.checkTraits("hyperkalemia")?rangeGen(5.1,16):d.checkTraits("hypokalemia")?rangeGen(0.5,3.4):rangeGen(3.5,5);
        if(Na ==-1) Na =d.checkTraits("hypernatremia")?rangeGenInt(147,200):d.checkTraits("hyponatremia")?rangeGenInt(100,135):rangeGenInt(136,145);
        cl=d.checkTraits("hyperchloridemia")?rangeGenInt(106,140):d.checkTraits("hypochoridemia")?rangeGenInt(70,94):rangeGenInt(95,105);
        ca=d.checkTraits("hypercalcemia")?rangeGen(10.3,19):d.checkTraits("hypocalcemia")?rangeGen(4,8.3):rangeGen(8.4,10.2);
        if(hco3==-1)hco3=d.checkTraits("metAlkalosis")?rangeGenInt(29,40):d.checkTraits("metAcidosis")?rangeGenInt(4,22):rangeGenInt(22,28);
        mg=d.checkTraits("hypermagnesemia")?rangeGen(2.1,8):d.checkTraits("hypomagnesemia")?rangeGen(0.4,1.4):rangeGen(1.5,2);
        p=d.checkTraits("hyperphosphatemia")?rangeGen(4.6,15):d.checkTraits("hypophosphatemia")?rangeGen(1,3):rangeGen(3,4.5);
        fe=d.checkTraits("hemochromatosis")?rangeGenInt(200,250):d.checkTraits("ida","cIDA")?rangeGenInt(20,49):rangeGenInt(50,170);
        values.add(new Value("Na⁺", Na,"mEq/L",136,145));
        values.add(new Value("K⁺",k,"mEq/L",3.5f,5));
        values.add(new Value("Cl⁻",cl,"mEq/L",95,105));
        values.add(new Value("Ca²⁺",ca,"mg/dL",8.4f,10.2f));
        values.add(new Value("HCO₃⁻",hco3,"mEq/L",22,28));
        values.add(new Value("Phosphorus",p,"mg/dL",3,4.5f));
        values.add(new Value("Mg²⁺",mg,"mg/dL",1.5f,2));
        values.add(new Value("Fe²⁺",fe,"μg/dL",50,170));
        values.add(valueConstructor("Ferritin",array("hemochromatosis","cIDA"),array("ida"),"ng/ml",m?15:12,m?200:150,true));
        values.add(valueConstructor("Transferrin Saturation",array("hemochromatosis"),array("ida","cIDA"),"%",m?15:12,m?50:45,true));
        values.add(valueConstructor("TIBC",array("ida"),array("hemochromatosis","cIDA"),"μg/dl",240,450,true));
        values.add(new Value("Anion gap",(int)(Na -(cl+hco3)),"mEq/L"));
        return valuesToString(values);
    }
    public String sugarTests(){
        ArrayList<Value> values=new ArrayList<>();
        return valuesToString(values);
    }
    public String LFT(){
        //TODO
        return "";
    }
    public String KFT(){
        if(k==-1)k=d.checkTraits("hyperkalemia")?rangeGen(5.1,16):d.checkTraits("hypokalemia")?rangeGen(0.5,3.4):rangeGen(3.5,5);
        if(Na ==-1) Na =d.checkTraits("hypernatremia")?rangeGen(147,200):d.checkTraits("hyponatremia")?rangeGen(100,135):rangeGen(136,145);
        return "";
    }
    private String CSFExam(){
        ArrayList<Value>values=new ArrayList<>();
        values.add(new Value("ICP",d.checkTraits("highICP")?rangeGenInt(16,30):rangeGenInt(7,15),"mmHg",7,15));
        if(d.checkTraits("acuteBacterialMeningitis")){
            values.add(new Value("WBC", rangeGenInt(1000,10000),"neutrophils/μL",0,8));
            values.add(new Value("Protein",rangeGenInt(100,500),"mg/dL",15,45));
        }else if(d.checkTraits("tuberculousMeningitis")){
            values.add(new Value("WBC", rangeGenInt(100,600),getRandom("lymph.","mixed leukocytes")+"/μL",0,8));
            values.add(new Value("Protein",rangeGenInt(50,300),"mg/dL",15,45));
        }else if(d.checkTraits("fungalMeningitis")){
            values.add(new Value("WBC", rangeGenInt(40,800),"mixed leukocytes/μL",0,8));
            values.add(new Value("Protein",rangeGenInt(50,300),"mg/dL",15,45));
        }else if(d.checkTraits("viralMeningitis")){
            values.add(new Value("WBC", rangeGenInt(5,300),"lymph./μL",0,8));
            values.add(new Value("Protein",rangeGenInt(30,60),"mg/dL",15,45));
        }else if(d.checkTraits("acuteSyphiliticMeningitis")){
            values.add(new Value("WBC", rangeGenInt(450,550),"lymph./μL",0,8));
            values.add(new Value("Protein",rangeGenInt(46,100),"mg/dL",15,45));
        }else{
            values.add(new Value("WBC", rangeGenInt(0,8),"lymph./μL",0,8));
            values.add(new Value("Protein",rangeGenInt(15,45),"mg/dL",15,45));
        }
        values.add(valueConstructor("Glucose",array(),array("acuteBacterialMeningitis","fungalMeningitis","tuberculousMeningitis"),"mg/dL",50,80,true));
        values.add(valueConstructor("Chloride",array(),array("acuteBacterialMeningitis","fungalMeningitis","tuberculousMeningitis"),"mEq/L",115,130,true));
        String temp=valuesToString(values);
        temp+="Appearance = "+(d.checkTraits("acuteBacterialMeningitis")?"Turbid":d.checkTraits("tuberculousMeningitis")?"Cobweb formation":"Clear");
        return temp;
    }
    public View resp(){
        //TODO
        return null;
    }
    private String mriText(){
        String temp="";
        return temp;
    }
    private View mriView(){
        ImageView view=new ImageView(context);
        String name="";
        if(d instanceof PitAdenoma)name="mri_pit_adenoma"+rangeGenInt(1,6);
        else name="i"+rangeGenInt(1,7);
        view.setImageResource(context.getResources().getIdentifier(name,"drawable",context.getPackageName()));
        return view;
    }
    private String valuesToString(ArrayList<Value> vs){
        String temp="";
        for(Value v:vs)temp+=v+"\n";
        return temp;
    }
    private Value valueConstructor(String name, String[] high, String[] low, String unit, double lb, double ub, boolean i){
        float v=d.checkTraits(high)?rangeGen(ub+0.1,ub*1.7,i):d.checkTraits(low)?rangeGen(lb*0.3,lb-0.1,i):rangeGen(lb,ub,i);
        return new Value(name,v,unit,(float) lb,(float) ub);
    }


}
