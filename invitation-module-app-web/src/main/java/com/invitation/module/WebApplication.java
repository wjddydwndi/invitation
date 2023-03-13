package com.invitation.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@ComponentScan(
        useDefaultFilters = true,
        basePackages = "com.invitation.module",
        includeFilters = {
                @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION),
                @ComponentScan.Filter(value = Service.class, type = FilterType.ANNOTATION),
                @ComponentScan.Filter(value = Repository.class, type = FilterType.ANNOTATION),
                @ComponentScan.Filter(value = Component.class, type = FilterType.ANNOTATION),
        }
)
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
