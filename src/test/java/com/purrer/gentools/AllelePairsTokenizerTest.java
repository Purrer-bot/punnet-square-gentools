package com.purrer.gentools;

import com.purrer.gentools.entities.AllelePair;
import com.purrer.gentools.interfaces.Token;
import com.purrer.gentools.tokenizers.AllelePairsTokenizer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllelePairsTokenizerTest {

    private final Set<AllelePair> singleLetterAllelePairs = Set.of(
            new AllelePair("A", "a"),
            new AllelePair("B", "b"),
            new AllelePair("C", "c")
    );

    private final Set<AllelePair> complexLetterAllelePairs = Set.of(
            new AllelePair("An", "an"),
            new AllelePair("Bec", "bec"),
            new AllelePair("One", "Two"),
            new AllelePair("All", "None")
    );

    private final AllelePairsTokenizer singleLetterTokenizer = new AllelePairsTokenizer(singleLetterAllelePairs);

    private final AllelePairsTokenizer complexLetterTokenizer = new AllelePairsTokenizer(complexLetterAllelePairs);

    @Test
    public void test() {
        List<Token> aabbCC = singleLetterTokenizer.tokenize("AabbCC");
        assertEquals(6, aabbCC.size());
        assertEquals(2, aabbCC.stream().filter(t -> t.getTokenValue().equals("b")).count());
        assertEquals(2, aabbCC.stream().filter(t -> t.getTokenValue().equals("C")).count());
    }

    @Test
    public void testOfComplexLetterTokenizer() {
        List<Token> anAnOneOnebecbec = complexLetterTokenizer.tokenize("AnAnTwoOnebecbecAllNone");
        assertEquals(8, anAnOneOnebecbec.size());
        assertEquals(1, anAnOneOnebecbec.stream().filter(t -> t.getTokenValue().equals("None")).count());
        assertEquals(2, anAnOneOnebecbec.stream().filter(t -> t.getTokenValue().equals("An")).count());
        assertEquals(1, anAnOneOnebecbec.stream().filter(t -> t.getTokenValue().equals("All")).count());
    }

}
