package calculator.services;

import java.util.Stack;

public record Calculation(ToPostfix postfix) {

    public long calculate() {

        String postfix = postfix().toPostfix();
        String[] split = postfix.split("\\s");
        Stack<Long> numbers = new Stack<>();

        for (String s : split) {

            if (s.matches("[/*^+-]")) {

                Long b = numbers.pop();
                Long a = numbers.pop();

                switch (s) {
                    case "+" -> numbers.push(a + b);
                    case "-" -> numbers.push(a - b);
                    case "*" -> numbers.push(a * b);
                    case "/" -> {
                        try {
                            numbers.push(a / b);
                        } catch (ArithmeticException e) {
                            System.out.println(e);
                            return 0;
                        }
                    }
                    case "^" -> numbers.push((long) Math.pow(a, b));
                }

            } else {
                numbers.push(Long.parseLong(s));
            }
        }

        return numbers.pop();
    }

}
