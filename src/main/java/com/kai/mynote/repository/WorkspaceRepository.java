package com.kai.mynote.repository;

import com.kai.mynote.entities.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkSpace, Long> {
}
