package calculator.services;

import java.util.Queue;
import java.util.Stack;

public class ToPostfix {

    private Queue<String> EXPRESSION;

    public ToPostfix() {

    }

    public void setEXPRESSION(Queue<String> EXPRESSION) {
        this.EXPRESSION = EXPRESSION;
    }

    public String toPostfix() {

        StringBuilder postfix = new StringBuilder();
        Stack<String> stack = new Stack<>();

        for (String current : EXPRESSION) {

            if (current.matches("[+-]*[0-9]+")) {
                postfix.append(current).append(" ");
                continue;
            }
            if (current.matches("[/*+^-]")) {
                if (stack.isEmpty() || "(".equals(stack.peek())) {
                    stack.push(current);
                    continue;
                }
                int precedence = getPrecedence(current);
                if (precedence > getPrecedence(stack.peek())) {
                    stack.push(current);
                    continue;
                }
                if (precedence <= getPrecedence(stack.peek())) {
                    while (!stack.isEmpty() && precedence <= getPrecedence(stack.peek()) &&
                            !"(".equals(stack.peek())) {
                        postfix.append(stack.pop()).append(" ");
                    }
                    stack.push(current);
                    continue;
                }
            }
            if ("(".equals(current)) {
                stack.push(current);
                continue;
            }
            if (")".equals(current)) {
                while (!"(".equals(stack.peek())) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.pop();
            }
        }

        for (int i = stack.size() - 1; i >= 0; i--) {
            postfix.append(stack.pop()).append(" ");
        }
        return postfix.toString();
    }

    private int getPrecedence(String string) {
        return switch (string) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> -1;
        };
    }



}
