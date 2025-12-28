package com.bca4th.UpTask.config;

import com.bca4th.UpTask.model.Role;
import com.bca4th.UpTask.model.User;
import com.bca4th.UpTask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("System Admin");
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("Default admin user created: admin@example.com / admin123");
        }

        if (!userRepository.existsByEmail("principal@example.com")) {
            User principal = new User();
            principal.setEmail("principal@example.com");
            principal.setPassword(passwordEncoder.encode("principal123"));
            principal.setName("Principal");
            principal.setRole(Role.PRINCIPAL);
            principal.setActive(true);
            userRepository.save(principal);
            System.out.println("Sample principal user created: principal@example.com / principal123");
        }

        if (!userRepository.existsByEmail("staff@example.com")) {
            User staff = new User();
            staff.setEmail("staff@example.com");
            staff.setPassword(passwordEncoder.encode("staff123"));
            staff.setName("Staff");
            staff.setRole(Role.STAFF);
            staff.setActive(true);
            userRepository.save(staff);
            System.out.println("Sample staff user created: staff@example.com / staff123");
        }
    }
}