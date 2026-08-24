package com.ipauno.model;

public enum Voicing {
    VOICED("voiced"),
    VOICELESS("voiceless");

    private final String label;

    Voicing(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
