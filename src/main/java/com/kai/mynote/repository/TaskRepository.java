package com.kai.mynote.repository;

import com.kai.mynote.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByNoteId(Long id);


    @Query("SELECT t FROM Task t WHERE t.author.username =:username")
    List<Task> getAllTasksByUsername(String username);

    @Query("SELECT t FROM Task t WHERE t.note.id =:id ORDER BY t.type DESC, t.updated_at DESC")
    Page<Task> findByNoteIdOrderByLastEditedAtDESC(Long id, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.note.id =:id ORDER BY t.type DESC, t.created_at DESC")
    Page<Task> findByNoteIdOrderByCreatedAtDESC(Long id, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.note.id =:id ORDER BY t.type DESC, t.created_at ASC")
    Page<Task> findByNoteIdOrderByCreatedAtASC(Long id, Pageable pageable);

}
