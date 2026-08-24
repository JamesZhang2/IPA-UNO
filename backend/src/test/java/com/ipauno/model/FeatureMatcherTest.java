package com.ipauno.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FeatureMatcherTest {

    private static final PulmonicConsonant B = card("b#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.BILABIAL,
            Voicing.VOICED, "b");
    private static final PulmonicConsonant B_COPY = card("b#1", MannerOfArticulation.PLOSIVE,
            PlaceOfArticulation.BILABIAL, Voicing.VOICED, "b");
    private static final PulmonicConsonant T = card("t#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.ALVEOLAR,
            Voicing.VOICELESS, "t");
    private static final PulmonicConsonant D = card("d#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.ALVEOLAR,
            Voicing.VOICED, "d");
    private static final PulmonicConsonant P = card("p#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.BILABIAL,
            Voicing.VOICELESS, "p");
    private static final PulmonicConsonant F = card("f#0", MannerOfArticulation.FRICATIVE,
            PlaceOfArticulation.LABIODENTAL, Voicing.VOICELESS, "f");
    private static final PulmonicConsonant M = card("m#0", MannerOfArticulation.NASAL, PlaceOfArticulation.BILABIAL,
            Voicing.VOICED, "m");

    @Test
    void allThreeFeaturesMatchingIsLegal() {
        assertTrue(FeatureMatcher.isLegal(B, B_COPY));
    }

    @Test
    void twoOfThreeMatchingWithVoicingDifferenceIsLegal() {
        assertTrue(FeatureMatcher.isLegal(D, T));
        assertTrue(FeatureMatcher.isLegal(P, B));
    }

    @Test
    void twoOfThreeMatchingWithPlaceDifferenceIsLegal() {
        assertTrue(FeatureMatcher.isLegal(D, B));
    }

    @Test
    void twoOfThreeMatchingWithMannerDifferenceIsLegal() {
        assertTrue(FeatureMatcher.isLegal(M, B));
    }

    @Test
    void oneOfThreeFeaturesMatchingIsIllegal() {
        assertFalse(FeatureMatcher.isLegal(B, T));
    }

    @Test
    void zeroOfThreeFeaturesMatchingIsIllegal() {
        assertFalse(FeatureMatcher.isLegal(B, F));
    }

    @Test
    void describeFormatsCardFeaturesInPlainEnglish() {
        assertEquals("b is a voiced bilabial plosive", FeatureMatcher.describe(B));
        assertEquals("t is a voiceless alveolar plosive", FeatureMatcher.describe(T));
    }

    @Test
    void explainIllegalPlayDescribesBothCardsAndWhichFeaturesMatched() {
        String explanation = FeatureMatcher.explainIllegalPlay(B, T);

        assertEquals(
                "b is a voiced bilabial plosive while t is a voiceless alveolar plosive. "
                        + "Only manner matches; you need at least 2 of 3.",
                explanation);
    }

    @Test
    void explainIllegalPlayReportsWhenNoFeaturesMatch() {
        String explanation = FeatureMatcher.explainIllegalPlay(B, F);

        assertEquals(
                "b is a voiced bilabial plosive while f is a voiceless labiodental fricative. "
                        + "No features match; you need at least 2 of 3.",
                explanation);
    }

    private static PulmonicConsonant card(
            String id,
            MannerOfArticulation manner,
            PlaceOfArticulation place,
            Voicing voicing,
            String symbol) {
        return new PulmonicConsonant(id, manner, place, voicing, symbol);
    }
}
