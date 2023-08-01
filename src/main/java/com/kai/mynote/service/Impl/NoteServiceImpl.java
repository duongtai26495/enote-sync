package com.kai.mynote.service.Impl;

import com.kai.mynote.entities.Note;
import com.kai.mynote.repository.NoteRepository;
import com.kai.mynote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public Note create(Note note) {
        if (note.getContent() == null){
            note.setContent("");
        }
        return noteRepository.save(note);
    }

    @Override
    public Note update(Note note) {
        if (noteRepository.findById(note.getId()).isPresent()) {
            Note currentNote = noteRepository.findById(note.getId()).get();

            if (note.getContent() != null && !currentNote.getContent().equalsIgnoreCase(note.getContent()))
            {currentNote.setContent(note.getContent());}

            if (note.getFeatured_image() != null && !currentNote.getFeatured_image().equalsIgnoreCase(note.getFeatured_image()))
            {currentNote.setFeatured_image(note.getFeatured_image());}

            if (note.getWorkspace() !=null && !currentNote.getWorkspace().equals(note.getWorkspace()))
            {currentNote.setWorkspace(note.getWorkspace());}

            currentNote.setEnabled(note.isEnabled());
            currentNote.setDone(note.isDone());

            noteRepository.save(currentNote);
        }
        return null;
    }

    @Override
    public void removeById(Long id) {
        if (noteRepository.findById(id).isPresent())
        {noteRepository.deleteById(id);}
    }

    @Override
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).isPresent() ? noteRepository.findById(id).get() : null ;
    }
}
