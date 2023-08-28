package com.kai.mynote.service;

import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.Task;
import org.springframework.data.domain.Page;

public interface NoteService {
    Note createNote(Note note);

    Note updateNote(Note note);

    void removeNoteById (Long id);

    Note getNoteById(Long id);

    Task createTask(Task task);

    Task findTaskById(Long id);

    Task updateTask(Task task);

    void removeTaskById(Long id);

    Page<Task> getAllTaskByNoteId(Long id, int page, int size);
}
