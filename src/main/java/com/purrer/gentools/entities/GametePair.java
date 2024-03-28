package com.purrer.gentools.entities;

import java.util.Objects;

public class GametePair {

    private final String firstGamete;

    private final String secondGamete;

    public GametePair(String firstGamete, String secondGamete) {
        this.firstGamete = firstGamete;
        this.secondGamete = secondGamete;
    }

    public String getFirstGamete() {
        return firstGamete;
    }

    public String getSecondGamete() {
        return secondGamete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GametePair pair = (GametePair) o;
        return Objects.equals(firstGamete, pair.firstGamete) && Objects.equals(secondGamete, pair.secondGamete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstGamete, secondGamete);
    }
}
