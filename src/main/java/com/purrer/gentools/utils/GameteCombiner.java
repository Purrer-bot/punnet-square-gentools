package com.purrer.gentools.utils;

import com.purrer.gentools.entities.GametePair;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;
import com.purrer.gentools.interfaces.SequenceValidation;
import com.purrer.gentools.validation.ValidationResult;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
        ValidationResult validationResult = validateSequence(sequence);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException(
                    String.format("Invalid sequence: %s. %s", sequence, validationResult.getMessage())
            );
        }
        List<GametePair> gameteGroups = extractor.getGameteGroups(sequence);
        int gametesCount = gameteGroups.size();
        Iterator<GametePair> iterator = gameteGroups.iterator();
        String[][] alleles = new String[gametesCount][2];

        for (int i = 0; i < gametesCount; i++) {
            GametePair gametes = iterator.next();
            alleles[i][0] = gametes.getFirstGamete();
            alleles[i][1] = gametes.getSecondGamete();
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
            combos[i] = alleles[0][i * 2 / combos.length] + other[i % other.length];
        }
        return combos;
    }

    /**
     * Validate string of gene sequence
     *
     * @param gametes sequence
     * @return validation result
     */
    private ValidationResult validateSequence(String gametes) {
        return validation.validateSequence(gametes);
    }

}
