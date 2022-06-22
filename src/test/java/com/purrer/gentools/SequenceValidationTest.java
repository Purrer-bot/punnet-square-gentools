package com.purrer.gentools;

import com.purrer.gentools.interfaces.SequenceValidation;
import com.purrer.gentools.validation.SequenceValidationImpl;
import org.junit.jupiter.api.Test;

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

    private final SequenceValidation sequenceValidation;

    public SequenceValidationTest() {
        sequenceValidation = new SequenceValidationImpl();
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

}
