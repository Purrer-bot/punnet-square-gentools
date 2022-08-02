package com.purrer.gentools.utils;

import com.purrer.gentools.entities.Gamete;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;
import com.purrer.gentools.interfaces.SequenceValidation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Helper class for working with gametes
 */
public class GameteCombiner {

    private final SequenceValidation validation;
    private final GameteGroupsExtractor extractor;

    public GameteCombiner(SequenceValidation validation, GameteGroupsExtractor extractor) {
        this.validation = validation;
        this.extractor = extractor;
    }

    /**
     * Get all gamete combinations
     *
     * @param sequence gene in string format (AaBbCc)
     * @return gamete combinations: {Abc, ABC, ..., abc}
     * @throws IllegalArgumentException if sequence has invalid pattern (valid: AaBbCc, invalid: Aad)
     */
    public List<String> getGametes(String sequence) {
        if(!validateSequence(sequence)){
            throw new IllegalArgumentException("Invalid sequence: " + sequence);
        }
        Map<String, List<Gamete>> gameteGroups = extractor.getGameteGroups(sequence);
        int gametesCount = gameteGroups.size();
        Iterator<List<Gamete>> iterator = gameteGroups.values().iterator();
        String[][] alleles = new String[gametesCount][2];

        for (int i = 0; i < gametesCount; i++) {
            List<Gamete> gametes = iterator.next();
            alleles[i][0] = gametes.get(0).getGameteValue();
            alleles[i][1] = gametes.get(1).getGameteValue();
        }
        return Arrays.asList(getCombinations(alleles));
    }

    private String[] getCombinations(String[][] alleles) {
        if (alleles.length == 1) {
            String[] gametes = new String[2];
            gametes[0] = alleles[0][0];
            gametes[1] = alleles[0][1];
            return gametes;
        }
        String[] combos = new String[(int) Math.pow(2, alleles.length)];
        String[] other = getCombinations(Arrays.copyOfRange(alleles, 1, alleles.length));
        for (int i = 0; i < combos.length; i++) {
            combos[i] =   alleles[0][i * 2 / combos.length] + other[i % other.length];
        }
        return combos;
    }
    /**
     * Validate string of gene sequence
     * @param gametes sequence
     * @return true if sequence is valid
     */
    private boolean validateSequence(String gametes){
        return validation.validateSequence(gametes);
    }

}
