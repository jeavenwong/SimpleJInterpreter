package com.jeaven.MyException;

//输出词法分析过程中的一些异常
public class LexerException extends Exception{
    public LexerException() {
        super();
    }

    public LexerException(String message) {
        super(message);
    }

}
