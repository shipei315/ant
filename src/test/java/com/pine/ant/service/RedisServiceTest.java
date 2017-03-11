package com.pine.ant.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pine.ant.AntApplication;
import com.pine.ant.service.redis.RedisOperatorService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=AntApplication.class)// 指定spring-boot的启动类  
public class RedisServiceTest {
    
    @Autowired
    RedisOperatorService redisOperatorService;
    
    @Test
    public void sendTest(){
        try {
            redisOperatorService.set("a", "ant", false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
