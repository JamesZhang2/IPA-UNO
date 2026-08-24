package com.ipauno.model;

public enum PlaceOfArticulation {
    BILABIAL("bilabial"),
    LABIODENTAL("labiodental"),
    DENTAL("dental"),
    ALVEOLAR("alveolar"),
    POSTALVEOLAR("postalveolar"),
    RETROFLEX("retroflex"),
    PALATAL("palatal"),
    VELAR("velar"),
    UVULAR("uvular"),
    PHARYNGEAL("pharyngeal"),
    GLOTTAL("glottal");

    private final String label;

    PlaceOfArticulation(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
