package com.kai.mynote.service.Impl;

import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.Task;
import com.kai.mynote.entities.Type;
import com.kai.mynote.repository.NoteRepository;
import com.kai.mynote.repository.TaskRepository;
import com.kai.mynote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Note create(Note note) {
        if (note.getName() == null){
            note.setName("Unnamed note");
        }
        return noteRepository.save(note);
    }

    @Override
    public Note update(Note note) {
        if (noteRepository.findById(note.getId()).isPresent()) {
            Note currentNote = noteRepository.findById(note.getId()).get();

            if (note.getName() != null && !currentNote.getName().equalsIgnoreCase(note.getName()))
            {currentNote.setName(note.getName());}

            if (note.getFeatured_image() != null && !currentNote.getFeatured_image().equalsIgnoreCase(note.getFeatured_image()))
            {currentNote.setFeatured_image(note.getFeatured_image());}

            if (note.getWorkspace() !=null && !currentNote.getWorkspace().equals(note.getWorkspace()))
            {currentNote.setWorkspace(note.getWorkspace());}

            if (!Double.isNaN(note.getProgress())){
                currentNote.setProgress(note.getProgress());}

            currentNote.setEnabled(note.isEnabled());
            currentNote.setDone(note.isDone());


            return noteRepository.save(currentNote);
        }
        return null;
    }

    @Override
    public void removeNoteById(Long id) {
        if (noteRepository.findById(id).isPresent())
        {noteRepository.deleteById(id);}
    }


    @Override
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).isPresent() ? noteRepository.findById(id).get() : null ;
    }

    @Override
    public Task createTask(Task task) {
        if (task.getContent() == null)
            task.setContent("Unnamed task");
        if (task.getType() == null){
            task.setType(Type.NOTE);
        }
       return taskRepository.save(task);
    }

    @Override
    public Task findTaskById(Long id) {
            return taskRepository.findById(id).isPresent() ? taskRepository.findById(id).get() : null;
    }

    @Override
    public Task updateTask(Task task) {
        if (taskRepository.findById(task.getId()).isPresent()){
            Task currentTask = taskRepository.findById(task.getId()).get();
            if (task.getContent()!=null && !task.getContent().equals(currentTask.getContent())){
                currentTask.setContent(task.getContent());
            }
            if (task.getType()!=null && !task.getType().equals(currentTask.getType())){
                currentTask.setType(task.getType());
            }
            currentTask.setEnabled(task.isEnabled());
            currentTask.setDone(task.isDone());
            return taskRepository.save(currentTask);
        }
        return null;
    }

    @Override
    public void removeTaskById(Long id) {
        if (taskRepository.findById(id).isPresent()){
            taskRepository.deleteById(id);
        }
    }

    @Override
    public Page<Task> getAllTaskByNoteId(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByNoteId(id, pageable);
    }
}
