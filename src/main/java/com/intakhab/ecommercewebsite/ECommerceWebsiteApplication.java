package com.intakhab.ecommercewebsite;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication
@PropertySource("classpath:views.properties")
@EnableScheduling
public class ECommerceWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceWebsiteApplication.class, args);
    }
}
