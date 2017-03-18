package com.pine.ant.lambda;

import javax.swing.JButton;

/**
 * Java 8 类型推断测试
 * 接受泛型的时候需要明确指明参数，否则类型无法正确推断
 * @author shipei
 *
 */
public class TypeInferenceTest {

    public static void main(String[] args) {
        JButton jButton = new JButton();
        jButton.addActionListener(event -> System.out.println(""));
    }
}
