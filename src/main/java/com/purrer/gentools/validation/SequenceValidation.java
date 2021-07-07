package com.purrer.gentools.validation;

import com.purrer.gentools.interfaces.Validation;

public class SequenceValidation implements Validation {
    @Override
    public boolean validate(String sequence) {
        if (sequence.length() % 2 != 0) {
            return false;
        }
        for (int j = 0; j < sequence.length() - 1; j += 2) {
            if (!String.valueOf(sequence.charAt(j)).equalsIgnoreCase(String.valueOf(sequence.charAt(j + 1)))) {
                return false;
            }
        }
        return true;
    }
}
