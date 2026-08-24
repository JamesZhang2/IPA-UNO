package com.ipauno.model;

import static com.ipauno.model.MannerOfArticulation.*;
import static com.ipauno.model.PlaceOfArticulation.*;
import static com.ipauno.model.Voicing.*;

import java.util.ArrayList;
import java.util.List;

public final class PulmonicConsonantCatalog {

    private PulmonicConsonantCatalog() {}

    public static List<PulmonicConsonant> createDeck() {
        List<PulmonicConsonant> deck = new ArrayList<>(CHART.size() * 2);
        for (ChartEntry entry : CHART) {
            deck.add(entry.toCard(0));
            deck.add(entry.toCard(1));
        }
        return List.copyOf(deck);
    }

    private static ChartEntry entry(
            String symbol, MannerOfArticulation manner, PlaceOfArticulation place, Voicing voicing) {
        return new ChartEntry(symbol, manner, place, voicing);
    }

    private record ChartEntry(
            String symbol, MannerOfArticulation manner, PlaceOfArticulation place, Voicing voicing) {

    
        PulmonicConsonant toCard(int copyIndex) {
            return new PulmonicConsonant(symbol + "#" + copyIndex, manner, place, voicing, symbol);
        }
    }

    private static final List<ChartEntry> CHART = List.of(
            entry("p", PLOSIVE, BILABIAL, VOICELESS),
            entry("b", PLOSIVE, BILABIAL, VOICED),
            entry("m", NASAL, BILABIAL, VOICED),
            entry("ʙ", TRILL, BILABIAL, VOICED),
            entry("ɸ", FRICATIVE, BILABIAL, VOICELESS),
            entry("β", FRICATIVE, BILABIAL, VOICED),
            entry("ɱ", NASAL, LABIODENTAL, VOICED),
            entry("f", FRICATIVE, LABIODENTAL, VOICELESS),
            entry("v", FRICATIVE, LABIODENTAL, VOICED),
            entry("ⱱ", TAP_OR_FLAP, LABIODENTAL, VOICED),
            entry("ʋ", APPROXIMANT, LABIODENTAL, VOICED),
            entry("θ", FRICATIVE, DENTAL, VOICELESS),
            entry("ð", FRICATIVE, DENTAL, VOICED),
            entry("t", PLOSIVE, ALVEOLAR, VOICELESS),
            entry("d", PLOSIVE, ALVEOLAR, VOICED),
            entry("n", NASAL, ALVEOLAR, VOICED),
            entry("r", TRILL, ALVEOLAR, VOICED),
            entry("ɾ", TAP_OR_FLAP, ALVEOLAR, VOICED),
            entry("s", FRICATIVE, ALVEOLAR, VOICELESS),
            entry("z", FRICATIVE, ALVEOLAR, VOICED),
            entry("ɬ", LATERAL_FRICATIVE, ALVEOLAR, VOICELESS),
            entry("ɮ", LATERAL_FRICATIVE, ALVEOLAR, VOICED),
            entry("l", LATERAL_APPROXIMANT, ALVEOLAR, VOICED),
            entry("ʃ", FRICATIVE, POSTALVEOLAR, VOICELESS),
            entry("ʒ", FRICATIVE, POSTALVEOLAR, VOICED),
            entry("ʈ", PLOSIVE, RETROFLEX, VOICELESS),
            entry("ɖ", PLOSIVE, RETROFLEX, VOICED),
            entry("ɳ", NASAL, RETROFLEX, VOICED),
            entry("ɽ", TAP_OR_FLAP, RETROFLEX, VOICED),
            entry("ʂ", FRICATIVE, RETROFLEX, VOICELESS),
            entry("ʐ", FRICATIVE, RETROFLEX, VOICED),
            entry("ɭ", LATERAL_APPROXIMANT, RETROFLEX, VOICED),
            entry("c", PLOSIVE, PALATAL, VOICELESS),
            entry("ɟ", PLOSIVE, PALATAL, VOICED),
            entry("ɲ", NASAL, PALATAL, VOICED),
            entry("ç", FRICATIVE, PALATAL, VOICELESS),
            entry("ʝ", FRICATIVE, PALATAL, VOICED),
            entry("j", APPROXIMANT, PALATAL, VOICED),
            entry("ʎ", LATERAL_APPROXIMANT, PALATAL, VOICED),
            entry("k", PLOSIVE, VELAR, VOICELESS),
            entry("g", PLOSIVE, VELAR, VOICED),
            entry("ŋ", NASAL, VELAR, VOICED),
            entry("x", FRICATIVE, VELAR, VOICELESS),
            entry("ɣ", FRICATIVE, VELAR, VOICED),
            entry("w", APPROXIMANT, VELAR, VOICED),
            entry("ʟ", LATERAL_APPROXIMANT, VELAR, VOICED),
            entry("q", PLOSIVE, UVULAR, VOICELESS),
            entry("ɢ", PLOSIVE, UVULAR, VOICED),
            entry("ɴ", NASAL, UVULAR, VOICED),
            entry("ʀ", TRILL, UVULAR, VOICED),
            entry("χ", FRICATIVE, UVULAR, VOICELESS),
            entry("ʁ", FRICATIVE, UVULAR, VOICED),
            entry("ħ", FRICATIVE, PHARYNGEAL, VOICELESS),
            entry("ʕ", FRICATIVE, PHARYNGEAL, VOICED),
            entry("ʔ", PLOSIVE, GLOTTAL, VOICELESS),
            entry("h", FRICATIVE, GLOTTAL, VOICELESS),
            entry("ɦ", FRICATIVE, GLOTTAL, VOICED));
}
