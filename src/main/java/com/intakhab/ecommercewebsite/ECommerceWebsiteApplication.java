package com.intakhab.ecommercewebsite;

import com.intakhab.ecommercewebsite.Enum.UserRole;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@PropertySource("classpath:views.properties")
@EnableScheduling
@RequiredArgsConstructor
public class ECommerceWebsiteApplication {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ECommerceWebsiteApplication.class, args);
    }

    @Bean
    public CommandLineRunner setupDefaultUser() {
        return args -> {
            List<User> adminList = userRepo.findByRole(UserRole.ADMIN);
            User admin = !adminList.isEmpty() ? adminList.get(0) : null;
            if (admin == null) {
                User newAdmin = new User();
                newAdmin.setRole(UserRole.ADMIN);
                newAdmin.setUsername("admin");
                newAdmin.setPassword(passwordEncoder.encode("admin"));
                newAdmin.setName("Admin");
                newAdmin.setEmailId("admin@gmail.com");
                newAdmin.setPhoneNumber("1234567890");
                newAdmin.setRegisterDate(LocalDate.now());

                userRepo.save(newAdmin);
            }
        };
    }
}