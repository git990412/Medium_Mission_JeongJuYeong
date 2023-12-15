package com.ll.medium.global.initData;

import com.ll.medium.domain.member.role.ERole;
import com.ll.medium.domain.member.role.entity.Role;
import com.ll.medium.domain.member.role.repository.RoleRepository;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@RequiredArgsConstructor
public class initRoles {
  @Autowired @Lazy private initRoles self;
  private final RoleRepository roleRepository;

  @Bean
  public ApplicationRunner initRolesData() {
    return args -> {
      if (roleRepository.count() == 0) {
        Stream.of(ERole.values())
            .forEach(
                role -> {
                  Role role1 = new Role();
                  role1.setName(role);
                  roleRepository.save(role1);
                });
      }
    };
  }
}
