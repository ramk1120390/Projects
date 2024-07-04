package com.Network.Network.DS.DesignPattren.Interpreter;

public class Interpreter {
    private final Expression expression;

    public Interpreter(Expression expression) {
        this.expression = expression;
    }

    public boolean interpret(String context) {
        return expression.interpret(context);
    }
}