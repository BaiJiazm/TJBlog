package com.baijiazm.tjblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baijiazm.tjblog.mapper")
public class TjblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(TjblogApplication.class, args);
    }
}
