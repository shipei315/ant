package com.pine.ant.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;import org.springframework.core.env.SystemEnvironmentPropertySource;

/**
 * 流操作示例代码
 * <br/>判断是否是惰性求值方法的标准：如果返回值仍然是Stream，则是惰性求值
 * <br/>如果返回值是另外一个值或者空就是及早求值方法
 * @author shipei
 *
 */
public class StreamExample {
    
    public static void main(String[] args) {
        StreamExample streamExample = new StreamExample();
        List<Student> students = streamExample.prepareStudentLit();
        
        // 惰性求值方法，最终不产生新的集合，只是描述新的集合
        students.stream().filter(student -> {
            System.out.println(student.name);
            return student.age <= 18;
        });
        
        // 及早求值方法，最终不产生新的集合，只是描述新的集合
        students.stream().filter(student -> {
            System.out.println(student.name);
            return student.age <= 18;
        }).count();
        
        // 常见操作之collect
        students.stream().filter(student -> {
            System.out.println(student.name);
            return student.age <= 18;
        }).collect(Collectors.toList());
        
        // 常见操作之map 将流中的值转换为另外一个对象，该函数的参数类型是Function
        List<Integer> nameSize = students.stream().filter(student -> StringUtils.isNotBlank(student.name))
                                         .map(student -> student.name.length())
                                         .collect(Collectors.toList());
        nameSize.stream().forEach(i -> System.out.println(i));
        
        // 将函数单独定义出来传递给map
        Function<Student, String> function = student -> student.name.toUpperCase();
        List<String> nameSize2 = students.stream().filter(student -> StringUtils.isNotBlank(student.name))
                                          .map(function).collect(Collectors.toList());
        nameSize2.stream().forEach(s -> System.out.println(s));
        
        // 常见操作之filter,将函数单独定义出来传递给filter
        Predicate<Student> predict = student -> student.name.length()> 3;
        List<String> name3 = students.stream().filter(predict)
                .map(function).collect(Collectors.toList());
        name3.stream().forEach(s -> System.out.println(s));
        
        // 常见类型之flatMap，针对流中的每一个对象转化为一个新的流，全局再合并成一个流
        List<String> chars = students.stream().flatMap(student -> Arrays.asList(student.name.toCharArray()).stream())
                                   .map(c -> new String(c)).collect(Collectors.toList());
        chars.forEach(c -> System.out.println(c));
        
        // 将flatMap转为函数单独提取出来
        System.out.println("FlatMap function ……");
        Function<Student,Stream<char[]>> flapFunction = student -> Arrays.asList(student.name.toCharArray()).stream();
        List<String> chars2 = students.stream().flatMap(flapFunction)
                .map(c -> new String(c)).collect(Collectors.toList());
        chars2.forEach(c -> System.out.println(c));
        
        // 常见类型之min,取出流中最小的值
        System.out.println("min ……");
        Student youngest = students.stream().min(Comparator.comparing(student -> student.age)).get();
        System.out.println(youngest.name);
        
        // 常见类型之max,同时将Comparator以函数的形式单独提取出来
        // comparing方法接受一个函数返回另外一个函数
        System.out.println("max ……");
        Comparator<Student> comparator = Comparator.comparing(student -> student.age);
        Student oldest = students.stream().max(comparator).get();
        System.out.println(oldest.name);
        
        // 常见类型之max,同时将Comparator以函数的形式单独提取出来
        // 此时提取不再使用Comparator.comparing方法构造
        System.out.println("max with comparator function ……");
        Comparator<Student> ageComparator = (student1, student2) -> student1.age - student2.age;
        Student oldest2 = students.stream().max(ageComparator).get();
        System.out.println(oldest2.name);
        
        // 常见类型操作之reduce
        BinaryOperator<Integer> operator  = (age1, age2) -> age1+age2;
        int totalAge = students.stream()
                               .map(student -> student.age)
                               .reduce(0, operator);
        System.out.println("Total age is "+totalAge);
        
        System.out.println("begin to use own map");
        List<String> names = streamExample.owmMap(students, student -> student.name);
        names.forEach(name -> System.out.println(name));
        //****以后所有的操作对list反复处理并不影响原先的list，启示就是是否一定需要对外暴露List接口
        //****使用Stream可以很好的封装内部实现的数据结构
        //****以上所有操作均存在链式操作的现象，对操作行为进行了封装
    }
    
    private List<Student> prepareStudentLit () {
        Student student1 = new Student();
        student1.name = "tom";
        student1.age = 10;
        student1.books = Arrays.asList("java1", "C", "C#");
        
        Student student2 = new Student();
        student2.name = "jerry";
        student2.age = 20;
        student2.books = Arrays.asList("java2", "C", "C#");
        
        Student student3 = new Student();
        student3.name = "jack";
        student3.age = 15;
        student3.books = Arrays.asList("java3", "C", "C#");
        
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        return students;
    }
    
    public static class Student{
        public String name;
        public int age;
        public List<String> books;
    }
    
    /**
     * Java 7 版本的代码示例
     * <br/> 获取所有的以J开头的书
     * @param students
     * @return
     */
    List<String> getAllJBooks(List<Student> students) {
        List<String> bookList = new ArrayList<>();
        for (Student student : students) {
            List<String> books  = student.books;
            if (CollectionUtils.isEmpty(books)) {
                continue;
            }
            
            for (String book : books) {
                if (book.startsWith("j")) {
                    bookList.add(book);
                }
            }
        }
        return bookList;
    }
    
    /**
     * Java 8 版本的代码示例
     * <br/> 流式处理
     * @param students
     * @return
     */
    List<String> getAllJBooks8(List<Student> students) {
        return students.stream()
                       .filter(student -> CollectionUtils.isNotEmpty(student.books))
                       .flatMap(student -> student.books.stream())
                       .filter(s -> s.startsWith("j"))
                       .collect(Collectors.toList());
    }
    
    /**
     * 自己实现的map，内部利用的是BiFunction 和 BinaryOperator
     * @param students
     * @param function
     * @return
     */
    private <R> List<R> owmMap (List<Student> students, Function<Student,R> function) {
        
        //前两个表示函数的参数类型，最后表示返回类型
        BiFunction<List, Student, List> biFunction = (list1, student) -> {
            list1.add(function.apply(student));
            return list1;
        };
        
        BinaryOperator<List> addOperator  = (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
        
        List list = students.stream().reduce(new ArrayList(), biFunction, addOperator);
        System.out.println("student size is "+list.size());
        return list;
    }
    
}
