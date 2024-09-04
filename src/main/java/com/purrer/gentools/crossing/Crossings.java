package com.purrer.gentools.crossing;

import com.purrer.gentools.extractor.TokenizingGameteGroupsExtractor;
import com.purrer.gentools.entities.AllelePair;
import com.purrer.gentools.interfaces.Crossing;
import com.purrer.gentools.tokenizers.AllelePairsTokenizer;
import com.purrer.gentools.utils.GameteCombiner;
import com.purrer.gentools.validation.SequenceValidationImpl;

import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Factory which constructs common instances of {@link Crossing}
 */
public class Crossings {

    public static final int NUMBER_OF_LATIN_LETTERS = 26;

    /**
     * Creates default instance of {@link Crossing} which splits gamete pairs by uppercase latin letters from A to Z,
     * for instance:
     * <pre>
     *     - AaBBcC - correct sequence for crossing, gametes will be [a, b, c]
     *     - ABbCC  - incorrect sequence for crossing because gamete [a] doesn't have a pair
     * </pre>
     */
    public static Crossing createDefaultCrossing() {
        return createDefaultCrossing(NUMBER_OF_LATIN_LETTERS);
    }

    /**
     * Creates default instance of {@link Crossing} which splits gamete pairs by uppercase latin letters, the number
     * of which depends on parameter <code>maxAlleles</code> - the sequence length will be limited using this parameter.
     * For instance (maxAlleles = 3):
     * <pre>
     *     - AaBBcC - correct sequence for crossing, gametes will be [a, b, c]
     *     - ABbCC  - incorrect sequence for crossing because gamete [a] doesn't have a pair
     *     - AABbCcdd - incorrect sequence because number of gametes in sequence exceeds maximum number of alleles
     * </pre>
     * @param maxAlleles - number of available alleles in sequences which created {@link Crossing} supports. This number
     *                   should be equal to (2 <= maxAlleles <= 26). For example, if maxAlleles = 3 then
     *                   the maximum available sequence can be 'AaBBcc', 'aaBbCC', 'bBAAcC' etc.
     */
    public static Crossing createDefaultCrossing(int maxAlleles) {
        if (maxAlleles < 2) {
            throw new IllegalArgumentException("maxAlleles number should be greater or equal to 2");
        }

        int minChar = 'a';
        int maxChar = 'z';

        if (maxAlleles > NUMBER_OF_LATIN_LETTERS) {
            throw new IllegalArgumentException("maxAlleles should be less or equal to number of latin letters 26");
        }

        AllelePair[] allelePairs = IntStream
                .range(minChar, maxChar + 1)
                .mapToObj(asciiNumber -> {
                    String allele = String.valueOf((char) asciiNumber);
                    return new AllelePair(allele.toUpperCase(), allele);
                })
                .toArray(AllelePair[]::new);

        CrossingConfig config = new CrossingConfig();
        config.addAllelePairs(allelePairs);

        return create(config);
    }

    /**
     * Creates instance of {@link Crossing} according to provided <code>config</code>
     * @param config config from which {@link Crossing} will be created
     * @return crossing configured with provided config
     */
    public static Crossing create(CrossingConfig config) {
        Objects.requireNonNull(config);
        Set<AllelePair> allelePairs = config.getAllelePairs();
        AllelePairsTokenizer tokenizer = new AllelePairsTokenizer(allelePairs);
        TokenizingGameteGroupsExtractor gameteGroupsExtractor = new TokenizingGameteGroupsExtractor(tokenizer);
        SequenceValidationImpl sequenceValidation = new SequenceValidationImpl(gameteGroupsExtractor, allelePairs);
        GameteCombiner gameteCombiner = new GameteCombiner(sequenceValidation, gameteGroupsExtractor);
        return new PolyhybridCrossing(gameteCombiner, tokenizer, sequenceValidation, allelePairs);
    }

}
