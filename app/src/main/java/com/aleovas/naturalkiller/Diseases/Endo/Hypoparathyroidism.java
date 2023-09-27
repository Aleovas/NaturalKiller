package com.aleovas.naturalkiller.Diseases.Endo;

import com.aleovas.naturalkiller.Disease;

import static com.aleovas.naturalkiller.MainActivity.prop;

public class Hypoparathyroidism extends Disease {
    public Hypoparathyroidism(){
        super();
        ageDeviation=40;
        age=30;
        addTraits("hypocalcemia");
        if(prop(0.9))addHistory("thyroidectomy");
        name="Hypoparathyroidism";
    }
}
