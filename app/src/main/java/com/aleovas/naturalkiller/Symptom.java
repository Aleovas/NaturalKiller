package com.aleovas.naturalkiller;

import java.util.ArrayList;
import java.util.Random;

import static com.aleovas.naturalkiller.MainActivity.capitalizeFirstLetter;
import static com.aleovas.naturalkiller.MainActivity.formatStringArraylist;
import static com.aleovas.naturalkiller.MainActivity.has;

/**
 * Created by omar on 7/31/18.
 */

public class Symptom {
    public ArrayList<String> haveStrings=new ArrayList<>();
    public ArrayList<String> beStrings=new ArrayList<>();
    Random rnd=new Random();
    boolean inquirable=false;
    String[] queries;
    ArrayList<String> notes=new ArrayList<>();
    String chosenTerm="";
    int type=-1;
    static int idNo=0;
    int id;
    boolean required=false;
    public Symptom(ArrayList<String> haveStrings, ArrayList<String> beStrings){
        this.beStrings=beStrings;
        this.haveStrings=haveStrings;
        id=idNo++;
    }
    public Symptom(String... strings){
        for(String s:strings)haveStrings.add(s);
        id=idNo++;
    }
    public Symptom(boolean be,String... strings){
        for(String s:strings)beStrings.add(s);
        id=idNo++;
    }
    public Symptom(String[] have, String[] be){
        for(String s:have)haveStrings.add(s);
        for(String s:be)beStrings.add(s);
        id=idNo++;
    }
    String getRandomString(){
        ArrayList<String> temp=new ArrayList<>();
        for(String s:haveStrings)temp.add(randomHaveString()+s);
        if(!has)for(String s:beStrings)temp.add(randomBeString()+s);
        return temp.get(rnd.nextInt(temp.size()));
    }
    String getRandomStringFragment(){
        ArrayList<String> temp=new ArrayList<>();
        for(String s:haveStrings)temp.add(s);
        if(!has)for(String s:beStrings)temp.add("being "+s);
        chosenTerm=temp.get(rnd.nextInt(temp.size()));
        return chosenTerm;
    }
    String randomHaveString(){
        String[] temp={"seems to have ", "is suffering from ", "has "};
        return temp[rnd.nextInt(temp.length)];
    }
    String randomBeString(){
        String[] temp={"is "};
        return temp[rnd.nextInt(temp.length)];
    }
    public void addQueries(String... strings){
        queries=strings;
    }
    public String getQuery(){
        ArrayList<String> strings=new ArrayList<>();
        if(type!=-1)strings.add(queries[type]);
        strings.addAll(notes);
        if(strings.size()==0)return "No further information can be given.";
        return formatStringArraylist(strings);
    }
    public Symptom type(int i){
        Symptom s=copy();
        s.type=i;
        s.inquirable=true;
        return s;
    }
    public Symptom required(){
        Symptom s=copy();
        s.required=true;
        return s;
    }
    public Symptom addNotes(String... ts){
        Symptom s=copy();
        for (String x:ts)if(x!=null)s.notes.add(x);
        inquirable=true;
        return s;
    }
    Symptom copy(){
        Symptom s=new Symptom(haveStrings,beStrings);
        s.id=id;
        idNo--;
        s.queries=queries;
        s.inquirable=inquirable;
        s.type=type;
        s.required=required;
        s.notes=notes;
        return s;
    }

    public String getChosenTerm() {
        return capitalizeFirstLetter(chosenTerm);
    }

    boolean match(Symptom s){
        return id==s.id;
    }
}
