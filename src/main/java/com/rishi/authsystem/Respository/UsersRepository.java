package com.rishi.authsystem.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishi.authsystem.Model.UserEntity;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    List<UserEntity> findByName(String name);

    boolean existsByEmail(String email);

}