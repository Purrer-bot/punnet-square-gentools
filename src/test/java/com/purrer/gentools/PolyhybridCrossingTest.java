package com.purrer.gentools;

import com.purrer.gentools.crossing.CrossingConfig;
import com.purrer.gentools.crossing.Crossings;
import com.purrer.gentools.crossing.PolyhybridCrossing;
import com.purrer.gentools.extractor.SymbolGameteGroupsExtractor;
import com.purrer.gentools.entities.AllelePair;
import com.purrer.gentools.interfaces.Crossing;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;
import com.purrer.gentools.interfaces.SequenceTokenizer;
import com.purrer.gentools.interfaces.SequenceValidation;
import com.purrer.gentools.tokenizers.AllelePairsTokenizer;
import com.purrer.gentools.utils.GameteCombiner;
import com.purrer.gentools.validation.SequenceValidationImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolyhybridCrossingTest {

    private final Set<AllelePair> allelePairs = Set.of(
            new AllelePair("A", "a"),
            new AllelePair("B", "b"),
            new AllelePair("C", "c"),
            new AllelePair("D", "d"),
            new AllelePair("E", "e"),
            new AllelePair("F", "f"),
            new AllelePair("G", "g"),
            new AllelePair("H", "h")
    );
    private final GameteGroupsExtractor extractor = new SymbolGameteGroupsExtractor();
    private final SequenceValidation validation = new SequenceValidationImpl(extractor, allelePairs);
    private final GameteCombiner combiner = new GameteCombiner(validation, extractor);
    private final SequenceTokenizer tokenizer = new AllelePairsTokenizer(allelePairs);
    private final Crossing crossing = new PolyhybridCrossing(combiner, tokenizer, validation, allelePairs);

    private final String firstSequence = "AaBBccDdEEFfGGhh";
    private final String secondSequence = "aaBbcCddeEFFGgHH";

    @Test
    public void testPolyhybridCrossing() {
        long time = System.nanoTime();
        Map<String, Integer> result = crossing.crossing(firstSequence, secondSequence);
        assertEquals(128, result.size());
        time = (System.nanoTime() - time) / 1_000_000L;
        System.out.println("Crossing took " + time + " milliseconds");
    }

    @Test
    public void testCrossingReordering() {
        CrossingConfig config = new CrossingConfig();
        config.addAllelePairs(
                new AllelePair("Al", "al"),
                new AllelePair("Cf", "cf"),
                new AllelePair("De", "de")
        );

        Crossing crossing = Crossings.create(config);
        Map<String, Integer> psquare = crossing.crossing("CfcfdeDeAlAl", "cfCfdedealAl");
        System.out.println(psquare);
    }

}
