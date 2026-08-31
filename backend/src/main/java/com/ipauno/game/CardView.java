package com.ipauno.game;

import com.ipauno.model.MannerOfArticulation;
import com.ipauno.model.PlaceOfArticulation;
import com.ipauno.model.PulmonicConsonant;
import com.ipauno.model.Voicing;

public record CardView(
        String id, String symbol, MannerOfArticulation manner, PlaceOfArticulation place, Voicing voicing) {

    static CardView from(PulmonicConsonant card) {
        return new CardView(card.id(), card.symbol(), card.manner(), card.place(), card.voicing());
    }
}
