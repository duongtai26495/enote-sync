package com.kai.mynote.repository;

import com.kai.mynote.entities.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    @Query("SELECT b FROM Blacklist b WHERE b.token = :token AND b.user.id = :id")
    Blacklist isExistInBlacklist(@Param("id") Long id, @Param("token") String token);
}
