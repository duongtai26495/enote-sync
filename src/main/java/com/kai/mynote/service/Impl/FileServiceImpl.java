package com.kai.mynote.service.Impl;

import com.kai.mynote.assets.AppConstants;
import com.kai.mynote.dto.UserDTO;
import com.kai.mynote.dto.UserUpdateDTO;
import com.kai.mynote.entities.User;
import com.kai.mynote.service.FileService;
import org.apache.catalina.authenticator.SpnegoAuthenticator;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${upload.path}")
    private String uploadPath;
    @Override
    public String storeNoteImage(MultipartFile file) throws IOException {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String generatedFileName = UUID.randomUUID().toString().replace("-", "");
        generatedFileName = generatedFileName + "." + fileExtension;

        System.out.println(generatedFileName);
        Path storageFolder = Paths.get(uploadPath);
        // Tạo thư mục nếu chưa tồn tại
            try {
                Files.createDirectories(storageFolder);
            }catch (Exception e){
                throw new RuntimeException(AppConstants.CANNOT_INITIALIZE_STORAGE,e);
            }
        Path destinationFilePath = storageFolder.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();
        if (!destinationFilePath.getParent().equals(storageFolder.toAbsolutePath())){
            throw new RuntimeException(AppConstants.CANNOT_STORE_OUSIDE);
        }
        // Lưu ảnh vào thư mục upload
        try(InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream,destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return generatedFileName;
    }

    @Override
    public String storeProfileImage(MultipartFile file, String username) {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getImage(String fileName) {
        Path storageFolder = Paths.get(uploadPath);
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()){
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(bytes);
            }else{
                return ResponseEntity.noContent().build();
            }
        }catch (IOException e){
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    public ResponseEntity<byte[]> getProfileImage(String fileName) {
        return null;
    }

    @Override
    public void deleteFile() {

    }

    @Override
    public boolean isImage(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        assert fileExtension != null;
        return Arrays.asList(new String[] {"png","jpg","jpeg","bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }
}
