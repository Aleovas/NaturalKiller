package com.aleovas.naturalkiller;

import com.aleovas.naturalkiller.Diseases.Endo.*;
import com.aleovas.naturalkiller.Diseases.HLS.*;
import com.aleovas.naturalkiller.Diseases.Neurology.Meningitis;
import com.aleovas.naturalkiller.Diseases.RS.Pneumonia;

import java.util.ArrayList;
import java.util.Arrays;

import static com.aleovas.naturalkiller.Disease.InfectionType.*;
import static com.aleovas.naturalkiller.MainActivity.prop;
import static com.aleovas.naturalkiller.MainActivity.rangeGenInt;
import static com.aleovas.naturalkiller.MainActivity.rnd;

public class DiseaseContainer {
    public final static ArrayList<Container> diseases=new ArrayList<>();
    static {
        diseases.add(new Container(Typhoid::new,"Hemolymphatic"));
        diseases.add(new Container(()->new Anemia(Anemia.AnemiaType.IDA),0.25f,"Hemolymphatic"));
        diseases.add(new Container(()->new Anemia(Anemia.AnemiaType.Megaloblastic),0.25f,"Hemolymphatic"));
//        diseases.add(new Container(()->new Anemia(Anemia.AnemiaType.Sickle),0.25f,"Hemolymphatic"));
        diseases.add(new Container(()->new Anemia(Anemia.AnemiaType.Hypovolemic), 0.25f, "Hemolymphatic"));
        diseases.add(new Container(Hyperparathyroidism::new,"Endocrine"));
        diseases.add(new Container(Hyperthyroidism::new,"Endocrine"));
        diseases.add(new Container(Hypoparathyroidism::new,"Endocrine"));
        diseases.add(new Container(Hypothyroidism::new,"Endocrine"));
        diseases.add(new Container(PitAdenoma::new,"Endocrine"));
        diseases.add(new Container(Meningitis::new,"Nervous"));
        diseases.add(new Container(()->new Pneumonia(Bacterial)));

//        diseases.add(new Container(()->new PitAdenoma(PitAdenoma.AdenomaType.Prolactinoma)));
//        diseases.add(new Container(()->new Disease()));

    }
    private static class Container{
        DiseaseGenerator generator;
        ArrayList<String> tags=new ArrayList<>();
        float prop=1;
        public Container(DiseaseGenerator d, String... s){
            generator=d;
            tags.addAll(Arrays.asList(s));
        }
        public Container(DiseaseGenerator d, float prop, String... s){
            generator=d;
            this.prop=prop;
            tags.addAll(Arrays.asList(s));
        }
    }
    interface DiseaseGenerator{
        Disease getDisease();
    }
    public static ArrayList<String> getDiseases(){
        ArrayList<String> list=new ArrayList<>();
        for(Container c:diseases)list.add(c.generator.getDisease().name);
        return list;
    }
    private static ArrayList<Container> getFromTag(String... tags){
        ArrayList<Container> list=new ArrayList<>();
        for(Container c:diseases)for (String s:tags)if(!list.contains(c)&&c.tags.contains(s))
            list.add(c);
        return list;
    }
    public static Disease getRandomDisease(){
        Container c=diseases.get(rnd.nextInt(diseases.size()));
        while(!prop(c.prop)){
            c=diseases.get(rnd.nextInt(diseases.size()));
        }
        return c.generator.getDisease().reset();
    }
    public static Disease getRandomDisease(String... tags){
        ArrayList<Container> temp=getFromTag();
        return temp.get(rnd.nextInt(temp.size())).generator.getDisease().reset();
    }
}
