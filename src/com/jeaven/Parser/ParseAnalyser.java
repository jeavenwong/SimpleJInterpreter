package com.jeaven.Parser;

import com.jeaven.Lexer.LexAnalyser;
import com.jeaven.Lexer.Token;
import com.jeaven.MyException.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Author: jeavenwong
 *  Date: 2020/05/07
 *  此文法分析器只支持的二元运算 + - * / 以及数字（小数，整型）
 *  注意：我们采用的是构造LL(1)文法分析，
 *  并且在算法实现上面我们不采用Table-Driven算法，即不使用First/Fellow集来预先生成一张文法分支预测表，即 Parse Table
 *  而是使用 " 递归下降法 " 来实现 LL(1) 文法分析
 *
 *  参考文章
 *  https://zhuanlan.zhihu.com/p/23123448?refer=vczh-nichijou
 *  https://wenku.baidu.com/view/2533122304a1b0717ed5ddb8.html
 * */

public class ParseAnalyser {
    private int index = 0;
    private int length = 0;
    private List<Token> tokens = new ArrayList<>();

    public Expression getTerm() {
        try {
            if(isNumber()) {
                Expression result = new NumberExpression();
                String str = tokens.get(index).getCode();
                ((NumberExpression) result).setVal(Float.parseFloat(str));
                index++;
                return result;
            } else {
                if(tokens.get(index).getCode().equals("(")) {
                    index++;
                    Expression result = getExp();
                    if(!tokens.get(index).getCode().equals(")")) {
                        throw new ParseException("syntax error, lack ' ) ' ");
                    } else {
                        index++;
                        return result;
                    }
                } else {
                    throw new ParseException("syntax error, lack ' ( ' ");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //用正则表达式判断是否是数字形式的字符串
    public boolean isNumber() {
        String str = tokens.get(index).getCode();
        String regex = "-?[0-9]+(\\.[0-9]+)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(str);
        if(isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public Expression getFactor() {
        Expression result = null;
        Expression temp0 = getTerm();
        while(true) {
            String operator = "";
            if(tokens.get(index).getCode().equals("*")) {
                operator = "*";
                index++;
            } else if(tokens.get(index).getCode().equals("/")) {
                operator = "/";
                index++;
            } else {
                break;
            }
            Expression temp1 = new BinaryOperatorExpression();
            ((BinaryOperatorExpression)temp1).setOperator(operator);
            temp1.left = temp0;
            temp1.right = getTerm();
            result = temp1;
            temp0 = result;
        }
        if(result == null)
            result = temp0;
        return result;
    }

    public Expression getExp() {
        Expression result = null;
        Expression temp0 = getFactor();
        while(true) {
            String operator = "";
            if(tokens.get(index).getCode().equals("+")) {
                operator = "+";
                index++;
            } else if(tokens.get(index).getCode().equals("-")) {
                operator = "-";
                index++;
            } else {
                break;
            }
            Expression temp1 = new BinaryOperatorExpression();
            ((BinaryOperatorExpression)temp1).setOperator(operator);
            temp1.left = temp0;
            temp1.right = getFactor();
            result = temp1;
            temp0 = result;
        }
        if(result == null)
            result = temp0;
        return result;
    }


    // 语法分析
    public List<String> parse(String inputPath) {
        //调用词法分析器对源码进行分词
        LexAnalyser lexAnalyser = new LexAnalyser();
        lexAnalyser.start(inputPath);
        lexAnalyser.saveResult("data/output.txt");
        tokens = lexAnalyser.getTokens();
        length = tokens.size();

        // 进行 LL(1) 语法分析
        // 得到一颗 AST
        Expression AST_ROOT = getExp();

        //对 AST 进行后序遍历，得到逆波兰表达式
        ASTLastOrderTraverse(AST_ROOT);
//        for(String c : lastOrderList) {
//            System.out.print(c + " ");
//        }

        return lastOrderList;
    }


    private List<String> lastOrderList = new ArrayList<>(); // AST 后序
    private List<String> inOrderList = new ArrayList<>();   // AST 先序

    // AST 中序遍历  得到的是正顺的 token 序列
    public void ASTinOrderTraverse(Expression head) {
        if(head == null) {
            return ;
        }
        if(head.left != null) {
            ASTinOrderTraverse(head.left);
        }
        String name = head.getClass().getTypeName();
        if(head.getClass().getTypeName() == "com.jeaven.Parser.BinaryOperatorExpression") {
            inOrderList.add(((BinaryOperatorExpression) head).getOperator());
        } else {
            inOrderList.add(String.valueOf(((NumberExpression)head).getVal()));
        }
        if(head.right != null) {
            ASTinOrderTraverse(head.right);
        }
    }

    // AST 后序遍历  得到的是 token 序列的逆波兰表达式
    // 用于 Stack-based VM 用逆波兰表达式求值
    public void ASTLastOrderTraverse(Expression head) {
        if(head == null) {
            return;
        }
        if(head.left != null) {
            ASTLastOrderTraverse(head.left);
        }
        if(head.right != null) {
            ASTLastOrderTraverse(head.right);
        }
        if(head.getClass().getTypeName() == "com.jeaven.Parser.BinaryOperatorExpression") {
            lastOrderList.add(((BinaryOperatorExpression) head).getOperator());
        } else {
            lastOrderList.add(String.valueOf(((NumberExpression)head).getVal()));
        }
    }

    // test
    public static void main(String[] args) {
        String inputPath = "data/input.j";
        new ParseAnalyser().parse(inputPath);
    }
}

