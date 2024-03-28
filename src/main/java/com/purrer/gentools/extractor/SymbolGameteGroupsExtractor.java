package com.purrer.gentools.extractor;

import com.purrer.gentools.entities.Gamete;
import com.purrer.gentools.entities.GametePair;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Extracts gamete groups from the input sequence using simple split-by-character algorithm
 */
public class SymbolGameteGroupsExtractor implements GameteGroupsExtractor {
    @Override
    public List<GametePair> getGameteGroups(String sequence) {
        Map<String, List<Gamete>> gametePairsMap = sequence.chars()
                .mapToObj(c -> (char) c)
                .map(c -> new Gamete(c.toString().toLowerCase(), c.toString()))
                .collect(Collectors.groupingBy(Gamete::getGameteKey));

        List<GametePair> pairs = new ArrayList<>(sequence.length() / 2);
        for (List<Gamete> list : gametePairsMap.values()) {
            if (list.size() < 2) {
                throw new IllegalArgumentException(
                        String.format(
                                "Sequence could not be split into chars. Gamete '%s' doesn't have a pair",
                                list.get(0).getGameteValue()
                        )
                );
            }
            if (list.size() > 2) {
                throw new IllegalArgumentException(
                        String.format(
                                "Sequence could not be split into chars. Gamete '%s' have more than one pair",
                                list.get(0).getGameteValue()
                        )
                );
            }

            pairs.add(new GametePair(list.get(0).getGameteValue(), list.get(1).getGameteValue()));
        }

        return pairs;
    }
}
