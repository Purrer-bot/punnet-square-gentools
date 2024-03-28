package com.purrer.gentools.tokenizers;

import com.purrer.gentools.DefaultToken;
import com.purrer.gentools.entities.AllelePair;
import com.purrer.gentools.interfaces.SequenceTokenizer;
import com.purrer.gentools.interfaces.Token;
import com.purrer.gentools.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link SequenceTokenizer} which tokenizes the sequences according to the set of available alleles
 */
public class AllelePairsTokenizer implements SequenceTokenizer {

    private final Set<String> alleleKeySet;

    public AllelePairsTokenizer(Set<AllelePair> pairs) {
        alleleKeySet = pairs
                .stream()
                .map(p -> Set.of(p.getDominant(), p.getRecessive()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Token> tokenize(String sequence) {
        List<Token> tokens = new ArrayList<>();

        int from = 0;
        int to = 1;

        while (to <= sequence.length()) {
            GetTokenResult token = getToken(sequence, from, to, alleleKeySet);
            if (token == null) {
                String near = StringUtils.safeSubstringWithDeltas(from, 3, to, 3, sequence, "...");
                throw new RuntimeException(
                        String.format(
                                "Unable to tokenize sequence %s on position from: %d to %d near '%s'",
                                sequence,
                                from,
                                to,
                                near
                        )
                );
            }

            tokens.add(token.token);
            from = token.lastTo;
            to = from + 1;
        }

        return tokens;
    }

    private GetTokenResult getToken(String originalSequence, int from, int to, Set<String> keySet) {

        Set<String> tokenKeys = getTokens(originalSequence, from, to, keySet);
        if (tokenKeys.isEmpty()) {
            return null;
        }

        if (tokenKeys.size() == 1) {
            String tokenKey = tokenKeys.stream().findFirst().get();
            String token = originalSequence.substring(from, to);

            while (!tokenKey.equals(token)) {
                to++;
                token = originalSequence.substring(from, to);
            }

            return new GetTokenResult(new DefaultToken(token), to);
        }

        return getToken(originalSequence, from, to + 1, tokenKeys);
    }

    private Set<String> getTokens(String originalSequence, int from, int to, Set<String> keySet) {
        String subsequence = originalSequence.substring(from, to);
        return keySet.stream().filter(ks -> ks.startsWith(subsequence)).collect(Collectors.toSet());
    }

    private static class GetTokenResult {
        private final Token token;
        private final int lastTo;

        private GetTokenResult(Token token, int lastTo) {
            this.token = token;
            this.lastTo = lastTo;
        }
    }

}
