package com.pine.ant.lambda;

import java.util.function.BinaryOperator;

/**
 * lambda表达式重载demo
 * @author shipei
 *
 */
public class OverrideExample {
    
    public static void main(String[] args) {
        OverrideExample overrideExample  = new OverrideExample();
        overrideExample.normalOverride(new Object());
        overrideExample.normalOverride(10);
        overrideExample.normalOverride("test");
        // lambda 表达式的重载和普通重载是一样的，同样是依据参数的类型完成推断
        // 参数类型的推断实际是将lambda表达式和接口函数中的函数进行对比
        // 最终选择的结果是同类型中最为具体的函数接口的类型
        overrideExample.overrideLambda((x,y)->x+y);
    }
    
    public void normalOverride(Object o) {
        System.out.println("Object ^^^^");
    }
    
    public void normalOverride(String s) {
        System.out.println("String ^^^^");
    }
    
    public void overrideLambda(BinaryOperator<Integer> lambda) {
        System.out.println("BinaryOperator ^^^");
    }
    
    public void overrideLambda(IntegerBiFunction lambda) {
        System.out.println("IntegerBiFunction ^^^");
    }
    
    // 自定义接口函数
    // 此处继承了BinaryOperator， 所以会正确的重载，否则编译报错
    private interface IntegerBiFunction extends BinaryOperator<Integer> {
        
    }
    
    /*private interface IntegerBiFunction {
        public Integer apply(Integer x1, Integer x2);
    }*/
}
