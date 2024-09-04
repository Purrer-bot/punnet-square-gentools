package com.purrer.gentools.crossing;

import com.purrer.gentools.entities.AllelePair;
import com.purrer.gentools.interfaces.Crossing;
import com.purrer.gentools.interfaces.SequenceTokenizer;
import com.purrer.gentools.interfaces.SequenceValidation;
import com.purrer.gentools.interfaces.Token;
import com.purrer.gentools.utils.GameteCombiner;
import com.purrer.gentools.validation.ValidationResult;

import java.util.*;

public class PolyhybridCrossing implements Crossing {

    private final GameteCombiner combiner;
    private final SequenceTokenizer tokenizer;
    private final SequenceValidation validation;
    private final Map<String, Integer> alleleToIndexMap = new HashMap<>();

    public PolyhybridCrossing(
            GameteCombiner combiner,
            SequenceTokenizer tokenizer,
            SequenceValidation validation,
            Set<AllelePair> allelePairs
    ) {
        this.combiner = combiner;
        this.tokenizer = tokenizer;
        this.validation = validation;
        int idx = 0;
        for (AllelePair allelePair : allelePairs) {
            alleleToIndexMap.put(allelePair.getDominant(), idx++);
            alleleToIndexMap.put(allelePair.getRecessive(), idx++);
        }
    }

    /**
     * Create polyhybrid flat Punnet Square
     *
     * @param parent1 list of first parent's gametes
     * @param parent2 list of second parent's gametes
     * @return map of all possible gamete combinations (Punnett square)
     */
    private Map<String, Integer> generatePunnetSquare(List<String> parent1, List<String> parent2) {
        Map<String, Integer> punnetSquare = new HashMap<>();

        for (String oneHalf : parent1) {
            List<String> punnetSquareRow = new ArrayList<>();
            for (String string : parent2) {
                punnetSquareRow.add(reorder(oneHalf + string));
            }
            mergeSquareWithGenotypesRow(punnetSquareRow, punnetSquare);
        }
        return punnetSquare;
    }

    /**
     * Helper method to reorder sequence after cross
     *
     * @param sequence sequence in string format (example: AbcABC)
     * @return reordered sequence (example AABbCc)
     */
    protected String reorder(String sequence) {
        List<Token> tokens = tokenizer.tokenize(sequence);
        sortAccordingToAvailableGenesOrder(tokens);

        StringBuilder stringBuilder = new StringBuilder();

        tokens.stream().map(Token::getTokenValue).forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    private void sortAccordingToAvailableGenesOrder(List<Token> tokens) {
        tokens.sort((prev, next) -> {
            int prevIdx = alleleToIndexMap.get(prev.getTokenValue());
            int nextIdx = alleleToIndexMap.get(next.getTokenValue());

            return prevIdx - nextIdx;
        });
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
     * @param genotypes     list of genotypes
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
        ValidationResult validationResult = validation.validateSequencePair(maleSequence, femaleSequence);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException("Sequences are invalid: " + validationResult.getMessage());
        }
        List<String> maleSequenceGametes = combiner.getGametes(maleSequence);
        List<String> femaleSequenceGametes = combiner.getGametes(femaleSequence);

        return generatePunnetSquare(maleSequenceGametes, femaleSequenceGametes);
    }

}
