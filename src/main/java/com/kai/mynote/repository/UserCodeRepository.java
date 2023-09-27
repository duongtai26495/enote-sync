package com.kai.mynote.repository;

import com.kai.mynote.entities.UserCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCodeRepository extends JpaRepository<UserCode, Long> {

    @Query("SELECT c FROM UserCode c WHERE c.username =:username")
    UserCode getCodeByUsername(String username);

    @Query("SELECT c FROM UserCode c WHERE c.email =:email")
    UserCode getCodeByEmail(String email);

    @Query("SELECT c FROM UserCode c WHERE c.code =:code")
    UserCode getCodeByCode(String code);
}
