package com.purrer.gentools.interfaces;

import com.purrer.gentools.entities.GametePair;

import java.util.List;

/**
 * Extractor of gamete groups from the sequences
 */
public interface GameteGroupsExtractor {

    /**
     * Extracts the gamete groups from the provided sequence. For example:
     *  <pre>
     *  sequence = "AaBBCc"
     *  output  ->  [{A, a}, {B, B}, {C, c}]
     *  </pre>
     * @param sequence sequence to extract gamete groups
     * @return list of gamete groups of sequence
     */
    List<GametePair> getGameteGroups(String sequence);

}
