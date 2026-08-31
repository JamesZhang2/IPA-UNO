package com.ipauno.game;

import com.ipauno.model.MannerOfArticulation;
import com.ipauno.model.PlaceOfArticulation;
import com.ipauno.model.PulmonicConsonant;
import com.ipauno.model.Voicing;
import java.util.List;

final class GameTestFixture {

    public static final PulmonicConsonant B = card(
            "b#0",
            MannerOfArticulation.PLOSIVE,
            PlaceOfArticulation.BILABIAL,
            Voicing.VOICED,
            "b");
    public static final PulmonicConsonant T = card(
            "t#0",
            MannerOfArticulation.PLOSIVE,
            PlaceOfArticulation.ALVEOLAR,
            Voicing.VOICELESS,
            "t");
    public static final PulmonicConsonant D = card(
            "d#0",
            MannerOfArticulation.PLOSIVE,
            PlaceOfArticulation.ALVEOLAR,
            Voicing.VOICED,
            "d");
    public static final PulmonicConsonant P = card(
            "p#0",
            MannerOfArticulation.PLOSIVE,
            PlaceOfArticulation.BILABIAL,
            Voicing.VOICELESS,
            "p");
    public static final PulmonicConsonant K = card(
            "k#0",
            MannerOfArticulation.PLOSIVE,
            PlaceOfArticulation.VELAR,
            Voicing.VOICELESS,
            "k");
    public static final PulmonicConsonant G = card(
            "g#0",
            MannerOfArticulation.PLOSIVE,
            PlaceOfArticulation.VELAR,
            Voicing.VOICED,
            "g");
    public static final PulmonicConsonant S = card(
            "s#0",
            MannerOfArticulation.FRICATIVE,
            PlaceOfArticulation.ALVEOLAR,
            Voicing.VOICELESS,
            "s");
    public static final PulmonicConsonant Z = card(
            "z#0",
            MannerOfArticulation.FRICATIVE,
            PlaceOfArticulation.ALVEOLAR,
            Voicing.VOICED,
            "z");

    private GameTestFixture() {
    }

    public static PulmonicConsonant card(
            String id,
            MannerOfArticulation manner,
            PlaceOfArticulation place,
            Voicing voicing,
            String symbol) {
        return new PulmonicConsonant(id, manner, place, voicing, symbol);
    }

    public static List<String> handIds(Game game) {
        return game.hand().stream().map(PulmonicConsonant::id).toList();
    }

    public static List<String> handIds(GameView view) {
        return view.hand().stream().map(CardView::id).toList();
    }
}
