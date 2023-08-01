package com.kai.mynote.controller;


import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.WorkSpace;
import com.kai.mynote.service.Impl.NoteServiceImpl;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.service.Impl.WorkspaceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/note/")
public class NoteController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private NoteServiceImpl noteService;

    @Autowired
    private WorkspaceServiceImpl workspaceService;

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getNoteById(@PathVariable Long id, Authentication authentication){
        Note note = noteService.getNoteById(id);
        if (note != null &&
            note.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("SUCCESS","Note information", note)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("FAIL","Bad request",null)
        );
    }


    @PostMapping("add")
    public ResponseEntity<ResponseObject> addNote(@RequestBody Note note, Authentication authentication){
        if (note != null && workspaceService.getWorkspaceById(note.getWorkspace().getId()) != null){
            WorkSpace workSpace = workspaceService.getWorkspaceById(note.getWorkspace().getId());
            if (workSpace.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
                note.setAuthor(userService.getUserForAuthor(authentication.getName()));
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("SUCCESS","Note created", noteService.create(note))
                );
            }

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("FAIL","Bad request",null)
        );
    }

    @PutMapping("update")
    public ResponseEntity<ResponseObject> updateNote(@RequestBody Note note, Authentication authentication){
        Note currentNote = noteService.getNoteById(note.getId());
        String authorName = authentication.getName();
        if (currentNote != null && currentNote.getAuthor().getUsername().equalsIgnoreCase(authorName)){ //Check workspace muốn chuyển tới có phải của user ko

            WorkSpace workSpace = workspaceService.getWorkspaceById(note.getWorkspace().getId());

            if (workSpace.getAuthor().getUsername().equalsIgnoreCase(authorName) &&
                    workspaceService.getWorkspaceById(note.getWorkspace().getId()).getAuthor().getUsername().equals(authorName)) { // Check workspace của note gửi lên có phải của user ko
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("SUCCESS", "Note updated", noteService.update(note))
                );
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("FAIL","Bad request",null)
        );
    }

    @DeleteMapping("remove/{id}")
    public ResponseEntity<ResponseObject> deleteNote(@PathVariable Long id, Authentication authentication){
        Note currentNote = noteService.getNoteById(id);

            if (currentNote.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
                noteService.removeById(id);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("SUCCESS","Note removed", null)
                );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("FAIL","Bad request",null)
        );
    }

}
