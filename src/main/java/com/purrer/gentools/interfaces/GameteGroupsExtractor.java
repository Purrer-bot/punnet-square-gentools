package com.purrer.gentools.interfaces;

import com.purrer.gentools.entities.Gamete;

import java.util.List;
import java.util.Map;

/**
 * Extracts gamete groups from the input sequence
 */
public interface GameteGroupsExtractor {
    Map<String, List<Gamete>> getGameteGroups(String sequence);
}
