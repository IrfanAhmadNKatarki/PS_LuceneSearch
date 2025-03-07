package com.publicis.sapient.rapid.lookup.RapidLookUp.repo;

import com.publicis.sapient.rapid.lookup.RapidLookUp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
