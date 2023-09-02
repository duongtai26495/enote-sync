package com.kai.mynote.service;

import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.WorkSpace;
import org.springframework.data.domain.Page;

public interface WorkspaceService {

    WorkSpace create(WorkSpace workSpace);

    WorkSpace update(WorkSpace workSpace);

    void removeById (Long id);

    WorkSpace getWorkspaceById(Long id);

    Page<Note> getAllNoteByWorkspaceId(Long id, int page, int size, String sort);


}
