package com.kai.mynote.controller;

import com.kai.mynote.util.AppConstants;
import com.kai.mynote.dto.ResponseObject;
import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.WorkSpace;
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

@RestController
@RequestMapping("/workspace")
public class WorkSpaceController {

    @Autowired
    private WorkspaceServiceImpl workspaceService;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserUtil userUtil;

    private static final Logger logger = LogManager.getLogger(WorkSpaceController.class);

    @GetMapping("/all")
    public Page<WorkSpace> getWsByUsername(Authentication authentication,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = AppConstants.LAST_EDITED_DESC_VALUE) String sort){
        if (userUtil.isUserActive(authentication)) {
            if(page < 0) {page = 0;}
            if(size < 0) {size = 10;}

            logger.info("User " + authentication.getName() + " just get all workspaces by " + sort );
            return userService.getAllWorkspace(authentication.getName(), page, size, sort);
        }
        return null;
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<ResponseObject> getInfoWorkspace(@PathVariable Long id, Authentication authentication){

        WorkSpace workSpace = workspaceService.getWorkspaceById(id);
        if(userUtil.isUserActive(authentication)
                && authentication.getName().equalsIgnoreCase(workSpace.getAuthor().getUsername())){
            logger.info("User "+authentication.getName()+" just get info workspace by id "+id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.WORKSPACE + " " + AppConstants.CREATED, workSpace)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,"Bad request",null)
        );
    }

    @GetMapping("/get/{id}")
    public Page<Note> getNotesByWorkspaceId(@PathVariable Long id, Authentication authentication,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "30") int size,
                                            @RequestParam(defaultValue = AppConstants.LAST_EDITED_DESC_VALUE) String sort) {
        WorkSpace workSpace = workspaceService.getWorkspaceById(id);
        if (userUtil.isUserActive(authentication)
        && authentication.getName().equalsIgnoreCase(workSpace.getAuthor().getUsername())){
            if(page < 0) {page = 0;}
            if(size < 0) {size = 10;}
            logger.info("User "+authentication.getName()+" just get workspace by id "+id);
            return workspaceService.getAllNoteByWorkspaceId(id, page, size, sort);
        }
        return null;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addWs(@RequestBody(required = false) WorkSpace workSpace, Authentication authentication){
            if (workSpace == null){
                workSpace = new WorkSpace();
            }
            if (userUtil.isUserActive(authentication)) {
                workSpace.setAuthor(userService.getUserForAuthor(authentication.getName()));

                logger.info("User " + authentication.getName() + " just added a workspace");
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS, AppConstants.WORKSPACE + " " + AppConstants.CREATED, workspaceService.create(workSpace))
                );
            }
            return null;
    }


    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateWs(@RequestBody WorkSpace workSpace, Authentication authentication){
        WorkSpace ws = workspaceService.getWorkspaceById(workSpace.getId());
        if (ws != null
                && userUtil.isUserActive(authentication)
                && ws.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){

            logger.info("User "+authentication.getName()+" just updated a workspace "+workSpace.getId());
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
        if (ws != null
                && userUtil.isUserActive(authentication)
                && ws.getAuthor().getUsername().equalsIgnoreCase(authentication.getName())){
            if (ws.getNotes().isEmpty()){
                workspaceService.removeById(id);

                logger.info("User "+authentication.getName()+" just removed a workspace "+id);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(AppConstants.SUCCESS_STATUS,AppConstants.WORKSPACE +" "+AppConstants.REMOVED, null)
                );
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject(AppConstants.FAILURE_STATUS,AppConstants.WORKSPACE+" have to empty to remove",null)
                );
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(AppConstants.FAILURE_STATUS,"Bad request",null)
        );
    }
}
