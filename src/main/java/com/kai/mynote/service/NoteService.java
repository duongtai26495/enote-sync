package com.kai.mynote.service;

import com.kai.mynote.entities.Note;

public interface NoteService {
    Note create(Note note);

    Note update(Note note);

    void removeById (Long id);

    Note getNoteById(Long id);
}
