package com.kai.mynote.controller;


import com.kai.mynote.assets.AppConstants;
import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.Task;
import com.kai.mynote.entities.WorkSpace;
import com.kai.mynote.service.Impl.NoteServiceImpl;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.service.Impl.WorkspaceServiceImpl;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private NoteServiceImpl noteService;

    @Autowired
    private WorkspaceServiceImpl workspaceService;


    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseObject> getNoteById(@PathVariable Long id, Authentication authentication){
        Note note = noteService.getNoteById(id);
        if (note != null &&
            note.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.NOTE, note)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
        );
    }


    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addNote(@RequestBody Note note, Authentication authentication){
        if (note != null && workspaceService.getWorkspaceById(note.getWorkspace().getId()) != null){
            WorkSpace workSpace = workspaceService.getWorkspaceById(note.getWorkspace().getId());
            if (workSpace !=null && workSpace.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
                note.setAuthor(userService.getUserForAuthor(authentication.getName()));
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.NOTE +" "+AppConstants.CREATED, noteService.create(note))
                );
            }

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateNote(@RequestBody Note note, Authentication authentication){
        Note currentNote = noteService.getNoteById(note.getId());
        String authorName = authentication.getName();
        if (currentNote != null && currentNote.getAuthor().getUsername().equalsIgnoreCase(authorName)){ //Check workspace muốn chuyển tới có phải của user ko

            WorkSpace workSpace = workspaceService.getWorkspaceById(note.getWorkspace().getId());

            if (workSpace.getAuthor().getUsername().equalsIgnoreCase(authorName) &&
                    workspaceService.getWorkspaceById(note.getWorkspace().getId()).getAuthor().getUsername().equals(authorName)) { // Check workspace của note gửi lên có phải của user ko
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.NOTE+" "+AppConstants.UPDATED, noteService.update(note))
                );
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
        );
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ResponseObject> deleteNote(@PathVariable Long id, Authentication authentication){
        Note currentNote = noteService.getNoteById(id);
            if (currentNote != null && currentNote.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())) {
                if (currentNote.getTasks().size() == 0){
                noteService.removeNoteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.NOTE + " " + AppConstants.REMOVED, null)
                );
                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.NOTE+" have to empty to remove",null)
                    );
                }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
        );
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<ResponseObject> getTaskById(@PathVariable Long id,
                                                      Authentication authentication){
        Task task = noteService.findTaskById(id);
        if (task != null &&
                task.getNote().getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.TASK, task)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
        );
    }


    @GetMapping("/tasks/{id}")
    public Page<Task> getAllTasksByNoteId (@PathVariable Long id,
                                            Authentication authentication,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size){
        return noteService.getAllTaskByNoteId(id, page, size);
    }

    @PostMapping("/task/add")
    public ResponseEntity<ResponseObject> addTask(@RequestBody Task task,
                                                  Authentication authentication){
        if (task != null && noteService.getNoteById(task.getNote().getId()) != null){
            Note note = noteService.getNoteById(task.getNote().getId());
            if (note !=null && note.getAuthor().getUsername().equals(authentication.getName())){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.TASK +" "+AppConstants.CREATED, noteService.createTask(task))
                );
            }

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
        );
    }

    @PutMapping("/task/update")
    public ResponseEntity<ResponseObject> updateTask(@RequestBody Task task,
                                                     Authentication authentication){
        Task currentTask = noteService.findTaskById(task.getId());
        String authorName = authentication.getName();
            if (currentTask != null && authorName.equals(currentTask.getNote().getAuthor().getUsername())) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.TASK + " " + AppConstants.UPDATED, noteService.updateTask(task))
                );
            }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
        );
    }

    @DeleteMapping("/task/remove/{id}")
    public ResponseEntity<ResponseObject> deleteTask(@PathVariable Long id,
                                                     Authentication authentication){
        Task currentTask = noteService.findTaskById(id);

        if (currentTask != null && currentTask.getNote().getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
            noteService.removeTaskById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.TASK +" "+AppConstants.REMOVED, null)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
        );
    }
}
