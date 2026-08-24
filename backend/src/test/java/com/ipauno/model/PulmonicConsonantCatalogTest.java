package com.ipauno.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class PulmonicConsonantCatalogTest {

    private static final int EXPECTED_SYMBOL_COUNT = 57;

    @Test
    void createDeckReturnsTwoCopiesOfEachSymbol() {
        List<PulmonicConsonant> deck = PulmonicConsonantCatalog.createDeck();
        Map<String, Long> counts = deck.stream()
                .collect(Collectors.groupingBy(PulmonicConsonant::symbol, Collectors.counting()));

        assertEquals(EXPECTED_SYMBOL_COUNT, counts.size());
        assertEquals(EXPECTED_SYMBOL_COUNT * 2, deck.size());
        counts.forEach((symbol, count) -> assertEquals(2L, count, symbol + " should appear twice"));
    }

    @Test
    void deckIdsAreUniqueAndFollowCopyIndexConvention() {
        List<PulmonicConsonant> deck = PulmonicConsonantCatalog.createDeck();

        Set<String> ids = deck.stream().map(PulmonicConsonant::id).collect(Collectors.toSet());
        assertEquals(deck.size(), ids.size());

        deck.stream()
                .collect(Collectors.groupingBy(PulmonicConsonant::symbol))
                .forEach((symbol, copies) -> {
                    Set<String> copyIds =
                            copies.stream().map(PulmonicConsonant::id).collect(Collectors.toSet());
                    assertEquals(Set.of(symbol + "#0", symbol + "#1"), copyIds);
                });
    }

    @Test
    void bothCopiesOfASymbolShareTheSameFeatures() {
        PulmonicConsonantCatalog.createDeck().stream()
                .collect(Collectors.groupingBy(PulmonicConsonant::symbol))
                .forEach((symbol, copies) -> {
                    PulmonicConsonant first = copies.getFirst();
                    PulmonicConsonant second = copies.getLast();
                    assertEquals(first.manner(), second.manner(), symbol);
                    assertEquals(first.place(), second.place(), symbol);
                    assertEquals(first.voicing(), second.voicing(), symbol);
                });
    }

    @Test
    void ambiguousChartPlacesUseSingleAssignedColumn() {
        assertFeatures(
                "θ",
                MannerOfArticulation.FRICATIVE,
                PlaceOfArticulation.DENTAL,
                Voicing.VOICELESS);
        assertFeatures(
                "ʃ",
                MannerOfArticulation.FRICATIVE,
                PlaceOfArticulation.POSTALVEOLAR,
                Voicing.VOICELESS);
        assertFeatures("t", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.ALVEOLAR, Voicing.VOICELESS);
        assertFeatures("n", MannerOfArticulation.NASAL, PlaceOfArticulation.ALVEOLAR, Voicing.VOICED);
        assertFeatures("h", MannerOfArticulation.FRICATIVE, PlaceOfArticulation.GLOTTAL, Voicing.VOICELESS);
        assertFeatures("ʔ", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.GLOTTAL, Voicing.VOICELESS);
    }

    private static void assertFeatures(
            String symbol,
            MannerOfArticulation manner,
            PlaceOfArticulation place,
            Voicing voicing) {
        PulmonicConsonant card = PulmonicConsonantCatalog.createDeck().stream()
                .filter(c -> c.symbol().equals(symbol))
                .findFirst()
                .orElse(null);

        assertNotNull(card, symbol + " should be in the catalog");
        assertEquals(manner, card.manner(), symbol + " manner");
        assertEquals(place, card.place(), symbol + " place");
        assertEquals(voicing, card.voicing(), symbol + " voicing");
    }
}
