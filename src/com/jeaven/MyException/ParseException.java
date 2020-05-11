package com.jeaven.MyException;

//输出词法分析过程中的一些异常
public class ParseException extends Exception{
    public ParseException() {
        super();
    }

    public ParseException(String message) {
        super(message);
    }

}
