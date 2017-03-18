package com.pine.ant.lambda;

import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * java 8 提供的基本的函数式接口
 * @author shipei
 *
 */
public class FunctionalExample {
    
    public static void main(String[] args) {
        
        // Predict 类
        Predicate<String> predict = s -> s.length() > 10;
        System.out.println(predict.test("10"));
        
        // Consumer 类
        Consumer<String> consumer = s -> System.out.println(s);
        consumer.accept("100");
        
        // Function 类
        Function<String, Integer> function = x -> Integer.valueOf(x);
        System.out.println(function.apply("1000"));
        
        // Supplier类 工厂方法
        Supplier<FunctionalExample> functionalExample = ()->new FunctionalExample("supplier");
        functionalExample.get();
        
        // BinaryOperator方法
        BinaryOperator<String> binaryoperator = (s1,s2) -> s1+s2;
        System.out.println(binaryoperator.apply("s1", "s2"));
        
        // 一元操作类
        UnaryOperator<String> unaryOperator = s -> s+s;
        System.out.println(unaryOperator.apply("unaryOperator"));
        
    }
    
    FunctionalExample (String supplier) {
        System.out.println(supplier);
    }
    
}
