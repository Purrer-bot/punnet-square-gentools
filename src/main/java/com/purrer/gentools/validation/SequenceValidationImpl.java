package com.purrer.gentools.validation;

import com.purrer.gentools.entities.AllelePair;
import com.purrer.gentools.entities.GametePair;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;
import com.purrer.gentools.interfaces.SequenceValidation;

import java.util.*;
import java.util.stream.Collectors;

import static com.purrer.gentools.validation.ValidationResult.valid;

public class SequenceValidationImpl implements SequenceValidation {

    private final GameteGroupsExtractor extractor;
    private final Map<String, AllelePair> allelePairMap;

    public SequenceValidationImpl(GameteGroupsExtractor extractor, Set<AllelePair> allelePairs) {
        this.extractor = extractor;
        Map<String, AllelePair> map = new HashMap<>();

        allelePairs.forEach(pair -> {
            map.put(pair.getDominant(), pair);
            map.put(pair.getRecessive(), pair);
        });

        allelePairMap = map;
    }

    @Override
    public ValidationResult validateSequence(String sequence) {
        try {
            Objects.requireNonNull(sequence, "Sequence should not be null");

            if (!validateCharacters(sequence)) {
                return invalid(sequence, "Sequence should only contain characters");
            }

            List<GametePair> gameteGroups = getGameteGroups(sequence);
            if (!validateRepeatingPairs(gameteGroups)) {
                return invalid(sequence, "Sequence should not contain repeating gamete pairs");
            }

            // validate if each gamete pair has only tokens from existing allele pairs
            for (GametePair group : gameteGroups) {
                String firstGamete = group.getFirstGamete();
                String secondGamete = group.getSecondGamete();

                AllelePair allelePair = allelePairMap.get(firstGamete);

                if (allelePair == null || !isFirstOrSecond(allelePair, secondGamete)) {
                    return invalid(
                            sequence,
                            String.format(
                                    "Unexpected gamete %s in sequence near '..%s%s'",
                                    secondGamete,
                                    firstGamete,
                                    secondGamete
                            )
                    );
                }
            }

            return valid();
        } catch (Throwable e) {
            return ValidationResult.invalid(
                    String.format("Unexpected error in sequence: %s. %s", sequence, e.getMessage())
            );
        }
    }

    private boolean isFirstOrSecond(AllelePair pair, String gamete) {
        return pair.getDominant().equals(gamete) || pair.getRecessive().equals(gamete);
    }

    @Override
    public ValidationResult validateSequencePair(String firstSequence, String secondSequence) {
        return validateLengths(firstSequence, secondSequence)
                .and(validateGametePairs(firstSequence, secondSequence));
    }

    private ValidationResult validateLengths(String firstSequence, String secondSequence) {
        boolean isValid = firstSequence.length() == secondSequence.length();
        if (!isValid) {
            return ValidationResult.invalid(
                    String.format("Sequences %s and %s have different lengths", firstSequence, secondSequence)
            );
        }

        return valid();
    }

    private ValidationResult validateGametePairs(String firstSequence, String secondSequence) {
        try {
            List<GametePair> firstSequenceGameteGroups = new ArrayList<>(getGameteGroups(firstSequence));
            List<GametePair> secondSequenceGameteGroups = new ArrayList<>(getGameteGroups(secondSequence));

            for (int i = 0; i < firstSequenceGameteGroups.size(); i++) {
                GametePair firstSequencePair = firstSequenceGameteGroups.get(i);
                GametePair secondSequencePair = secondSequenceGameteGroups.get(i);
                boolean firstAreFromSameAllele =
                        areFromSameAllele(firstSequencePair.getFirstGamete(), secondSequencePair.getFirstGamete());

                if (!firstAreFromSameAllele) {
                    return ValidationResult.invalid(
                            String.format(
                                    "Invalid sequences %s and %s. Sequences have gametes from different alleles '%s' and '%s' accordingly",
                                    firstSequence,
                                    secondSequence,
                                    firstSequencePair.getFirstGamete(),
                                    secondSequencePair.getFirstGamete()
                            )
                    );
                }
                boolean secondAreFromSameAllele =
                        areFromSameAllele(firstSequencePair.getSecondGamete(), secondSequencePair.getSecondGamete());

                if (!secondAreFromSameAllele) {
                    return ValidationResult.invalid(
                            String.format(
                                    "Invalid sequences %s and %s. Sequences have gametes from different alleles '%s' and '%s' accordingly",
                                    firstSequence,
                                    secondSequence,
                                    firstSequencePair.getSecondGamete(),
                                    secondSequencePair.getSecondGamete()
                            )
                    );
                }

            }

            return valid();
        } catch (Throwable e) {
            return ValidationResult.invalid(
                    String.format(
                            "Unexpected error in sequences: %s and %s. %s",
                            firstSequence,
                            secondSequence,
                            e.getMessage()
                    )
            );
        }
    }

    private boolean areFromSameAllele(String firstGamete, String secondGamete) {
        AllelePair alleleForFirstGamete = allelePairMap.get(firstGamete);

        if (alleleForFirstGamete == null) {
            return false;
        }

        return alleleForFirstGamete.getDominant().equals(secondGamete) || alleleForFirstGamete.getRecessive().equals(secondGamete);
    }

    /**
     * @return true if sequence hasn't repeating gamete pairs, otherwise false
     */
    private boolean validateRepeatingPairs(List<GametePair> gameteGroups) {
        Map<GametePair, List<GametePair>> collect = gameteGroups.stream().collect(Collectors.groupingBy(el -> el));

        return collect.values()
                .stream()
                .noneMatch(pairList -> pairList.size() > 1);
    }

    private List<GametePair> getGameteGroups(String sequence) {
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

    private ValidationResult invalid(String sequence, String message) {
        return ValidationResult.invalid(
                String.format(
                        "Invalid sequence: %s. %s",
                        sequence,
                        message
                )
        );
    }

}
