package com.webfontaine.ghclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCaching
@EnableSwagger2
@Slf4j
public class GhClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GhClientApplication.class, args);
    }

}
