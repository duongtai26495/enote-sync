package com.kai.mynote.repository;

import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.role_name = :name")
    Role findRoleByName(String name);

}
