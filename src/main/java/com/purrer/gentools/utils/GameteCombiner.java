package com.purrer.gentools.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Helper class for working with gametes
 */
public class GameteCombiner {
    /**
     * Get all gamete combinations
     *
     * @param sequence gene in string format (AaBbCc)
     * @return gamete combinations: {Abc, ABC, ..., abc}
     * @throws IllegalArgumentException if sequence has invalid pattern (valid: AaBbCc, invalid: Aad)
     */
    public static List<String> getGametes(String sequence) {
        if(!validateSequence(sequence)){
            throw new IllegalArgumentException("Invalid sequence: " + sequence);
        }
        int gametesCount = sequence.length() / 2;
        String[][] alleles = new String[gametesCount][2];
        int even = 0;
        for (int i = 0; i < gametesCount; i++) {
            alleles[i][0] = String.valueOf(sequence.charAt(even));
            alleles[i][1] = String.valueOf(sequence.charAt(even + 1));
            even += 2;
        }
        return Arrays.asList(getCombinations(alleles));
    }

    private static String[] getCombinations(String[][] alleles) {
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
    private static boolean validateSequence(String gametes){
        if (gametes.length() % 2 != 0) {
            return false;
        }
        for (int j = 0; j < gametes.length() - 1; j += 2) {
            if (!String.valueOf(gametes.charAt(j)).equalsIgnoreCase(String.valueOf(gametes.charAt(j + 1)))) {
                return false;
            }
        }
        return true;
    }

}
