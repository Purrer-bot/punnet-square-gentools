package com.purrer.gentools;

import com.purrer.gentools.entities.Gamete;
import com.purrer.gentools.extractor.SymbolGameteGroupsExtractor;
import com.purrer.gentools.extractor.TokenizingGameteGroupsExtractor;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;
import com.purrer.gentools.interfaces.SequenceTokenizer;
import com.purrer.gentools.interfaces.SequenceValidation;
import com.purrer.gentools.validation.SequenceValidationImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceValidationTest {

    private final String sequenceWithGameteWithNoPair = "AaBbCd";
    private final String oddSequence = "AAbbC";
    private final String sequenceWithRepeatingGametePairs = "AaBbCcAa";
    private final String sequenceWithNonLetters = "AaBb22..";
    private final String normalSequence0 = "AaBBcCDd";
    private final String normalSequence1 = "AaBBcCDdee";
    private final String normalSequence2 = "aaBbCCDd";
    private final String normalSequence3 = "aaEeCCDd";

    private final GameteGroupsExtractor symbolExtractor;
    private final GameteGroupsExtractor tokenizingExtractor;
    private final SequenceTokenizer tokenizer;
    private final SequenceValidation sequenceValidation;
    private final SequenceValidation sequenceValidationWithTokenExtractor;

    public SequenceValidationTest() {
        tokenizer = new TestTokenizer();

        symbolExtractor = new SymbolGameteGroupsExtractor();
        tokenizingExtractor = new TokenizingGameteGroupsExtractor(tokenizer);

        sequenceValidation = new SequenceValidationImpl(symbolExtractor);
        sequenceValidationWithTokenExtractor = new SequenceValidationImpl(tokenizingExtractor);
    }

    @Test
    public void whenSequenceIsNullThenException() {
        assertThrows(NullPointerException.class, () -> {
            boolean b = sequenceValidation.validateSequence(null);
        });
    }

    @Test
    public void whenSequenceLengthIsOddThenFalse() {
        assertFalse(sequenceValidation.validateSequence(oddSequence));
    }

    @Test
    public void whenSequenceHasGameteWithNoPairThenFalse() {
        assertFalse(sequenceValidation.validateSequence(sequenceWithGameteWithNoPair));
    }

    @Test
    public void whenSequenceHasRepeatingGametePairsThenFalse() {
        assertFalse(sequenceValidation.validateSequence(sequenceWithRepeatingGametePairs));
    }

    @Test
    public void whenSequenceHasNonLettersThenFalse() {
        assertFalse(sequenceValidation.validateSequence(sequenceWithNonLetters));
    }

    @Test
    public void whenNotFalseSequenceThenTrue() {
        assertTrue(sequenceValidation.validateSequence(normalSequence0));
    }

    @Test
    public void whenAtLeastOneOfSequencesFalseThenFalse() {
        assertFalse(sequenceValidation.validateSequencePair(sequenceWithGameteWithNoPair, normalSequence0));
    }

    @Test
    public void whenSequencesHasDifferentLengthsThenFalse() {
        assertFalse(sequenceValidation.validateSequencePair(normalSequence0, normalSequence1));
    }

    @Test
    public void whenSequencesHasDifferentGametesSetThenFalse() {
        assertFalse(sequenceValidation.validateSequencePair(normalSequence0, normalSequence3));
    }

    @Test
    public void whenBothSequencesTrueWithSameLengthsThenTrue() {
        assertTrue(sequenceValidation.validateSequencePair(normalSequence0, normalSequence2));
    }

    @Test
    public void whenTokenizingExtractorThenSplitByUppercase() {
        Map<String, List<Gamete>> aaBbbCccD = tokenizingExtractor.getGameteGroups("AaBbbCccD");

        List<Gamete> aa = aaBbbCccD.get("aa");
        assertNotNull(aa);
        assertEquals("aa", aa.get(0).getGameteKey());
        assertEquals("Aa", aa.get(0).getGameteValue());


        List<Gamete> bbb = aaBbbCccD.get("bbb");
        assertNotNull(bbb);
        assertEquals("bbb", bbb.get(0).getGameteKey());
        assertEquals("Bbb", bbb.get(0).getGameteValue());
    }

    @Test
    public void whenTokenizingExtractorAndSplitByUppercaseThenSequenceValid() {
        boolean aaBbbCccD = sequenceValidationWithTokenExtractor.validateSequence("AaAaBbbBbbCccCccDdDd");
        assertTrue(aaBbbCccD);
    }

}
