package org.jad.auth.config;

import org.jad.auth.entity.RoleEntity;
import org.jad.auth.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleConfig {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(RoleEntity.builder().name("USER").build());
                roleRepository.save(RoleEntity.builder().name("ADMIN").build());
                roleRepository.save(RoleEntity.builder().name("FOURNISSEUR").build());
            }
        };
    }
}
