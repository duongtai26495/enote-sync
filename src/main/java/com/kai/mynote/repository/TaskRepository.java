package com.kai.mynote.repository;

import com.kai.mynote.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByNoteId(Long id);
    Page<Task> findByNoteId(Long id, Pageable pageable);
}
