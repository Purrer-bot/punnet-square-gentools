package com.purrer.gentools;

import com.purrer.gentools.crossing.PolyhybridCrossing;
import com.purrer.gentools.extractor.SymbolGameteGroupsExtractor;
import com.purrer.gentools.interfaces.Crossing;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;
import com.purrer.gentools.interfaces.SequenceValidation;
import com.purrer.gentools.utils.GameteCombiner;
import com.purrer.gentools.validation.SequenceValidationImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolyhybridCrossingTest {

    private final GameteGroupsExtractor extractor = new SymbolGameteGroupsExtractor();
    private final SequenceValidation validation = new SequenceValidationImpl(extractor);
    private final GameteCombiner combiner = new GameteCombiner(validation, extractor);
    private final Crossing crossing = new PolyhybridCrossing(combiner);

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

}
