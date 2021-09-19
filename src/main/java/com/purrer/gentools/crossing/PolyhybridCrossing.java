package com.purrer.gentools.crossing;

import com.purrer.gentools.interfaces.Crossing;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class PolyhybridCrossing implements Crossing {

    /**
     * Create polyhybrid flat Punnet Square
     *
     * @param parent1 list of first parent's gametes
     * @param parent2 list of second parent's gametes
     * @return list of all possible gamete combinations
     */
    private static List<String> generatePunnetSquare(List<String> parent1, List<String> parent2) {
        List<String> p1 = stream(parent1.toArray(new String[0])).sorted().collect(Collectors.toList());
        List<String> p2 = stream(parent2.toArray(new String[0])).sorted().collect(Collectors.toList());
        List<String> square = new ArrayList<>();

        for (int i = 0; i < parent1.size(); i++) {
            String oneHalf = p1.get(i);
            for (int j = 0; j < parent2.size(); j++) {
                square.add(reorder(oneHalf + p2.get(j)));
            }
        }
        return square;
    }

    /**
     * Helper method to reorder gene sequence after cross
     *
     * @param gene sequence in string format (example: AbcABC)
     * @return reordered sequence (example AABbCc)
     */
    private static String reorder(String gene) {
        Character[] chars = new Character[gene.length()];
        for (int i = 0; i < chars.length; i++)
            chars[i] = gene.charAt(i);

        Arrays.sort(chars,
                Comparator
                        .comparingInt((Character c) -> Character.toLowerCase(c.charValue()))
                        .thenComparingInt(Character::charValue));
        StringBuilder sb = new StringBuilder(chars.length);
        for (char c : chars)
            sb.append(c);
        return sb.toString();
    }

    /**
     * Build a map of genotypes:
     * <p>
     * AaBb : 4
     * <br>
     * aabb : 2
     * <br>
     * ...
     * </p>
     *
     * @param genotypes list of genotypes
     * @return counting map
     */
    private static Map<String, Integer> getGenotypeCross(List<String> genotypes) {
        Map<String, Integer> phenotypes = new HashMap<>();

        for (String string : genotypes) {

            phenotypes.putIfAbsent(string, 0);
            phenotypes.computeIfPresent(string, (k, v) -> v += 1);
        }

        return phenotypes;
    }

    /**
     * Build polyhybrid crossing of two gene sequences
     *
     * @param maleSequence   gene sequence in format: AaBbCc
     * @param femaleSequence gene sequence in same format and length as male
     * @return counting map of all possible genotypes:
     * <p>
     * AaBb : 4
     * <br>
     * aabb : 2
     * <br>
     * ...
     * </p>
     */
    @Override
    public Map<String, Integer> crossing(String maleSequence, String femaleSequence) {
        List<String> maleSequenceGametes = com.purrer.gentools.utils.GameteCombiner.getGametes(maleSequence);
        List<String> femaleSequenceGametes = com.purrer.gentools.utils.GameteCombiner.getGametes(femaleSequence);
        List<String> flatPunnetSquare = generatePunnetSquare(maleSequenceGametes, femaleSequenceGametes);
        return getGenotypeCross(flatPunnetSquare);
    }

}
