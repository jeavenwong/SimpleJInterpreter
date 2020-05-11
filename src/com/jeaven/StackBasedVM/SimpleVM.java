package com.jeaven.StackBasedVM;

import com.jeaven.Parser.ParseAnalyser;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: jeavenwong
 * Date: 2020/05/07
 *
 * 写一个超级简易的 stack-based 的 VM 来对语法分析后的 AST 进行解释
 * */
public class SimpleVM {
    private String[] operators = {"+", "-", "*", "/"};

    public void run(List<String> params) {
        int index = 0;
        int len = params.size();
        Stack<String> stack = new Stack<>();
        do {
            if(isNumber(params.get(index))) {
                stack.push(params.get(index));
            } else if(IsOperator(params.get(index))){
                String param2 = stack.pop();
                String param1 = stack.pop();
                String operator = params.get(index);
                float a = 0.0f;
                float b = 0.0f;
                float res = 0.0f;
                switch (operator) {
                    case "+":
                        a = Float.parseFloat(param1);
                        b = Float.parseFloat(param2);
                        res = a + b;
                        stack.push(String.valueOf(res));
                        System.out.println(param1 + " + " + param2 + " 's result is: " + String.valueOf(res));
                        break;
                    case "-":
                        a = Float.parseFloat(param1);
                        b = Float.parseFloat(param2);
                        res = a - b;
                        stack.push(String.valueOf(res));
                        System.out.println(param1 + " - " + param2 + " 's result is: " + String.valueOf(res));
                        break;
                    case "*":
                        a = Float.parseFloat(param1);
                        b = Float.parseFloat(param2);
                        res = a * b;
                        stack.push(String.valueOf(res));
                        System.out.println(param1 + " * " + param2 + " 's result is: " + String.valueOf(res));
                        break;
                    case "/":
                        a = Float.parseFloat(param1);
                        b = Float.parseFloat(param2);
                        res = a / b;
                        stack.push(String.valueOf(res));
                        System.out.println(param1 + " / " + param2 + " 's result is: " + String.valueOf(res));
                        break;
                }
            }
            index++;
        } while(index < len);
        System.out.println("the final result is: " + stack.pop());
    }

    //判断是不是操作符
    public boolean IsOperator(String str) {
        for(String c : operators) {
            if(str.equals(c)) {
                return true;
            }
        }
        return false;
    }

    //用正则表达式判断是否是数字形式的字符串
    public boolean isNumber(String str) {
        String regex = "-?[0-9]+(\\.[0-9]+)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(str);
        if(isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    // test
    public static void main(String[] args) {
        ParseAnalyser parseAnalyser = new ParseAnalyser();
        List<String> list = parseAnalyser.parse("data/input.j");
        SimpleVM simpleVM = new SimpleVM();
        simpleVM.run(list);
    }

}
