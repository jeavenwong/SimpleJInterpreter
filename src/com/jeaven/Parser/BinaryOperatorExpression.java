package com.jeaven.Parser;

public class BinaryOperatorExpression extends Expression {
    public BinaryOperatorExpression() {
        super();
    }

    private String operator;

    public void setOperator(String _operator) {
        operator = _operator;
    }

    public String getOperator() {
        return operator;
    }
}
