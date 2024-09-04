package com.purrer.gentools.extractor;

import com.purrer.gentools.entities.GametePair;
import com.purrer.gentools.interfaces.GameteGroupsExtractor;
import com.purrer.gentools.interfaces.SequenceTokenizer;
import com.purrer.gentools.interfaces.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Extracts gamete groups from the input sequence using {@link SequenceTokenizer}
 */
public class TokenizingGameteGroupsExtractor implements GameteGroupsExtractor {

    private final SequenceTokenizer tokenizer;

    public TokenizingGameteGroupsExtractor(SequenceTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public List<GametePair> getGameteGroups(String sequence) {
        List<Token> tokens = tokenizer.tokenize(sequence);

        List<GametePair> pairs = new ArrayList<>();

        Iterator<Token> iterator = tokens.iterator();
        while (iterator.hasNext()) {
            GametePair pair = new GametePair(iterator.next().getTokenValue(), iterator.next().getTokenValue());
            pairs.add(pair);
        }

        return pairs;
    }
}
