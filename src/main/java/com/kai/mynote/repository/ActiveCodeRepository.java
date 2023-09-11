package com.kai.mynote.repository;

import com.kai.mynote.entities.ActivateCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveCodeRepository extends JpaRepository<ActivateCode, Long> {

    @Query("SELECT c FROM ActivateCode c WHERE c.username =:username")
    ActivateCode getCodeByUsername(String username);

    @Query("SELECT c FROM ActivateCode c WHERE c.email =:email")
    ActivateCode getCodeByEmail(String email);

    @Query("SELECT c FROM ActivateCode c WHERE c.code =:code")
    ActivateCode getCodeByCode(String code);
}
