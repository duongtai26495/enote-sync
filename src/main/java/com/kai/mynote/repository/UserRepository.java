package com.kai.mynote.repository;

import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.User;
import com.kai.mynote.entities.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByEmail(String email);
    User findFirstByUsername(String username);

}
