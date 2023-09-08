package com.kai.mynote.repository;

import com.kai.mynote.entities.ActiveCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveCodeRepository extends JpaRepository<ActiveCode, Long> {

    @Query("SELECT c FROM ActiveCode c WHERE c.username =:username")
    ActiveCode getCodeByUsername(String username);

    @Query("SELECT c FROM ActiveCode c WHERE c.email =:email")
    ActiveCode getCodeByEmail(String email);

    @Query("SELECT c FROM ActiveCode c WHERE c.code =:code")
    ActiveCode getCodeByCode(String code);
}
