package com.kai.mynote.service.Impl;

import com.kai.mynote.entities.Note;
import com.kai.mynote.entities.WorkSpace;
import com.kai.mynote.repository.NoteRepository;
import com.kai.mynote.repository.WorkspaceRepository;
import com.kai.mynote.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.kai.mynote.assets.AppConstants.TIME_FORMAT;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public WorkSpace create(WorkSpace workSpace) {
        if (workSpace.getName() == null){
            workSpace.setName("Unnamed workspace");
        }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        workSpace.setCreated_at(dateFormat.format(date));
        workSpace.setNotes(new ArrayList<>());
        workSpace.setUpdated_at(dateFormat.format(date));
        return workspaceRepository.save(workSpace);
    }

    @Override
    public WorkSpace update(WorkSpace workSpace) {
        if (workspaceRepository.findById(workSpace.getId()).isPresent()) {
            WorkSpace currentWs = workspaceRepository.findById(workSpace.getId()).get();
            if (workSpace.getName() != null && !currentWs.getName().equalsIgnoreCase(workSpace.getName()))
            {currentWs.setName(workSpace.getName());}

            if (workSpace.getFeatured_image() != null && !currentWs.getFeatured_image().equalsIgnoreCase(workSpace.getFeatured_image()))
            {currentWs.setFeatured_image(workSpace.getFeatured_image());}
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
            currentWs.setUpdated_at(dateFormat.format(date));
            return workspaceRepository.save(currentWs);
        }
        return null;
    }

    @Override
    public void removeById(Long id) {
        if(workspaceRepository.existsById(id))
            workspaceRepository.deleteById(id);
    }

    @Override
    public WorkSpace getWorkspaceById(Long id) {
        return workspaceRepository.findById(id).isPresent() ? workspaceRepository.findById(id).get() : null;
    }

    @Override
    public Page<Note> getAllNoteByWorkspaceId(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return noteRepository.findByWorkspaceId(id, pageable);
    }
}
