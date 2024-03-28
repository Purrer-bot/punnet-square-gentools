package com.purrer.gentools.interfaces;

import com.purrer.gentools.validation.ValidationResult;

public interface SequenceValidation {
    ValidationResult validateSequence(String sequence);
    ValidationResult validateSequencePair(String firstSequence, String secondSequence);
}
