package com.kai.mynote.service;

import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {
    Note create(Note note);

    Note update(Note note);

    void removeNoteById (Long id);

    Note getNoteById(Long id);

    Task createTask(Task task);

    Task findTaskById(Long id);

    Task updateTask(Task task);

    void removeTaskById(Long id);

    Page<Task> getAllTaskByNoteId(Long id, int page, int size);
}
