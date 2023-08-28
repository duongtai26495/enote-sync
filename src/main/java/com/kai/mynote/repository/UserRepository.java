package com.kai.mynote.repository;

import com.kai.mynote.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByEmail(String email);
    User findFirstByUsername(String username);

}
