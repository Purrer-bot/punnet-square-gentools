package com.purrer.gentools.utils;

public abstract class StringUtils {

    /**
     * Returns the substring of the provided string including deltas and appender at the beginning and the end of
     * substring. The method will never throw {@link IndexOutOfBoundsException} because it calculates the bounds before
     * calculating the substring. Example:
     * <p>
     *      <code>
     *          StringUtils.safeSubstringWithDeltas(11, 3, 16, 3, "This is the example of the input", "...") -> "...the example..."
     *          <p />
     *          StringUtils.safeSubstringWithDeltas(0, 3, 4, 3, "This is the example of the input", "xxx") -> "This isxxx"
     *      </code>
     * </p>
     * @param from the beginning index, inclusive
     * @param deltaFrom delta of beginning index
     * @param to the ending index, exclusive
     * @param deltaTo delta of the ending index
     * @param string string to substring
     * @param appender appender to add at the beginning and the end of the substring
     * @return substring of a giving string according to provided rules
     */
    public static String safeSubstringWithDeltas(
            int from,
            int deltaFrom,
            int to,
            int deltaTo,
            String string,
            String appender
    ) {
        from = Math.max(from - deltaFrom, 0);
        String leftAppender = from == 0 ? "" : appender;

        to = Math.min(to + deltaTo, string.length());
        String rightAppender = to == string.length() ? "" : appender;

        return String.format("%s%s%s", leftAppender, string.substring(from, to), rightAppender);
    }

}
