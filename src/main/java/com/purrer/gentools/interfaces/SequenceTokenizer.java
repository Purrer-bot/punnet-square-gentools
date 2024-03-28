package com.purrer.gentools.interfaces;

import java.util.List;

/**
 * Tokenizer which splits the input sequence into list of tokens of this sequence. The tokenization algorithm
 * is defined in implementations of this interface
 */
public interface SequenceTokenizer {

    /**
     * Splits the provided sequence into the list of tokens in the same order as these tokens are present in original
     * sequence. Example:
     *  <pre>
     *  sequence = "AaBBCc"
     *  output  ->  [A, a, B, B, C, c]
     *  </pre>
     * @param sequence sequence to split into tokens
     * @return list of tokens of the provided sequence
     */
    List<Token> tokenize(String sequence);

}
