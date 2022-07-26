package com.purrer.gentools.validation;

import com.purrer.gentools.entities.Gamete;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;
import com.purrer.gentools.interfaces.SequenceValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SequenceValidationImpl implements SequenceValidation {

    private final GameteGroupsExtractor extractor;

    public SequenceValidationImpl(GameteGroupsExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public boolean validateSequence(String sequence) {
        Objects.requireNonNull(sequence);

        if (!validateCharacters(sequence)) {
            return false;
        }

        if (!validateRepeatingPairs(sequence)) {
            return false;
        }

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

    @Override
    public boolean validateSequencePair(String firstSequence, String secondSequence) {
        return validateSequence(firstSequence) &&
                validateSequence(secondSequence) &&
                validateLengths(firstSequence, secondSequence) &&
                validateGametePairs(firstSequence, secondSequence);
    }

    private boolean validateLengths(String firstSequence, String secondSequence) {
        return firstSequence.length() == secondSequence.length();
    }

    private boolean validateGametePairs(String firstSequence, String secondSequence) {
        List<String> firstSequenceKeys = new ArrayList<>(getGameteGroups(firstSequence).keySet());
        List<String> secondSequenceKeys = new ArrayList<>(getGameteGroups(secondSequence).keySet());

        for (int i = 0; i < firstSequenceKeys.size(); i++) {
            if (!firstSequenceKeys.get(i).equalsIgnoreCase(secondSequenceKeys.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return true if sequence hasn't repeating gamete pairs, otherwise false
     */
    private boolean validateRepeatingPairs(String sequence) {
        Map<String, List<Gamete>> gameteGroups = getGameteGroups(sequence);

        for (List<Gamete> characters : gameteGroups.values()) {
            if (characters.size() != 2) {
                return false;
            }
        }

        return true;
    }

    private Map<String, List<Gamete>> getGameteGroups(String sequence) {
        return extractor.getGameteGroups(sequence);
    }

    private boolean validateCharacters(String sequence) {
        char[] gametes = sequence.toCharArray();

        for (char c : gametes) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

}
