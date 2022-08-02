package com.purrer.gentools;

import com.purrer.gentools.interfaces.SequenceTokenizer;
import com.purrer.gentools.interfaces.Token;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestTokenizer implements SequenceTokenizer {
    @Override
    public List<Token> tokenize(String sequence) {
        return Arrays.stream(sequence.split("(?=\\p{Upper})"))
                .map(DefaultToken::new).collect(Collectors.toList());
    }
}
