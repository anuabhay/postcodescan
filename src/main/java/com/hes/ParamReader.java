package com.hes;
import java.util.Arrays;

import java.util.ArrayList;

class Params {
    ArrayList locations;
    ArrayList types;

    Params(ArrayList locations, ArrayList types) {
        this.locations = locations;
        this.types = types;
    }
}
public class ParamReader {
    public static Params loadConfig(String locations, String types){
        String[] loc_arr = locations.split(",");
        ArrayList suburbs = new ArrayList( Arrays.asList(loc_arr));

        String[] type_arr = types.split(",");
        ArrayList bussiness_types = new ArrayList( Arrays.asList(type_arr));
        Params config = new Params(suburbs,bussiness_types);
        return config;
    }
}

