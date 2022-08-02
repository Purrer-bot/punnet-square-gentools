package com.purrer.gentools.extractor;

import com.purrer.gentools.entities.Gamete;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;
import com.purrer.gentools.interfaces.SequenceTokenizer;
import com.purrer.gentools.interfaces.Token;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Extracts gamete groups from the input sequence using {@link SequenceTokenizer}
 */
public class TokenizingGameteGroupsExtractor implements GameteGroupsExtractor {

    private final SequenceTokenizer tokenizer;

    public TokenizingGameteGroupsExtractor(SequenceTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public Map<String, List<Gamete>> getGameteGroups(String sequence) {
        return tokenizer.tokenize(sequence)
                .stream()
                .map(Token::getTokenValue)
                .map(token -> new Gamete(token.toLowerCase(), token))
                .collect(Collectors.groupingBy(Gamete::getGameteKey));
    }
}
