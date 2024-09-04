package com.purrer.gentools.crossing;

import com.purrer.gentools.entities.AllelePair;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CrossingConfig {

    private final Set<AllelePair> allelePairs = new LinkedHashSet<>();
    private Comparator<AllelePair> allelePairsOrderComparator = null;

    public void addAllelePairs(AllelePair... pairs) {
        for (AllelePair pair : pairs) {
            addAllelePair(pair);
        }
    }

    public void addAllelePair(AllelePair pair) {
        validateNotExist(pair);
        allelePairs.add(pair);
    }

    public void setAllelePairsOrderComparator(Comparator<AllelePair> comparator) {
        this.allelePairsOrderComparator = comparator;
    }

    public Set<AllelePair> getAllelePairs() {
        Set<AllelePair> resultSet = allelePairs;
        if (allelePairsOrderComparator != null) {
            resultSet = resultSet.stream()
                    .sorted(allelePairsOrderComparator)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return Collections.unmodifiableSet(resultSet);
    }

    private void validateNotExist(AllelePair pair) {
        boolean alleleWithDominantOrRecessiveExists = allelePairs
                .stream()
                .anyMatch(p -> p.getDominant().equals(pair.getDominant()) || p.getRecessive().equals(pair.getRecessive()));

        if (alleleWithDominantOrRecessiveExists) {
            throw new IllegalStateException(
                    String.format(
                            "Gamete pair with dominant '%s' or recessive '%s' already exist in a config",
                            pair.getDominant(),
                            pair.getRecessive()
                    )
            );
        }
    }

}
