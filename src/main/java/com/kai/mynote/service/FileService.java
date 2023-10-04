package com.kai.mynote.service;

import com.kai.mynote.entities.Media;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    public String storeNoteImage(MultipartFile file) throws IOException;
    public String storeProfileImage(MultipartFile file, String username);
    public ResponseEntity<byte[]> getImage(String fileName);
    public ResponseEntity<byte[]> getProfileImage(String fileName);
    public boolean isImage(MultipartFile file);
    public String removeImageByName(Media media);
    public Media saveMedia(MultipartFile file, String username) throws IOException;
    public Media getMediaByName(String name);
}
