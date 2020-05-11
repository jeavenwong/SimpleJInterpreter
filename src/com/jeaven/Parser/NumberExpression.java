package com.jeaven.Parser;

public class NumberExpression extends Expression {
    public NumberExpression() {
        super();
    }
    private float val;
    public void setVal(float _val) {
        val = _val;
    }

    public float getVal() {
        return val;
    }
}
