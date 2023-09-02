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

    @Query("SELECT n FROM Note n WHERE n.id = :id")
    Note findNoteById(@Param("id") Long id);

    @Query("SELECT n FROM Note n WHERE n.workspace.id = :id")
    List<Note> findByWorkspaceId(Long id);


    @Query("SELECT n FROM Note n WHERE n.workspace.id = :id ORDER BY n.updated_at DESC")
    Page<Note> findAllByOrderByUpdatedAtDesc(Long id, Pageable pageable);
    @Query("SELECT n FROM Note n WHERE n.workspace.id = :id ORDER BY n.updated_at ASC")
    Page<Note> findAllByOrderByUpdatedAtAsc(Long id, Pageable pageable);
    @Query("SELECT n FROM Note n WHERE n.workspace.id = :id ORDER BY n.created_at DESC")
    Page<Note> findAllByOrderByCreatedAtDesc(Long id, Pageable pageable);
    @Query("SELECT n FROM Note n WHERE n.workspace.id = :id ORDER BY n.created_at ASC")
    Page<Note> findAllByOrderByCreatedAtAsc(Long id, Pageable pageable);
    @Query("SELECT n FROM Note n WHERE n.workspace.id = :id ORDER BY n.name DESC")
    Page<Note> findAllByOrderByNameDesc(Long id, Pageable pageable);
    @Query("SELECT n FROM Note n WHERE n.workspace.id = :id ORDER BY n.name ASC")
    Page<Note> findAllByOrderByNameAsc(Long id, Pageable pageable);
}
