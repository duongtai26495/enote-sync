package com.kai.mynote.config;

import com.kai.mynote.entities.Role;
import com.kai.mynote.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InitDataConfig {

    @Bean
    public CommandLineRunner initialDataLoader(RoleRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                // Nếu không có dữ liệu nào trong cơ sở dữ liệu, thêm dữ liệu ban đầu
                repository.saveAll(List.of(
                        new Role("ROLE_USER"),
                        new Role("ROLE_ADMIN")
                ));
            }
        };
    }
}
