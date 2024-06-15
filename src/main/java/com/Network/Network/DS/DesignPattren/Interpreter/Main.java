package com.Network.Network.DS.DesignPattren.Interpreter;

public class Main {
    public static Expression getMaleExpression() {
        Expression robert = new TerminalExpression("Robert");
        Expression john = new TerminalExpression("John");
        return new OrExpression(robert, john);
    }

    public static Expression getMarriedWomanExpression() {
        Expression julie = new TerminalExpression("Julie");
        Expression married = new TerminalExpression("Married");
        return new AndExpression(julie, married);
    }

    public static void main(String[] args) {
        Expression maleExpression = getMaleExpression();
        Expression marriedWomanExpression = getMarriedWomanExpression();

        Interpreter interpreter = new Interpreter(maleExpression);
        System.out.println("John is male? " + interpreter.interpret("John"));

        interpreter = new Interpreter(marriedWomanExpression);
        System.out.println("Julie is a married woman? " + interpreter.interpret("Married Julie"));
    }
}
