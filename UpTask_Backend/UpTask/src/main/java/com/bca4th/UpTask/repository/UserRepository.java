package com.bca4th.UpTask.repository;

import com.bca4th.UpTask.model.Role;
import com.bca4th.UpTask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByRoleAndActiveTrue(Role role);

    List<User> findByActiveTrue();
}
