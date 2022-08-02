package com.purrer.gentools;

import com.purrer.gentools.interfaces.Token;

public class DefaultToken implements Token {

    private final String value;

    public DefaultToken(String value) {
        this.value = value;
    }

    @Override
    public String getTokenValue() {
        return value;
    }
}
