package com.aleovas.naturalkiller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.Random;

import static com.aleovas.naturalkiller.DiseaseContainer.getRandomDisease;
import static com.aleovas.naturalkiller.MainActivity.Demographic.parse;

public class MainActivity extends AppCompatActivity {
    public enum Gender {Male, Female, Nonbinary}
    public static Gender gender=Gender.Female;
    public static Random rnd=new Random();
    static int age=45;
    FloatingActionButton fabs[]=new FloatingActionButton[4];
    boolean menuOpen=false;
    boolean caseGenerated=false;
    boolean newCase=false;
    Tests tests;
    Disease d;
    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private ArrayList<contentContainer> containers;
//    public static Context context;
    public static boolean easyMode=true;
    public static int easyinessModifier=2;
    public static float transModifier=10f;
    public static boolean has=false;
    public static boolean needsHistory=true;
    public static boolean normalDist=true;
    boolean needsTests;
    Menu menu;
    static SharedPreferences prefs;
    public static String[] frequency={"regularly", "frequently"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFabs();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        containers = new ArrayList<>();
        adapter = new CardAdapter(this, containers);
        recyclerView.setAdapter(adapter);
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    if(!containers.get(position).title.equals("Case")){
                                        containers.remove(position);
                                        adapter.notifyItemRemoved(position);
                                    }else Snackbar.make(recyclerView,"You can't dismiss the case",Snackbar.LENGTH_SHORT).show();
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    if(!containers.get(position).title.equals("Case")){
                                        containers.remove(position);
                                        adapter.notifyItemRemoved(position);
                                    }else Snackbar.make(recyclerView,"You can't dismiss the case",Snackbar.LENGTH_SHORT).show();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        recyclerView.addOnItemTouchListener(swipeTouchListener);
        //        int resId = R.anim.layout_animation_fall_down;
        //        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        //        recyclerView.setLayoutAnimation(animation);
//        context=this;
        SymptomContainer.init();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void initFabs() {
        fabs[0]=findViewById(R.id.historyButton);
        fabs[0].setImageBitmap(textAsBitmap("H",32, Color.WHITE));
        fabs[1]=findViewById(R.id.physicalButton);
        fabs[1].setImageBitmap(textAsBitmap("P",32, Color.WHITE));
        fabs[2]=findViewById(R.id.labButton);
        fabs[2].setImageBitmap(textAsBitmap("L",32, Color.WHITE));
        fabs[3]=findViewById(R.id.imageButton);
        fabs[3].setImageBitmap(textAsBitmap("I",32, Color.WHITE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu=menu;
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_case:
                newCase(null);
                return true;
            case R.id.answer:
                getAnswer();
                return true;
            case R.id.settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getAnswer(){
        String s=d.name;
        if(d.comorbidities.size()>0) {
            s += "\nComorbidities: ";
            for(Disease c:d.comorbidities) s+=c.name+", ";
            s=stripEnd(s,", ");
        }
        Snackbar snackbar= Snackbar.make(fabs[0],s,Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(3);
        snackbar.show();
    }
    public void openSettings(){
        startActivity(new Intent(this,SettingsActivity.class));
    }
    public void newCase(View view){
        containers.clear();
        getSettings();
        adapter.notifyDataSetChanged();
        d=getRandomDisease();
        Demographic.generate(d);
        has=false;
        needsHistory=true;
        MenuItem answer=menu.findItem(R.id.answer);
        answer.setVisible(true);
        String temp=getRandom("A %a-year-old %g "+
                getRandom(getRandom("comes to you ", "comes to the clinic ", "admitted to the clinic is ")+getRandom("complaining of ","suffering from "),
                        getRandom("is suffering from ", has(), "is complaining of ")),
                "You are visited by a %a-year-old %g "+getRandom("suffering from ", "complaining of ", "who "+getRandom("suffers from ",has(), "complains of ")));
        //TODO Check if random symptom number is correct for this case
        int symNo = Math.min((int) Math.round(d.getSymptomNo()*rnd.nextFloat()+0.49),6);
        //int symNo = (int) Math.round(d.symptoms.length*0.6);
        if(symNo==0)temp+="no symptoms";
        if(symNo==1)temp+=d.getSymptomFragment();
        else if(symNo==2)temp+=d.getSymptomFragment()+" and "+d.getSymptomFragment();
        else if(symNo>=3){
            if(prop(0.4)){
                for(int i=1;i<symNo;i++)temp+=d.getSymptomFragment()+", ";
                temp+="and "+d.getSymptomFragment();
            }else {
                temp+=d.getSymptomFragment()+" and "+d.getSymptomFragment()+". ";
                temp+="%gsc also "+getRandom("has", "complains of", "suffers from")+" ";
                symNo-=2;
                if(symNo==1)temp+=d.getSymptomFragment();
                else if(symNo==2)temp+=d.getSymptomFragment()+" and "+d.getSymptomFragment();
                else if(symNo>=3){
                    for(int i=1;i<symNo;i++)temp+=d.getSymptomFragment()+", ";
                    temp+="and "+d.getSymptomFragment();
                }
            }
        }
        temp+=". ";
        if(gender==Gender.Nonbinary){
            temp=temp.replaceAll(" is "," are ");
            temp=temp.replaceAll(" was "," were ");
            temp=temp.replaceAll(" has "," have ");
        }
        newCase=true;
        addText("Case",temp);
        caseGenerated=true;
        //tests=new Tests(d);
        needsTests=true;
    }

    public static void getSettings() {
        easyMode=prefs.getBoolean("value_indicator",true);
        easyinessModifier=Integer.parseInt(prefs.getString("example_list","2"));
    }

    private void openMenu(){
        for(int i=0;i<4;i++)fabs[i].animate().translationY(dipToPixels(this,-i*64-64));
        menuOpen=true;
    }
    private void closeMenu(){
        for(int i=0;i<4;i++) fabs[i].animate().translationY(0);
        menuOpen=false;
    }
    public void fab(View view){
        if(!caseGenerated){
            newCase(view);
        }else {
            if(menuOpen)closeMenu();else openMenu();
        }
    }
    public void history(View view){
        if(needsTests){
            tests=new Tests(d, this);
            needsTests=false;
        }
        addText("History",tests.history.getText());
    }
    public void labDiag(View view){
        if(needsTests){
            tests=new Tests(d, this);
            needsTests=false;
        }
        testDialog(tests.labs,"Lab Tests",view);
    }
    public void testDialog(ArrayList<Tests.Test> t,String title, View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(tests.getNames(t), (dialog, which) -> {
            Tests.Test test= t.get(which);
            if(test.isTestGenerator){
                testDialog(test.testGeneratorGenerator.getTests(),test.name,v);
            }else if(testPerformed(test.name)){
                Snackbar.make(v,"You already performed this test",Snackbar.LENGTH_SHORT).show();
            }else addTestResult(test);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void physExam(View view){
        if(needsTests){
            tests=new Tests(d, this);
            needsTests=false;
        }
        testDialog(tests.exams,"Physical Exams",view);
    }
    public void imaging(View view){
        if(needsTests){
            tests=new Tests(d, this);
            needsTests=false;
        }
        testDialog(tests.imagingTechs,"Imaging Techniques",view);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_TAB:
                newCase(fabs[0]);
                return true;
                default:return super.onKeyUp(keyCode,event);
        }
    }

    static class Demographic{
        static void generate(Disease d){
            gender=rnd.nextFloat()<=d.malePreference?Gender.Male:Gender.Female;
            if(normalDist){
                float mean=d.bimodalDistribution&&rnd.nextBoolean()?d.age2:d.age;
                float std=d.bimodalDistribution&&rnd.nextBoolean()?d.ageDeviation2:d.ageDeviation;
                age= (int) (rnd.nextGaussian()*(std/4)+mean);
            }else {
                if (d.bimodalDistribution && rnd.nextBoolean()) {
                    age = (int) (d.age2 + (d.ageDeviation2 * rnd.nextFloat() - d.ageDeviation2 / 2));
                } else
                    age = (int) (d.getAge() + (d.ageDeviation * rnd.nextFloat() - d.ageDeviation / 2));
            }
            if(age<=0){
                //as a work around, age is set to negative for months
                age=-rnd.nextInt(12);
            }
            if(age>=5)tStuff(d);
            if(!d.init)d.init();
        }
        private static void tStuff(Disease d){
            if(prop(0.005*transModifier)){
                if(prop(0.95)){
                    if(gender==Gender.Female)d.addHistory("AFAB");else d.addHistory("AMAB");
                }
                gender=Gender.Nonbinary;
                d.addTraits("transgender");
            }
            if((prop(0.02*transModifier))&&gender!=Gender.Nonbinary){
                if(gender==Gender.Female){
                    d.addHistory("AFAB");
                    gender=Gender.Male;
                }else{
                    d.addHistory("AMAB");
                    gender=Gender.Female;
                }
                d.addTraits("transgender");
            }
        }
        static String getSubjectPronoun(){
            switch (gender){
                case Male: return "he";
                case Female: return "she";
                default:return "they";
            }
        }
        static String getSubjectPronounCapital(){
            switch (gender){
                case Male: return "He";
                case Female: return "She";
                default:return "They";
            }
        }
        static String getObjectPronoun(){
            switch (gender){
                case Male: return "him";
                case Female: return "her";
                default:return "them";
            }
        }
        static String getPossesivePronoun(){
            switch (gender){
                case Male: return "his";
                case Female: return "her";
                default:return "their";
            }
        }
        static String getGenderWord(){
            if(age<18){
                if(prop(0.1))return age<=3?"infant":"child";
                switch (gender){
                    case Male: return "boy";
                    case Female: return "girl";
                    default:return age<=3?"infant":"child";
                }
            }else {
                switch (gender){
                    case Male: return getRandom("man","male");
                    case Female: return getRandom("woman","female","lady");
                    default:return getRandom("patient");
                }
            }
        }
        static String parse(String s){
            s=s.replaceAll("%gsc", getSubjectPronounCapital());
            s=s.replaceAll("%gs", getSubjectPronoun());
            s=s.replaceAll("%go", getObjectPronoun());
            s=s.replaceAll("%gp", getPossesivePronoun());
            s=s.replaceAll("%g", getGenderWord());
            if(age<0)s=s.replaceAll("%a-year",-age+"-month");
            s=s.replaceAll("%a", age+"");
            s=s.replaceAll("hey was", "hey were");
            s=s.replaceAll("hey is", "hey are");
            s=s.replaceAll("hey has", "hey have");
            s=s.replaceAll("hey suffers", "hey suffer");
            s=s.replaceAll("hey seems", "hey seem");
            s=s.replaceAll("has being", "has been");
            s=s.replaceAll("also is", "is also");
            s=s.replaceAll("also are", "are also");
            return stripEnd(s,"\n");
        }
    }
    public void addText(String title, String text){
        getSettings();
        containers.add(new contentContainer(title,parse(text)));
        adapter.notifyDataSetChanged();
    }
    public void addView(String title, String text, View view){
        getSettings();
        containers.add(new contentContainer(title, parse(text),view));
        adapter.notifyDataSetChanged();
    }
    public void addTestResult(Tests.Test t){
        if(t.viewGenerator==null){
            addText(t.name,t.getText());
        }else {
            addView(t.name,t.getText(),t.getView());
        }
    }
    public float dipToPixels(Context context, float dipValue){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  dipValue, metrics);
    }
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
    public static float rangeGen(double i1, double i2){
        return (float) (rnd.nextFloat()*((i2-i1))+i1);
    }
    public static float rangeGen(double i1, double i2, boolean i){
        if(i)return (float) rangeGenInt((int)i1,(int)i2);else return rangeGen(i1,i2);
    }
    public static int rangeGenInt(int i1, int i2){
        return (rnd.nextInt(i2+1-i1)+i1);
    }
    public static String intCheck(double d){
        if(((int) d)==d)return ((int)d)+"";else return d+"";
    }
    public static double round(double d, int i){
        double temp=d*Math.pow(10,i);
        temp=Math.round(temp);
        temp=temp/Math.pow(10,i);
        return temp;
    }
    public static String getRandom(String... strings){
        if(strings.length>0)return strings[rnd.nextInt(strings.length)];else return "";
    }
    public static String getRandom(ArrayList<String> strings){
        if(strings.size()>0)return strings.get(rnd.nextInt(strings.size()));else return "";
    }
    public static String getFuzzyTime(int i){
        if(i<=20)return i+" hours ago";
        if(i<=36)return "yesterday";
        if(i<=48)return "2 days ago";
        return (int)Math.ceil((float)i/24)+" days ago";
    }
    public static String stripEnd(final String str, final String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (stripChars == null) {
            while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.isEmpty()) {
            return str;
        } else {
            while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                end--;
            }
        }
        return str.substring(0, end);
    }
    public boolean testPerformed(String s){
        for(contentContainer c: containers)if(c.title.equals(s))return true;
        return false;
    }
    public class contentContainer{
        public String title, content;
        public View view;
        boolean containsView=false;
        public contentContainer(String t,String c){
            title=t;content=c;
        }
        public contentContainer(String t, String c, View v){
            title=t;content=c;view=v;
            containsView=true;
        }
    }
    public class CardAdapter extends RecyclerView.Adapter<Holder>{
        private Context context;
        private ArrayList<contentContainer> cards;
        public CardAdapter(Context context, ArrayList<contentContainer> cards) {
                this.context = context;
                this.cards = cards;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_row,parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            contentContainer card= cards.get(position);
            holder.setDetails(card);
        }

        @Override
        public int getItemCount() {
            return cards.size();
        }
    }
    public class Holder extends RecyclerView.ViewHolder{
        private TextView txtName, txtText;
        private LinearLayout layout;
        public Holder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtText = itemView.findViewById(R.id.txtText);
            layout = itemView.findViewById(R.id.txtView);
        }
        public void setDetails(contentContainer container) {
            layout.removeAllViews();
            txtName.setText(container.title);
            txtText.setText(container.content);
            if(container.containsView){
                layout.addView(container.view);
            }
        }
    }
    public static boolean prop(double d){
        return rnd.nextFloat()<=d;
    }
    public static String[] array(String... s){
        return s;
    }
    public static String[] array(ArrayList<String> s){
        return s.toArray(new String[s.size()]);
    }
    public static String header(){
        return getRandom("The patient ","%gsc ");
    }
    public static ArrayList<String> getStringList(ArrayList list){
        ArrayList<String> temp=new ArrayList<>();
        for(Object o:list)temp.add(o.toString());
        return temp;
    }
    public static String formatStringArraylist(ArrayList<String> list){
        String temp="";
        for(String o:list)temp+="• "+o+"\n";
        return temp;
    }
    private String has(){
        return "has ";
    }
    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }


}
