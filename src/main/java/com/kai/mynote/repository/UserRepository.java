package com.kai.mynote.repository;

import com.kai.mynote.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email =:email")
    User findFirstByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.username =:username")
    User findFirstByUsername(String username);

}
