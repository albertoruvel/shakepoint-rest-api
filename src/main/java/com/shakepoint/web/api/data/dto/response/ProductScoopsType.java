package com.shakepoint.web.api.data.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ProductScoopsType {
    public static final List<String> PROTEIN_SCOOPS;
    public static final List<String> AMINOACID_SCOOPS;
    public static final List<String> OXIDE_SCOOPS;

    static {
        PROTEIN_SCOOPS = new ArrayList<String>();
        PROTEIN_SCOOPS.add("1");
        PROTEIN_SCOOPS.add("2");

        AMINOACID_SCOOPS = new ArrayList<String>();
        AMINOACID_SCOOPS.add("1");
        AMINOACID_SCOOPS.add("2");

        OXIDE_SCOOPS = new ArrayList<>();
        OXIDE_SCOOPS.add("1/2");
        OXIDE_SCOOPS.add("1");
        OXIDE_SCOOPS.add("1 1/2");
        OXIDE_SCOOPS.add("2");
    }

}
