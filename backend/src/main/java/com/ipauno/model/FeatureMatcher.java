package com.ipauno.model;

import java.util.ArrayList;
import java.util.List;

public final class FeatureMatcher {

    private FeatureMatcher() {
    }

    // TODO: a wildcard on the discard pile should short-circuit matching before this logic runs.

    public static boolean isLegal(PulmonicConsonant played, PulmonicConsonant target) {
        return matchingFeatureCount(played, target) >= 2;
    }

    public static String describe(PulmonicConsonant card) {
        return card.symbol() + " is a " + featurePhrase(card);
    }

    public static String explainIllegalPlay(PulmonicConsonant played, PulmonicConsonant target) {
        return describe(played) + " while " + describe(target) + ". "
                + matchSummary(played, target);
    }

    private static String featurePhrase(PulmonicConsonant card) {
        return card.voicing().label() + " " + card.place().label() + " " + card.manner().label();
    }

    private static String matchSummary(PulmonicConsonant played, PulmonicConsonant target) {
        List<String> matched = new ArrayList<>(3);
        if (played.manner() == target.manner()) {
            matched.add("manner");
        }
        if (played.place() == target.place()) {
            matched.add("place");
        }
        if (played.voicing() == target.voicing()) {
            matched.add("voicing");
        }

        return switch (matched.size()) {
            case 0 -> "No features match; you need at least 2 of 3.";
            case 1 -> "Only " + matched.getFirst() + " matches; you need at least 2 of 3.";
            default ->
                "Only " + String.join(" and ", matched) + " match; you need at least 2 of 3.";
        };
    }

    private static int matchingFeatureCount(PulmonicConsonant played, PulmonicConsonant target) {
        int count = 0;
        if (played.manner() == target.manner()) {
            count++;
        }
        if (played.place() == target.place()) {
            count++;
        }
        if (played.voicing() == target.voicing()) {
            count++;
        }
        return count;
    }
}
