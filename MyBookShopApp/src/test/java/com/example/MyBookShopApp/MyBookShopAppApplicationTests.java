package com.example.MyBookShopApp;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MyBookShopAppApplicationTests {
    private final MyBookShopAppApplication myBookShopAppApplication;
    @Value("${auth.secret}")
    String secret;

    @Autowired
    MyBookShopAppApplicationTests(MyBookShopAppApplication myBookShopAppApplication) {
        this.myBookShopAppApplication = myBookShopAppApplication;
    }

    @Test
    void contextLoads() {
        assertNotNull(myBookShopAppApplication);
    }
    @Test
    void verifyAuthSecret(){
        assertThat(secret, Matchers.containsString("box"));
    }

}
