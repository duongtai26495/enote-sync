package com.kai.mynote.service.Impl;

import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.Task;
import com.kai.mynote.entities.Type;
import com.kai.mynote.repository.NoteRepository;
import com.kai.mynote.repository.TaskRepository;
import com.kai.mynote.service.NoteService;
import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.kai.mynote.assets.AppConstants.TIME_FORMAT;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;


    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Note createNote(Note note) {
        if (note.getName() == null){
            note.setName("Unnamed note");
        }
        note.setTasks(new ArrayList<>());
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        note.setCreated_at(dateFormat.format(date));
        note.setUpdated_at(dateFormat.format(date));
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(Note note) {
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
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
            currentNote.setUpdated_at(dateFormat.format(date));

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
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        if (task.getContent() == null)
            task.setContent("Unnamed task");
        if (task.getType() == null){
            task.setType(Type.NOTE);
        }

        task.setCreated_at(dateFormat.format(date));
        task.setUpdated_at(dateFormat.format(date));
        Task createdTask = taskRepository.save(task);
        if(createdTask.getType() == Type.CHECK){
            progressCalc(task.getNote().getId());
        }
        return createdTask;
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
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
            currentTask.setUpdated_at(dateFormat.format(date));
            Task taskDone = taskRepository.save(currentTask);
            Note note = noteRepository.findNoteById(currentTask.getNote().getId());
            note.setProgress(progressCalc(note.getId()));
            updateNote(note);
            return taskDone;
        }
        return null;
    }


    @Override
    public void removeTaskById(Long id) {

        if (taskRepository.findById(id).isPresent()){
            Task task = taskRepository.findById(id).get();
            taskRepository.deleteById(id);
            if (task.getType().equals(Type.CHECK)){
                progressCalc(task.getNote().getId());
            }
        }
    }

    @Override
    public Page<Task> getAllTaskByNoteId(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByNoteId(id, pageable);
    }

    private double progressCalc(long noteId){
        List<Task> tasks = taskRepository.findByNoteId(noteId);
        long tasksNote = tasks.stream().filter(item -> item.getType() == Type.CHECK).count();
        long isDoneTask = tasks.stream().filter(item -> item.getType() == Type.CHECK && item.isDone()).count();
        return ((double) isDoneTask / tasksNote) * 100;
    }

}
