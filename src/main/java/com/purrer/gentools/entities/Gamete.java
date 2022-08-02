package com.purrer.gentools.entities;

public class Gamete {
    private final String gameteKey;
    private final String gameteValue;

    public Gamete(String gameteKey, String gameteValue) {
        this.gameteKey = gameteKey;
        this.gameteValue = gameteValue;
    }

    public String getGameteKey() {
        return gameteKey;
    }

    public String getGameteValue() {
        return gameteValue;
    }
}
