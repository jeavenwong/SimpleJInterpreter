package com.jeaven.Lexer;

import com.jeaven.MyException.LexerException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  Author: jeavenwong
 *  Date: 2020/05/06
 */
public class LexAnalyser {

    //仅仅支持数字的四则运算,支持浮点数和整型
    //匹配数字的正则表达式 ((-)(d)*(.)( d)*)|( d)*|(-)( d)*|( (d)*(.)( d)*)  其中 d 表示 [0-9]
    //匹配运算符的正则表达式 [\+ | - | \* | /]
    //匹配体现优先级的括号 [( | )]
    //匹配 ; 作为终结符 [;]
    private static final char[] OPERATORS = {'+', '-', '*', '/'};

    private final String NUMBER = "Number      ";
    private final String OPERATOR = "Operator    ";
    private final String OTHER = "Other       ";  //空格 回车


    private char[] codes = new char[1000];
    private int length = 0;
    private List<Token> tokens = new ArrayList<>();
    private  List<String> test = new ArrayList<>();

    public LexAnalyser() {}

    public void start(String inputPath) {
        readCode(inputPath);
        analyse();
    }

    private void analyse() {
        int index = 0;
        int state = 0;
        StringBuffer word = new StringBuffer("");
        try {
            while (index < length) {
                char c = codes[index];
                switch (state) {
                    case 0:
                        if(c == '-') {
                            word.append(c);
                            state = 1;
                            index++;
                        } else if(isNumber(c)) {
                            word.append(c);
                            state = 2;
                            index++;
                        } else if (isOperator(c)) {
                            state = 6;
                        } else if(c == ' ' || c == '\n') {
                            index++;
                        } else if(c == '(' || c == ')') {
                            state = 6;
                        } else if(c == ';') {
                            state = 6;
                        } else {
                            state = 6;
                        }
                        break;
                    case 1:
                        if(isNumber(c)) {
                            word.append(c);
                            index++;
                            state = 2;
                        } else {
                            //System.out.println("error happened on state 1...");
                            throw new LexerException("error happened on state 1...");
                        }
                        break;
                    case 2:
                        if(isNumber(c)) {
                            word.append(c);
                            index++;
                        } else if (c == '.') {
                            word.append(c);
                            state = 3;
                            index++;
                        } else {
                            state = 5;
                        }
                        break;
                    case 3:
                        if(isNumber(c)) {
                            word.append(c);
                            index++;
                            state = 4;
                        } else {
                            //System.out.println("error happened on state 3...");
                            throw new LexerException("error happened on state 3...");
                        }
                        break;
                    case 4:
                        if(isNumber(c)) {
                            word.append(c);
                            index++;
                        } else {
                            state = 5;
                        }
                        break;
                    case 5:
                        if(!isNumber(c)) {
                            //test.add(word.toString());
                            tokens.add(new Token("Number", word.toString()));
                            word = new StringBuffer("");
                            state = 0;
                        } else {
                            //System.out.println("error happened on state 5...");
                            throw new LexerException("error happened on state 5...");
                        }
                        break;
                    case 6:
                        if(isOperator(c)) {
                            word.append(c);
                            //test.add(word.toString());
                            tokens.add(new Token("Operator", word.toString()));
                            index++;
                            word = new StringBuffer("");
                            state = 0;
                        } else  if(c == '(' || c == ')') {
                            word.append(c);
                            index++;
                            //test.add(word.toString());
                            tokens.add(new Token("BLAKIT", word.toString()));
                            word = new StringBuffer("");
                            state = 0;
                        } else if(c == ';') {
                            word.append(c);
                            index++;
                            //test.add(word.toString());
                            tokens.add(new Token("Terminator", word.toString()));
                            word = new StringBuffer("");
                            state = 0;
                        } else {
                            word.append(c);
                            index++;
                            //test.add(word.toString());
                            tokens.add(new Token("Error", word.toString()));
                            String ExceptionTmp = word.toString();
                            word = new StringBuffer("");
                            state = 0;
                            throw new LexerException("Unsupport Error, can not support the symbol ' " + ExceptionTmp + " '");
                        }
                        break;
                }
            }
            for(Token s : tokens) {
                s.toString();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * 读取代码并放入char数组
     */

    private void readCode(String inputPath) {
        length = 0;
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath))));
            String line;
            while ((line = bf.readLine()) != null) {
                char[] tmp = line.toCharArray();
                for (char a : tmp) {
                    codes[length++] = a;
                }
                codes[length++] = '\n';
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("!!!ERROR, FAILED TO GET INPUT!!! ");
        }
    }


    /**
     * 将结果输出至文件
     */
    public void saveResult(String outputPath) {
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath)))) {
            for (Token token : tokens) {
                if (token.isValid()) {
                    bw.write(token.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("!!!ERROR FAILED TO OUTPUT!!!");
        }
    }

    /**
     * 获取词法分析完成后的token
     * @return List<Token>
     */
    public List<Token> getTokens() {
        return tokens;
    }


    private boolean isNumber(char i) {
        return (i >= '0' && i <= '9');
    }


    private boolean isOperator(char c) {
        for (char operator : OPERATORS) {
            if (c == operator) {
                return true;
            }
        }
        return false;
    }

    // test
    public static void main(String[] args) {
        LexAnalyser lexAnalyser = new LexAnalyser();
        lexAnalyser.start("data/input.j");
        lexAnalyser.saveResult("data/output.txt");
    }
}
