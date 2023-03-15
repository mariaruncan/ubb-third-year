import java.util.regex.Pattern;

public class MyRegex {
    private static Pattern pattern;
    private static final String idRegex = "^[a-zA-Z][a-zA-Z0-9]{0,7}$";
    private static final String keywordRegex = "^(#include|<iostream>|using|namespace|std|int|main|cin|while|cout|float|if|else)$";
    private static final String constantRegex = "^(\\+|-)?\\d*(\\.\\d+)?$";
    private static final String operatorRegex = "^(!=|>>|<<|>|<|=|-|\\*|\\+)$";
    private static final String separatorRegex = "^([;(){},])$";

    /**
     * Checks if a string is id
     * @param atom string to be checked
     * @return true or false, is the string is id
     */
    static boolean isId(String atom) {
        pattern = Pattern.compile(idRegex);
        return pattern.matcher(atom).find();
    }

    /**
     * Checks if a string is keyword
     * @param atom string to be checked
     * @return true or false, is the string is keyword
     */
    static boolean isKeyWord(String atom) {
        pattern = Pattern.compile(keywordRegex);
        return pattern.matcher(atom).find();
    }

    /**
     * Checks if a string is constant
     * @param atom string to be checked
     * @return true or false, is the string is constant
     */
    static boolean isConstant(String atom) {
        pattern = Pattern.compile(constantRegex);
        return pattern.matcher(atom).find();
    }

    /**
     * Checks if a string is operator
     * @param atom string to be checked
     * @return true or false, is the string is operator
     */
    static boolean isOperator(String atom) {
        pattern = Pattern.compile(operatorRegex);
        return pattern.matcher(atom).find();
    }

    /**
     * Checks if a string is separator
     * @param atom string to be checked
     * @return true or false, is the string is separator
     */
    static boolean isSeparator(String atom) {
        pattern = Pattern.compile(separatorRegex);
        return pattern.matcher(atom).find();
    }
}
