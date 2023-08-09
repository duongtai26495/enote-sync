package com.kai.mynote.repository;

import com.kai.mynote.entities.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE n.author.username = :username")
    List<Note> getAllNote(@Param("username") String username);

    @Query("SELECT n FROM Note n WHERE n.workspace.id = :id order by n.id DESC")
    Page<Note> findByWorkspaceId(Long id, Pageable pageable);
}
