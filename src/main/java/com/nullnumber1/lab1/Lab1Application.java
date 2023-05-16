package com.nullnumber1.lab1;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Lab1Application {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Lab1Application.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
