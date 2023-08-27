package com.kai.mynote.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {
    public String storeNoteImage(MultipartFile file) throws IOException;
    public String storeProfileImage(MultipartFile file, String username);
    public ResponseEntity<byte[]> getImage(String fileName);
    public ResponseEntity<byte[]> getProfileImage(String fileName);
    public void deleteFile();
    public boolean isImage(MultipartFile file);
}
