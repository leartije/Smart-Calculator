package calculator.ui;

import calculator.services.InputParser;

import java.util.Scanner;

import static calculator.util.Msg.*;

public class Calculator {

    private final Scanner SCANNER = new Scanner(System.in);
    private final InputParser inputParser;

    public Calculator() {
        this.inputParser = new InputParser();
    }

    public void start() {
        while (true) {
            String input = SCANNER.nextLine();
            if ("".equals(input)) {
                continue;
            }
            if (EXIT.equals(input)) {
                System.out.println(BYE);
                SCANNER.close();
                return;
            }
            if (HELP.equals(input)) {
                System.out.println("Help me! I don't know how to use calculator! HEEELP!");
                continue;
            }
            if (input.charAt(0) == '/') {
                System.out.println(UNKNOWN_COMMAND);
                continue;
            }
            inputParser.setInput(input);
            inputParser.calculate();

        }
    }

}
