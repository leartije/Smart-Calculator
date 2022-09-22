package calculator.services;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

import static calculator.util.Msg.*;

public class InputParser {

    private final String ALPHA = "(?i)[a-z]+";
    private final String NUMBER = "[+-]*[0-9]+";

    private final Queue<String> EXPRESSION;
    private final Map<String, Long> VARIABLE;
    private String input;
    private final ToPostfix toPostfix;
    private final Calculation calculation;

    public InputParser() {
        this.EXPRESSION = new ArrayDeque<>();
        this.VARIABLE = new HashMap<>();
        this.toPostfix = new ToPostfix();
        this.calculation = new Calculation(toPostfix);
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Queue<String> getEXPRESSION() {
        return EXPRESSION;
    }

    private boolean isValidFormat() {
        input = input.strip();

        //make one space between operands, operators and parenthesis
        input = input.replaceAll("(^[+-]\\d+|\\d+|[a-z]+|\\(|\\))", " $1 ");
        input = input.strip().replaceAll("\\s+", " ");

        //checks if there is more than one consecutive * or /
        if (input.matches(".*[*]{2,}.*") || input.matches(".*[/]{2,}.*")) {
            System.out.println(INVALID_EXPRESSION);
            return false;
        }
        //checks parenthesis
        if (input.contains("(") || input.contains(")")) {
            if (input.indexOf(")") < input.indexOf("(") || !checkParentheses(input)) {
                System.out.println(INVALID_EXPRESSION);
                return false;
            }
        }
        //checks if expression ends with number, variable or parenthesis
        if (!input.matches(".*((?i)[a-z]+|[0-9]+|\\))$")) {
            System.out.println(INVALID_EXPRESSION);
            return false;
        }

        return true;
    }

    private boolean isExpression() {
        Pattern pattern = Pattern.compile("(^(?i)([+-]*[0-9]+|[a-z]+)[\\s]*[/*+^-]+.*)|^\\(.+");
        return pattern.matcher(input).matches();
    }


    private boolean isValidExpression() {
        EXPRESSION.clear();
        String[] split = input.split("\\s");

        for (String s : split) {
            if (s.matches(NUMBER)) {
                EXPRESSION.offer(s);
                continue;
            }
            if (s.matches(ALPHA) && VARIABLE.get(s) == null) {
                System.out.println(INVALID_IDENTIFIER);
                return false;
            }
            if (s.matches(ALPHA) && VARIABLE.get(s) != null) {
                EXPRESSION.offer(String.valueOf(VARIABLE.get(s)));
                continue;
            }
            if (s.matches("[+]+")) {
                EXPRESSION.offer("+");
                continue;
            }
            if (s.matches("[-]+")) {
                EXPRESSION.offer(s.length() % 2 == 0 ? "+" : "-");
                continue;
            }
            EXPRESSION.offer(s);
        }

        return true;
    }

    private void identifierAndVariable() {
        input = input.strip().replaceAll("\\s+", "");
        String[] split = input.split("=");

        if (split.length == 1) {
            if (!split[0].matches(ALPHA)) {
                System.out.println(INVALID_IDENTIFIER);
                return;
            }

            if (VARIABLE.get(split[0]) != null) {
                System.out.println(VARIABLE.get(split[0]));
                return;
            }
            System.out.println(UNKNOWN_VARIABLE);
            return;
        }
        if (split.length == 2) {
            if ((split[0].matches(ALPHA)) && split[1].matches(ALPHA)) {
                if (VARIABLE.get(split[1]) != null) {
                    VARIABLE.put(split[0], VARIABLE.get(split[1]));
                    return;
                }
                System.out.println(UNKNOWN_VARIABLE);
                return;
            }
            if ((split[0].matches(ALPHA)) && split[1].matches(NUMBER)) {
                VARIABLE.put(split[0], Long.parseLong(split[1]));
                return;
            }
            System.out.println(INVALID_ASSIGNMENT);
            return;
        }

        System.out.println(INVALID_ASSIGNMENT);

    }

    public void calculate() {
        if (!isValidFormat()) {
            return;
        }
        if (isExpression()) {
            if (!isValidExpression()) {
                return;
            }
        } else {
            identifierAndVariable();
            return;
        }
        toPostfix.setEXPRESSION(getEXPRESSION());
        System.out.println(calculation.calculate());
    }

    private boolean checkParentheses(String input) {
        int a = 0;
        int b = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ')') {
                a++;
                continue;
            }
            if (input.charAt(i) == '(') {
                b++;
            }
        }
        return a == b;
    }

}
