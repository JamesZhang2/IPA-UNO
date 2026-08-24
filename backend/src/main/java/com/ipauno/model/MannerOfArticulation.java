package com.ipauno.model;

public enum MannerOfArticulation {
    PLOSIVE("plosive"),
    NASAL("nasal"),
    TRILL("trill"),
    TAP_OR_FLAP("tap or flap"),
    FRICATIVE("fricative"),
    LATERAL_FRICATIVE("lateral fricative"),
    APPROXIMANT("approximant"),
    LATERAL_APPROXIMANT("lateral approximant");

    private final String label;

    MannerOfArticulation(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
