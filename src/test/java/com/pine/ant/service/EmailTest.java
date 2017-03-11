package com.pine.ant.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pine.ant.AntApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=AntApplication.class)// 指定spring-boot的启动类   
//@SpringApplicationConfiguration(classes = AntApplication.class)// 1.4.0 前版本  
public class EmailTest {
    
    @Autowired 
    EmailService emailService;
    
    @Test
    public void sendTest(){
        try {
            emailService.sendSimpleMail();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
