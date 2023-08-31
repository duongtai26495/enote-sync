package com.kai.mynote.controller;


import com.kai.mynote.util.AppConstants;
import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.Task;
import com.kai.mynote.entities.WorkSpace;
import com.kai.mynote.service.Impl.FileServiceImpl;
import com.kai.mynote.service.Impl.NoteServiceImpl;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.service.Impl.WorkspaceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private FileServiceImpl fileService;
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
                        new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.NOTE +" "+AppConstants.CREATED, noteService.createNote(note))
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
                        new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.NOTE+" "+AppConstants.UPDATED, noteService.updateNote(note))
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
                if (currentNote.getTasks().isEmpty()){
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

    @GetMapping("/task/get/{id}")
    public ResponseEntity<ResponseObject> getTaskById(@PathVariable Long id,
                                                      Authentication authentication){
        Task task = noteService.findTaskById(id);
        if (task != null &&
                task.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
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
                task.setAuthor(userService.getUserForAuthor(authentication.getName()));
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
            if (currentTask != null && authorName.equals(currentTask.getAuthor().getUsername())) {
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

        if (currentTask != null && currentTask.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
            noteService.removeTaskById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.TASK +" "+AppConstants.REMOVED, null)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
        );
    }

    @GetMapping("/sort_value")
    public List<Map<String, String>> getSortValue(){
        List<Map<String, String>> sortValue = new ArrayList<>();

        sortValue.add(new HashMap<String, String>() {{
            put(AppConstants.LAST_EDITED_DESC_LABEL, AppConstants.LAST_EDITED_DESC_VALUE);
        }});
        sortValue.add(new HashMap<String, String>() {{
            put(AppConstants.LAST_EDITED_ASC_LABEL, AppConstants.LAST_EDITED_ASC_VALUE);
        }});
        sortValue.add(new HashMap<String, String>() {{
            put(AppConstants.CREATED_AT_DESC_LABEL, AppConstants.CREATED_AT_DESC_VALUE);
        }});
        sortValue.add(new HashMap<String, String>() {{
            put(AppConstants.CREATED_AT_ASC_LABEL, AppConstants.CREATED_AT_ASC_VALUE);
        }});
        sortValue.add(new HashMap<String, String>() {{
            put(AppConstants.A_Z_LABEL, AppConstants.A_Z_VALUE);
        }});
        sortValue.add(new HashMap<String, String>() {{
            put(AppConstants.Z_A_LABEL, AppConstants.Z_A_VALUE);
        }});

        return sortValue;
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<ResponseObject> uploadImage(@RequestParam("f_image") MultipartFile file, @PathVariable String id) {
        try {
            // Kiểm tra kích thước tệp ảnh
            long fileSize = file.getSize();
            if (fileSize > AppConstants.MAX_FILE_SIZE) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
                );
            }

            // Kiểm tra xem tệp có phải là ảnh không
            if (!fileService.isImage(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
                );
            }


            Note note = noteService.getNoteById(Long.parseLong(id));
            String imageURL = fileService.storeNoteImage(file);
            note.setFeatured_image(imageURL);
            // Trả về tên tệp ảnh
            noteService.updateNote(note);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.NOTE +" "+AppConstants.UPDATED, imageURL)
            );
            // Trả về tên tệp ảnh
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.BAD_REQUEST_MSG,null)
            );
        }
    }
}
