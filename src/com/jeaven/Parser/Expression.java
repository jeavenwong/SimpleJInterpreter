package com.jeaven.Parser;


/*
* 构造 Expression类 作为 AST（抽象语法树） 的节点类型， 用于构建AST
* Expression 有两种类型，分别是 BinaryOperatorExpression 和 NumberExpression 类型
* BinaryOperatorExpression 是存放操作符的节点，有两个指针，因为 AST 是二叉树
* NumberExpression 是存放数字的节点，是叶子节点，两个指针都是null
*/
public class Expression {
    public Expression left;
    public Expression right;
    public Expression() {
        left = null;
        right = null;
    }
}
