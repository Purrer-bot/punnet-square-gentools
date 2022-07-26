package com.purrer.gentools.extractor;

import com.purrer.gentools.entities.Gamete;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Extracts gamete groups from the input sequence using simple split-by-character algorithm
 */
public class SymbolGameteGroupsExtractor implements GameteGroupsExtractor {
    @Override
    public Map<String, List<Gamete>> getGameteGroups(String sequence) {
        return sequence.chars()
                .mapToObj(c -> (char) c)
                .map(c -> new Gamete(c.toString().toLowerCase(), c.toString()))
                .collect(Collectors.groupingBy(Gamete::getGameteKey));
    }
}
