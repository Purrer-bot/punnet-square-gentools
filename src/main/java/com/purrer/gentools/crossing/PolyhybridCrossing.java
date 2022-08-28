package com.purrer.gentools.crossing;

import com.purrer.gentools.interfaces.Crossing;
import com.purrer.gentools.utils.GameteCombiner;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class PolyhybridCrossing implements Crossing {

    private final GameteCombiner combiner;

    public PolyhybridCrossing(GameteCombiner combiner) {
        this.combiner = combiner;
    }

    /**
     * Create polyhybrid flat Punnet Square
     *
     * @param parent1 list of first parent's gametes
     * @param parent2 list of second parent's gametes
     * @return map of all possible gamete combinations (Punnett square)
     */
    private Map<String, Integer> generatePunnetSquare(List<String> parent1, List<String> parent2) {
        List<String> p1 = stream(parent1.toArray(new String[0])).sorted().collect(Collectors.toList());
        List<String> p2 = stream(parent2.toArray(new String[0])).sorted().collect(Collectors.toList());

        Map<String, Integer> punnetSquare = new HashMap<>();

        for (int i = 0; i < parent1.size(); i++) {
            String oneHalf = p1.get(i);
            List<String> punnetSquareRow = new ArrayList<>();
            for (int j = 0; j < parent2.size(); j++) {
                punnetSquareRow.add(reorder(oneHalf + p2.get(j)));
            }
            mergeSquareWithGenotypesRow(punnetSquareRow, punnetSquare);
        }
        return punnetSquare;
    }

    /**
     * Helper method to reorder gene sequence after cross
     *
     * @param gene sequence in string format (example: AbcABC)
     * @return reordered sequence (example AABbCc)
     */
    protected String reorder(String gene) {
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
     * Merge a map of genotypes:
     * <p>
     * AaBb : 4
     * <br>
     * aabb : 2
     * <br>
     * ...
     * </p>
     *
     * @param genotypes list of genotypes
     * @param squareToMerge Punnett square in which list of genotypes will be merged
     */
    private static void mergeSquareWithGenotypesRow(List<String> genotypes, Map<String, Integer> squareToMerge) {
        for (String string : genotypes) {
            squareToMerge.putIfAbsent(string, 0);
            squareToMerge.computeIfPresent(string, (k, v) -> v += 1);
        }
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
        List<String> maleSequenceGametes = combiner.getGametes(maleSequence);
        List<String> femaleSequenceGametes = combiner.getGametes(femaleSequence);

        return generatePunnetSquare(maleSequenceGametes, femaleSequenceGametes);
    }

}
