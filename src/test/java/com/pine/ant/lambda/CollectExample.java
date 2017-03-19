package com.pine.ant.lambda;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.pine.ant.lambda.customerCollector.StringCollector;
import com.pine.ant.lambda.customerCollector.StringCombiner;

public class CollectExample {
    
    public static void main(String[] args) {
        List<Student> students = Student.prepareStudentList();
        
        // Collect 转化为其他集合
        students.get(0).prices.stream().collect(Collectors.toCollection(TreeSet::new));
        Student youngest = students.stream().min(Comparator.comparing(student -> student.age)).get();
        System.out.println(youngest.name);
        //求取平均值
        double avgDouble = students.stream().collect(Collectors.averagingInt(student -> student.age));
        System.out.println(avgDouble);
        
        //数据分组，partitioningBy传入一个Predicate函数接口的实现
        Map<Boolean,List<Student>> res = students.stream().collect(Collectors.partitioningBy(student -> student.name.startsWith("t")));
       
        //更加自然的分组方式， groupingBy传入一个Function函数接口即可
        Map<String,List<Student>> res2 = students.stream().collect(Collectors.groupingBy(student -> student.name));
        
        // 分组的统计方式上进行修改
        Map<String,Long> res3 = students.stream().collect(Collectors.groupingBy(student -> student.name, Collectors.counting()));
        res3.forEach((key, val) -> {
            System.out.println("key is "+key+" val is "+val);
        });
        
        //基于分组的统计，将分组结果记录到对应的Key上
        Function<Student,String> function = student -> {
            if(student.age <= 18){
                return "young";
            }else {
                return "adult";
            }
        };
        Map<String,List<String>> res4 = students.stream().collect(Collectors.groupingBy(function, Collectors.mapping(Student::getName,Collectors.toList())));
        res4.forEach((key, val) -> {
            val.stream().forEach(temp -> System.out.println("key is "+key+" val is "+temp));
        });
        
        // 数据分组，调用join方法
        String allNames = students.stream().map(student -> student.name).collect(Collectors.joining(",", "{", "}"));
        System.out.println("all names are *** "+allNames);
        
        // 这个Demo没有跑起来，运行会报错
        try{
            String s = students.stream().filter(student -> null != student.name)
                                        .map(Student::getName)
                                        .collect(new StringCollector())
                                        .toString();
            
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        //这个实现了
        String s1 = students.stream().filter(student -> null != student.name)
                .map(Student::getName)
                .collect(StringCombiner::new, StringCombiner::add,StringCombiner::merge)
                .sb.toString();
        System.out.println("customer collector res is "+ s1);
    }
}
