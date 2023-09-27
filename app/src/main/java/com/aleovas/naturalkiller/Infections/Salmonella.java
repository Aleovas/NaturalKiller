package com.aleovas.naturalkiller.Infections;


import com.aleovas.naturalkiller.Diseases.HLS.Typhoid;
import com.aleovas.naturalkiller.Infection;

/**
 * Created by omar on 7/31/18.
 */

public class Salmonella extends Infection {
    public Salmonella(){
        type=microbe.Bacteria;
        diseases.add(new Typhoid());
        name="Salmonella typhi";
        incubationLBound=24;
        incubationUBound=48;
    }
}
