package com.invitation.module.rds;

import com.invitation.module.common.CommonApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
// https://stackoverflow.com/questions/58091902/how-to-inherit-application-properties-with-spring-boot-multiple-modules

// https://spring.io/blog/2020/08/14/config-file-processing-in-spring-boot-2-4
// https://stackoverflow.com/questions/23138494/spring-boot-application-properties-maven-multi-module-projects
// @PropertySource(value="classpath:config/application.yml", factory = YamlPropertySourceFactory.class)
@SpringBootApplication
public class DataRdsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataRdsApplication.class, args);
    }
}
