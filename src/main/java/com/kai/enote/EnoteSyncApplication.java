package com.kai.enote;

import com.kai.enote.models.Role;
import com.kai.enote.models.User;
import com.kai.enote.repository.RoleRepository;
import com.kai.enote.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class EnoteSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnoteSyncApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
		return args -> {
			if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role(1, "ADMIN"));
			roleRepository.save(new Role(2,"USER"));
			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("Blackhat1"));
			admin.setAuthorities(roles);
			admin.setFullname("Kai Admin");
			admin.setUserId(1);
			userRepository.save(admin);
		};
	}
}
