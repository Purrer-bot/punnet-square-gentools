package com.purrer.gentools;

import com.purrer.gentools.extractor.SymbolGameteGroupsExtractor;
import com.purrer.gentools.extractor.TokenizingGameteGroupsExtractor;
import com.purrer.gentools.entities.AllelePair;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;
import com.purrer.gentools.interfaces.SequenceTokenizer;
import com.purrer.gentools.interfaces.SequenceValidation;
import com.purrer.gentools.validation.SequenceValidationImpl;
import com.purrer.gentools.validation.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SequenceValidationTest {

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

    private final Set<AllelePair> allelePairsForTokens = Set.of(
            new AllelePair("Aa", "aa"),
            new AllelePair("Bbb", "bbb"),
            new AllelePair("Ccc", "ccc"),
            new AllelePair("Dd", "dd")
    );

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

        sequenceValidation = new SequenceValidationImpl(symbolExtractor, allelePairs);
        sequenceValidationWithTokenExtractor = new SequenceValidationImpl(tokenizingExtractor, allelePairsForTokens);
    }

    @Test
    public void whenSequenceIsNullThenException() {
        ValidationResult validationResult = sequenceValidation.validateSequence(null);
        assertFalse(validationResult.isValid());
        assertTrue(validationResult.getMessage().contains("Sequence should not be null"));
    }

    @Test
    public void whenSequenceLengthIsOddThenFalse() {
        ValidationResult validationResult = sequenceValidation.validateSequence(oddSequence);
        assertFalse(validationResult.isValid());
        assertTrue(validationResult.getMessage().contains("Gamete 'C' doesn't have a pair"));
    }

    @Test
    public void whenSequenceHasGameteWithNoPairThenFalse() {
        ValidationResult validationResult = sequenceValidation.validateSequence(sequenceWithGameteWithNoPair);
        assertFalse(validationResult.isValid());
        assertTrue(validationResult.getMessage().contains("Gamete 'C' doesn't have a pair"));
    }

    @Test
    public void whenSequenceHasRepeatingGametePairsThenFalse() {
        ValidationResult validationResult = sequenceValidation.validateSequence(sequenceWithRepeatingGametePairs);
        assertFalse(validationResult.isValid());
        assertTrue(validationResult.getMessage().contains("Gamete 'A' have more than one pair"));
    }

    @Test
    public void whenSequenceHasNonLettersThenFalse() {
        ValidationResult validationResult = sequenceValidation.validateSequence(sequenceWithNonLetters);
        assertFalse(validationResult.isValid());
    }

    @Test
    public void whenNotFalseSequenceThenTrue() {
        assertTrue(sequenceValidation.validateSequence(normalSequence0).isValid());
    }

    @Test
    public void whenAtLeastOneOfSequencesFalseThenFalse() {
        ValidationResult validationResult = sequenceValidation.validateSequencePair(sequenceWithGameteWithNoPair, normalSequence0);
        assertFalse(validationResult.isValid());
    }

    @Test
    public void whenSequencesHasDifferentLengthsThenFalse() {
        ValidationResult validationResult = sequenceValidation.validateSequencePair(normalSequence0, normalSequence1);
        assertFalse(validationResult.isValid());
    }

    @Test
    public void whenSequencesHasDifferentGametesSetThenFalse() {
        ValidationResult validationResult = sequenceValidation.validateSequencePair(normalSequence0, normalSequence3);
        assertFalse(validationResult.isValid());
    }

    @Test
    public void whenBothSequencesTrueWithSameLengthsThenTrue() {
        ValidationResult validationResult = sequenceValidation.validateSequencePair(normalSequence0, normalSequence2);
        assertTrue(validationResult.isValid());
    }

    @Test
    public void whenTokenizingExtractorAndSplitByUppercaseThenSequenceValid() {
        ValidationResult validationResult = sequenceValidationWithTokenExtractor.validateSequence("AaAaBbbBbbCccCccDdDd");
        assertTrue(validationResult.isValid());
    }

}
