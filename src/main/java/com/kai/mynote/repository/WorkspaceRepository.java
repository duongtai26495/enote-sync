package com.kai.mynote.repository;

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

    @Query(value = "SELECT w FROM WorkSpace w WHERE w.author.username = :username")
    List<WorkSpace> getAllWorkspace(@Param("username") String username);
    @Query(value = "SELECT w FROM WorkSpace w WHERE w.author.username = :username ORDER BY w.created_at ASC")
    Page<WorkSpace> getAllWorkspaceByUsernameOrderByCreatedAtASC(@Param("username") String username, Pageable pageable);

    @Query(value = "SELECT w FROM WorkSpace w WHERE w.author.username = :username ORDER BY w.created_at DESC")
    Page<WorkSpace> getAllWorkspaceByUsernameOrderByCreatedAtDESC(@Param("username") String username, Pageable pageable);

    @Query(value = "SELECT w FROM WorkSpace w WHERE w.author.username = :username ORDER BY w.updated_at ASC")
    Page<WorkSpace> getAllWorkspaceByUsernameOrderByUpdatedAtASC(@Param("username") String username, Pageable pageable);

    @Query(value = "SELECT w FROM WorkSpace w WHERE w.author.username = :username ORDER BY w.updated_at DESC")
    Page<WorkSpace> getAllWorkspaceByUsernameOrderByUpdatedAtDESC(@Param("username") String username, Pageable pageable);

    @Query(value = "SELECT w FROM WorkSpace w WHERE w.author.username = :username ORDER BY w.name ASC")
    Page<WorkSpace> getAllWorkspaceByUsernameOrderByNameASC(@Param("username") String username, Pageable pageable);

    @Query(value = "SELECT w FROM WorkSpace w WHERE w.author.username = :username ORDER BY w.name DESC")
    Page<WorkSpace> getAllWorkspaceByUsernameOrderByNameDESC(@Param("username") String username, Pageable pageable);

    @Query(value = "SELECT w FROM WorkSpace w WHERE w.author.username = :username AND w.favorite = true ORDER BY w.updated_at DESC")
    Page<WorkSpace> getAllWorkspaceByUsernameOrderByFavoriteDESC(@Param("username") String username, Pageable pageable);
    @Query(value = "SELECT w FROM WorkSpace w WHERE w.author.username = :username AND w.favorite = true ORDER BY w.updated_at ASC")
    Page<WorkSpace> getAllWorkspaceByUsernameOrderByFavoriteASC(@Param("username") String username, Pageable pageable);
}
