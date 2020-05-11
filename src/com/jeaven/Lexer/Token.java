package com.jeaven.Lexer;

/**
 * Author: jeavenwong
 * Date: 2020/05/06
 */
public class Token {

    public static final int ERROR = -1;
    public static final int VT_NUM = 0;     // num
    public static final int VT_ADD = 1;     // +
    public static final int VT_SUB = 2;     // -
    public static final int VT_MULTI = 3;     // *
    public static final int VT_DEVID = 4;   // /
    public static final int VT_L_B = 5;     // (
    public static final int VT_R_B = 6;     // )
    public static final int VT_END = 7;     // $R

    private int typeInt;
    private String typeStr;
    private String code;
    private String error;

    public Token(String typeStr, String code) {
        this.typeStr = typeStr;
        this.code = code;
        this.error = null;
        this.typeInt = toTypeInt();
    }

    public Token(int typeInt, String code, String error) {
        this.typeInt = typeInt;
        this.code = code;
        this.error = error;
    }

    public Token(int typeInt, String code) {
        this.typeInt = typeInt;
        this.code = code;
    }

    public boolean isValid() {
        return  (!code.equals(" ") && !code.equals("\n"));
    }


    public int getTypeInt() {
        return typeInt;
    }

    public String getCode() {
        return code;
    }

    private int toTypeInt() {
        if (typeStr.contains("Number")) {
            return VT_NUM;
        } else if(typeStr.contains("Error")) {
            return ERROR;
        } else {
            switch (code) {
                case "+":
                    return VT_ADD;
                case "-":
                    return VT_SUB;
                case "*":
                    return VT_MULTI;
                case "/":
                    return VT_DEVID;
                case "(":
                    return VT_L_B;
                case ")":
                    return VT_R_B;
                case ";":
                    return VT_END;
                default:
                    return ERROR;
            }
        }
    }

    @Override
    public String toString() {
        return "Token: { " +
//                "typeStr= " + typeStr +
                "typeInt= " + typeInt +
                ",  code='" + code + '\'' +
                (error != null ?
                        (",  error='" + error + '\'' + " }") : " }");
    }
}
