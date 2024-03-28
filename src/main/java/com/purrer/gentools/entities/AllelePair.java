package com.purrer.gentools.entities;

import java.util.Objects;

public class AllelePair {

    private final String dominant;

    private final String recessive;

    public AllelePair(String dominant, String recessive) {
        Objects.requireNonNull(dominant, "Dominant allele should not be null");
        Objects.requireNonNull(recessive, "Recessive allele should not be null");
        this.dominant = dominant;
        this.recessive = recessive;
    }

    public String getDominant() {
        return dominant;
    }

    public String getRecessive() {
        return recessive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllelePair that = (AllelePair) o;
        return Objects.equals(dominant, that.dominant) && Objects.equals(recessive, that.recessive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dominant, recessive);
    }

}
