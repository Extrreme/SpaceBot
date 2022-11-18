package dev.extrreme.spacebot.utils;

public class NumberUtility {
    /**
     * Checks if the provided string contains a number (integer, double, float, long, short, byte)
     *
     * @param str the string to check contains an {@link Number}
     * @return TRUE if the string contains an integer, otherwise FALSE
     */
    public static boolean isNumeric(String str) {
        return isInteger(str) || isDouble(str) || isFloat(str) || isLong(str) || isShort(str) || isByte(str);
    }

    /**
     * Checks if the provided string contains an integer, useful check before using {@link Integer#parseInt(String)}
     *
     * @param str the string to check contains an {@link Integer}
     * @return TRUE if the string contains an integer, otherwise FALSE
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the provided string contains a double, useful check before using {@link Double#parseDouble(String)}
     *
     * @param str the string to check contains an {@link Double}
     * @return TRUE if the string contains a double, otherwise FALSE
     */
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the provided string contains a float, useful check before using {@link Float#parseFloat(String)}
     *
     * @param str the string to check contains a {@link Float}
     * @return TRUE if the string contains an integer, otherwise FALSE
     */
    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the provided string contains a long, useful check before using {@link Long#parseLong(String)}
     *
     * @param str the string to check contains an {@link Long}
     * @return TRUE if the string contains a long, otherwise FALSE
     */
    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the provided string contains a short, useful check before using {@link Short#parseShort(String)}
     *
     * @param str the string to check contains an {@link Short}
     * @return TRUE if the string contains a short, otherwise FALSE
     */
    public static boolean isShort(String str) {
        try {
            Short.parseShort(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the provided string contains a byte, useful check before using {@link Byte#parseByte(String)}
     *
     * @param str the string to check contains an {@link Byte}
     * @return TRUE if the string contains a byte, otherwise FALSE
     */
    public static boolean isByte(String str) {
        try {
            Byte.parseByte(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static int clampInt(int num, int min, int max) {
        return (int) clamp(num, min, max);
    }

    public static double clamp(double num, double min, double max) {
        if (num > max) {
            return max;
        } else if (num < min) {
            return min;
        }
        return num;
    }

    public static boolean between(double num, double lower, double upper, boolean inclusiveLower,
                                  boolean inclusiveUpper) {
        return (inclusiveLower ? num >= lower : num > lower) && (inclusiveUpper ? num <= upper : num < upper);
    }
}

