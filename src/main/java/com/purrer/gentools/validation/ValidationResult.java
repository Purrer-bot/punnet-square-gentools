package com.purrer.gentools.validation;

public class ValidationResult {

    private final boolean isValid;

    private final String message;

    private ValidationResult(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    public static ValidationResult valid() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult invalid(String message) {
        return new ValidationResult(false, message);
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {
        return message;
    }

    public ValidationResult and(ValidationResult other) {
        boolean isValid = this.isValid && other.isValid;
        return new ValidationResult(isValid, isValid ? null : other.message);
    }
}
