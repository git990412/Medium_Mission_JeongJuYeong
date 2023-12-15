package com.ll.medium.domain.member.role.repository;

import com.ll.medium.domain.member.role.ERole;
import com.ll.medium.domain.member.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  public Role findByName(ERole name);
}
