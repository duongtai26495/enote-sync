package com.kai.mynote.controller;


import com.kai.mynote.entities.Media;
import com.kai.mynote.util.AppConstants;
import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.Task;
import com.kai.mynote.entities.WorkSpace;
import com.kai.mynote.service.Impl.FileServiceImpl;
import com.kai.mynote.service.Impl.NoteServiceImpl;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.service.Impl.WorkspaceServiceImpl;
import com.kai.mynote.util.UserUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(AppConstants.API_PREFIX_V1+"note")
public class NoteController {
    @Autowired
    private FileServiceImpl fileService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private NoteServiceImpl noteService;
    @Autowired
    private WorkspaceServiceImpl workspaceService;
    @Autowired
    private UserUtil userUtil;

    private static final Logger logger = LogManager.getLogger(NoteController.class);

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseObject> getNoteById(@PathVariable Long id, Authentication authentication) {
        Note note = noteService.getNoteById(id);
        if (note != null
                && userUtil.isUserActive(authentication)
                && note.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())) {
            logger.info("User "+authentication.getName()+" get a note "+id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.NOTE, note)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
        );
    }


    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addNote(@RequestBody Note note, Authentication authentication) {
        if (note != null
                && userUtil.isUserActive(authentication)
                && workspaceService.getWorkspaceById(note.getWorkspace().getId()) != null) {
            WorkSpace workSpace = workspaceService.getWorkspaceById(note.getWorkspace().getId());
            if (workSpace != null && workSpace.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())) {
                note.setAuthor(userService.getUserForAuthor(authentication.getName()));
                logger.info("User "+authentication.getName()+" added a note: "+note.getName());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.NOTE + " " + AppConstants.CREATED, noteService.createNote(note))
                );
            }

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateNote(@RequestBody Note note, Authentication authentication) {
        Note currentNote = noteService.getNoteById(note.getId());
        String authorName = authentication.getName();
        if (currentNote != null
                && userUtil.isUserActive(authentication)
                && currentNote.getAuthor().getUsername().equalsIgnoreCase(authorName)) { //Check workspace muốn chuyển tới có phải của user ko

            WorkSpace workSpace = workspaceService.getWorkspaceById(note.getWorkspace().getId());

            if (workSpace.getAuthor().getUsername().equalsIgnoreCase(authorName) &&
                    workspaceService.getWorkspaceById(note.getWorkspace().getId()).getAuthor().getUsername().equals(authorName)) {


                Note createdNote = noteService.updateNote(note);
                logger.info("User "+authentication.getName()+" added a note: "+createdNote.getName());
                // Check workspace của note gửi lên có phải của user ko
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.NOTE + " " + AppConstants.UPDATED,createdNote)
                );

            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
        );
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ResponseObject> deleteNote(@PathVariable Long id, Authentication authentication) {
        Note currentNote = noteService.getNoteById(id);
        if (currentNote != null
                && userUtil.isUserActive(authentication)
                && currentNote.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())) {
            if (currentNote.getTasks().isEmpty()) {
                noteService.removeNoteById(id);

                logger.info("User "+authentication.getName()+" removed a note: "+id);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.NOTE + " " + AppConstants.REMOVED, null)
                );
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.NOTE + " have to empty to remove", null)
                );
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
        );
    }

    @GetMapping("/task/get/{id}")
    public ResponseEntity<ResponseObject> getTaskById(@PathVariable Long id,
                                                      Authentication authentication) {
        Task task = noteService.findTaskById(id);
        if (task != null
                && userUtil.isUserActive(authentication)
                && task.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())) {

            logger.info("User "+authentication.getName()+" get a task: "+id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.TASK, task)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
        );
    }


    @GetMapping("/tasks/{id}")
    public Page<Task> getAllTasksByNoteId(@PathVariable Long id,
                                          Authentication authentication,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = AppConstants.LAST_EDITED_DESC_VALUE) String sort) {
        if (userUtil.isUserActive(authentication)){
            if(page < 0) {page = 0;}
            if(size < 0) {size = 10;}
            logger.info("User "+authentication.getName()+" get tasks from note: "+id);
            return noteService.getAllTaskByNoteId(id, page, size, sort);
        }
        return null;
    }

    @PostMapping("/task/add")
    public ResponseEntity<ResponseObject> addTask(@RequestBody Task task,
                                                  Authentication authentication) {
        try {
            if (task != null
                    && userUtil.isUserActive(authentication)
                    && noteService.getNoteById(task.getNote().getId()) != null) {
                Note note = noteService.getNoteById(task.getNote().getId());
                if (note != null && note.getAuthor().getUsername().equals(authentication.getName())) {
                    task.setAuthor(userService.getUserForAuthor(authentication.getName()));
                    Task createdTask = noteService.createTask(task);
                    logger.info("User "+authentication.getName()+" added a task: " +createdTask.getId());
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.TASK + " " + AppConstants.CREATED, createdTask)
                    );
                }

            }
        }catch (Exception e){
            logger.error("Error: "+ e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
        );
    }

    @PutMapping("/task/update")
    public ResponseEntity<ResponseObject> updateTask(@RequestBody Task task,
                                                     Authentication authentication) {
        Task currentTask = noteService.findTaskById(task.getId());
        String authorName = authentication.getName();
        if (currentTask != null
                && userUtil.isUserActive(authentication)
                && authorName.equals(currentTask.getAuthor().getUsername())) {
            logger.info("User "+authentication.getName()+" updated a task:"+task.getId());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.TASK + " " + AppConstants.UPDATED, noteService.updateTask(task))
            );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
        );
    }

    @DeleteMapping("/task/remove/{id}")
    public ResponseEntity<ResponseObject> deleteTask(@PathVariable Long id,
                                                     Authentication authentication) {
        Task currentTask = noteService.findTaskById(id);

        if (currentTask != null
                && userUtil.isUserActive(authentication)
                && currentTask.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())) {
            noteService.removeTaskById(id);

            logger.info("User "+authentication.getName()+" removed a task:"+id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.TASK + " " + AppConstants.REMOVED, null)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
        );
    }



    @PostMapping("/upload/{id}")
    public ResponseEntity<ResponseObject> uploadImage(@RequestParam("f_image") MultipartFile file, @PathVariable String id, Authentication authentication) {
        try {
            // Kiểm tra kích thước tệp ảnh
            if(userUtil.isUserActive(authentication)) {
                long fileSize = file.getSize();
                if (fileSize > AppConstants.MAX_FILE_SIZE) {
                    logger.warn("User " + authentication.getName() + " uploaded a large file: " + fileSize);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
                    );
                }

                // Kiểm tra xem tệp có phải là ảnh không
                if (fileService.isImage(file)) {
                    logger.warn("User " + authentication.getName() + " uploaded not an image file");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
                    );
                }


                Note note = noteService.getNoteById(Long.parseLong(id));
                Media media = fileService.saveMedia(file, authentication.getName());
                String imageURL = media.getName();
                note.setFeatured_image(imageURL);
                // Trả về tên tệp ảnh
                noteService.updateNote(note);

                logger.warn("User " + authentication.getName() + " uploaded an image: " + imageURL);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.NOTE + " " + AppConstants.UPDATED, imageURL)
                );
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
            );
            // Trả về tên tệp ảnh
        } catch (IOException e) {
            logger.error("Upload failed",e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(AppConstants.FAILURE_STATUS, AppConstants.BAD_REQUEST_MSG, null)
            );
        }
    }

    @GetMapping("search")
    private Page<Note> searchNote(@RequestParam(name = "name") String name,
                                  @RequestParam(defaultValue = "12") int size,
                                  @RequestParam(defaultValue = "0") int page,
                                  Authentication authentication) {
        if(userUtil.isUserActive(authentication)) {
            if(page < 0) {page = 0;}
            if(size < 0) {size = 10;}
            logger.warn("User " + authentication.getName() + " searched " + name);
            return noteService.findNoteByName(name, authentication.getName(), size, page);
        }
        return null;
    }
}
