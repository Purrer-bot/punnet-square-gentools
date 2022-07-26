package com.purrer.gentools.interfaces;

import java.util.List;

public interface SequenceTokenizer {
    List<Token> tokenize(String sequence);
}
