package com.ipauno.model;

public record PulmonicConsonant(
        String id,
        MannerOfArticulation manner,
        PlaceOfArticulation place,
        Voicing voicing,
        String symbol)
        implements Card {}
