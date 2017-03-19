package com.pine.ant.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student {
    public String name;
    public int age;
    public List<String> books;
    public List<Integer> prices;

    public static List<Student> prepareStudentList() {
        Student student1 = new Student();
        student1.name = "tom";
        student1.age = 10;
        student1.books = Arrays.asList("java1", "C", "C#");
        student1.prices = Arrays.asList(10, 20, 30);

        Student student2 = new Student();
        student2.name = "jerry";
        student2.age = 20;
        student2.books = Arrays.asList("java2", "C", "C#");
        student2.prices = Arrays.asList(20, 30, 30);
        
        Student student3 = new Student();
        student3.name = "jack";
        student3.age = 15;
        student3.books = Arrays.asList("java3", "C", "C#");
        student3.prices = Arrays.asList(5, 6, 8);
        
        
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        return students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}