package com.pine.ant.lambda;

import java.awt.event.ActionListener;
import java.util.function.BinaryOperator;

/**
 * 几种常见的lambda表达式样式
 * lambda表达式样式的类型推断取决于上下文
 * 由三部分组成: 参数列表，运算符，函数主体
 * 是一个匿名方法，将行为像数据一样传递
 * @author shipei
 *
 */
public class BasicLambda {
    
    public static void main(String[] args) {
        
        //不需要使用参数的lambda表达式
        Runnable noArguments = () -> System.out.println("no need to use arguments");
        new Thread(noArguments).start();
        
        //使用一个参数的lambda表达式
        ActionListener listener = event -> System.out.println("button clicked");
        
        //使用两个参数的lambda表达式
        BinaryOperator<Integer> add = (x,y) -> x+y;
        //调用传入的lambda表达式
        add.apply(10, 20);
        
        //lambda表达式中参数中带有显示的类型
        BinaryOperator<Integer> delete = (Integer x,Integer y) -> x+y;
        //调用传入的lambda表达式
        delete.apply(10, 20);
        
        // 使用代码块的lambda表达式
        Runnable block = () -> {
            System.out.println("This is a block thread!");
        };
        new Thread(block).start();
    }
    
    BasicLambda (int value) {
        System.out.println(value);
    }
    
    void increaseValue (int value) {
        System.out.println(++value);
    }
}
