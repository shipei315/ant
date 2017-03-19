package com.pine.ant.lambda;

import java.util.IntSummaryStatistics;
import java.util.List;

/**
 * 本实例的作用是展示流对象对Java基本类型的处理
 * <br/>
 * Java 的泛型是对泛型参数类型的擦除，换句话说他是Object对象的实例
 * <br/>因此对于基本类型的变量只有将其转化为装箱类型才能使用List进而使用相关流对象
 * 由此引出两类操作：1是将基本类型转换为装箱类型称之为装箱 反之就称之为拆箱
 * <br/>对于需要大量数值运算的算法来说，拆箱和装箱将会增加计算开销和占用额外的存储空间
 * <br/>Stream类的某些方法对基本类型和装箱类型做了区分，Java 8 中对整形，长整型和双浮点型进行了特殊处理
 * <br/>特殊处理之后的计算效率有了明显提升
 * @author shipei
 *
 */
public class PrimitiveExample {
    
    public static void main(String[] args) {
        
        List<Student> students = Student.prepareStudentList();
        IntSummaryStatistics statistics = students.stream().flatMap(student -> student.prices.stream()).mapToInt(a ->a ).summaryStatistics();
        System.out.println(statistics.getMax());
        System.out.println(statistics.getMin());
        System.out.println(statistics.getAverage());
        System.out.println(statistics.getCount());
        
        // 对基本类型做特殊处理的方法在命名上有明确规范
        // 如果返回类型是基本类型则在基本类型前面加上To 如 ToIntFunction
        // 如果参数类型是基本类型则不加前缀只需要类型名称即可 如IntFunction
        // 如果是高阶函数则直接在操作之后加上To类型即可 如mapToInt
        // 与之相对应的是返回的流也以相应的基本类型开头 如 IntStream
    }
}
