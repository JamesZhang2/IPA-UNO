package com.ipauno.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class PulmonicConsonantTest {

    @Test
    void mannerEnumCoversAllPulmonicChartManners() {
        Set<String> expected = Set.of(
                "PLOSIVE",
                "NASAL",
                "TRILL",
                "TAP_OR_FLAP",
                "FRICATIVE",
                "LATERAL_FRICATIVE",
                "APPROXIMANT",
                "LATERAL_APPROXIMANT");

        Set<String> actual = Arrays.stream(MannerOfArticulation.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

    @Test
    void placeEnumCoversAllPulmonicChartPlaces() {
        Set<String> expected = Set.of(
                "BILABIAL",
                "LABIODENTAL",
                "DENTAL",
                "ALVEOLAR",
                "POSTALVEOLAR",
                "RETROFLEX",
                "PALATAL",
                "VELAR",
                "UVULAR",
                "PHARYNGEAL",
                "GLOTTAL");

        Set<String> actual = Arrays.stream(PlaceOfArticulation.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

    @Test
    void voicingEnumCoversVoicedAndVoiceless() {
        Set<String> expected = Set.of("VOICED", "VOICELESS");

        Set<String> actual =
                Arrays.stream(Voicing.values()).map(Enum::name).collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

    @Test
    void recordEqualityUsesAllComponents() {
        PulmonicConsonant first = new PulmonicConsonant(
                "p#0",
                MannerOfArticulation.PLOSIVE,
                PlaceOfArticulation.BILABIAL,
                Voicing.VOICED,
                "p");
        PulmonicConsonant same = new PulmonicConsonant(
                "p#0",
                MannerOfArticulation.PLOSIVE,
                PlaceOfArticulation.BILABIAL,
                Voicing.VOICED,
                "p");
        PulmonicConsonant differentManner = new PulmonicConsonant(
                "p#0",
                MannerOfArticulation.NASAL,
                PlaceOfArticulation.BILABIAL,
                Voicing.VOICED,
                "p");

        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        assertNotEquals(first, differentManner);
    }

    @Test
    void twoCopiesOfSameSymbolAreDistinctCards() {
        PulmonicConsonant copyZero = new PulmonicConsonant(
                "p#0",
                MannerOfArticulation.PLOSIVE,
                PlaceOfArticulation.BILABIAL,
                Voicing.VOICED,
                "p");
        PulmonicConsonant copyOne = new PulmonicConsonant(
                "p#1",
                MannerOfArticulation.PLOSIVE,
                PlaceOfArticulation.BILABIAL,
                Voicing.VOICED,
                "p");

        assertEquals("p", copyZero.symbol());
        assertEquals("p", copyOne.symbol());
        assertNotEquals(copyZero, copyOne);
        assertNotEquals(copyZero.id(), copyOne.id());
    }

    @Test
    void pulmonicConsonantIsACard() {
        Card card = new PulmonicConsonant(
                "t#0",
                MannerOfArticulation.PLOSIVE,
                PlaceOfArticulation.ALVEOLAR,
                Voicing.VOICELESS,
                "t");

        assertEquals("t#0", card.id());
    }
}
