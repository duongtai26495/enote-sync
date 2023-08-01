package com.kai.mynote.repository;

import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.WorkSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkSpace, Long> {

    @Query(value = "SELECT w FROM WorkSpace w WHERE w.author.username = :username")
    Page<WorkSpace> getAllWorkspace(@Param("username") String username, Pageable pageable);

}
