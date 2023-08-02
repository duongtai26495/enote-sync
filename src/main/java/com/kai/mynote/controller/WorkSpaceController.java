package com.kai.mynote.controller;

import com.kai.mynote.assets.AppConstants;
import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.User;
import com.kai.mynote.entities.WorkSpace;
import com.kai.mynote.service.Impl.UserServiceImpl;
import com.kai.mynote.service.Impl.WorkspaceServiceImpl;
import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspace")
public class WorkSpaceController {

    @Autowired
    private WorkspaceServiceImpl workspaceService;

    @Autowired
    private UserServiceImpl userService;


    @GetMapping("")
    public Page<WorkSpace> getWsByUsername(Authentication authentication,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size){
            return userService.getAllWorkspace(authentication.getName(), page, size);
    }

    @GetMapping("/{id}")
    public Page<Note> getNotesByWorkspaceId(@PathVariable Long id, Authentication authentication,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "30") int size){
        WorkSpace workSpace = workspaceService.getWorkspaceById(id);
        if (authentication.getName().equalsIgnoreCase(workSpace.getAuthor().getUsername())){
            return workspaceService.getAllNoteByWorkspaceId(id, page, size);
        }
        return null;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addWs(@RequestBody(required = false) WorkSpace workSpace, Authentication authentication){
            if (workSpace == null){
                workSpace = new WorkSpace();
            }
            workSpace.setAuthor(userService.getUserForAuthor(authentication.getName()));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.WORKSPACE+" "+AppConstants.CREATED, workspaceService.create(workSpace))
            );

    }


    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateWs(@RequestBody WorkSpace workSpace, Authentication authentication){
        WorkSpace ws = workspaceService.getWorkspaceById(workSpace.getId());
        if (ws != null && ws.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.WORKSPACE +" "+AppConstants.UPDATED, workspaceService.update(workSpace))
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,"Bad request",null)
        );
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ResponseObject> deleteWs(@PathVariable Long id, Authentication authentication){
        WorkSpace ws = workspaceService.getWorkspaceById(id);
        if (ws != null && ws.getNotes().size() == 0){
            if (ws.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
                workspaceService.removeById(id);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.WORKSPACE +" "+AppConstants.REMOVED, null)
                );
            }
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.WORKSPACE+" have to empty to remove",null)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,"Bad request",null)
        );
    }
}
