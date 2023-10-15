package com.kai.mynote.service.Impl;

import com.kai.mynote.entities.Media;
import com.kai.mynote.repository.MediaRepository;
import com.kai.mynote.repository.UserRepository;
import com.kai.mynote.util.AppConstants;
import com.kai.mynote.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static com.kai.mynote.util.AppConstants.TIME_PATTERN;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${upload.path}")
    private String uploadPath;
    @Override
    public String storeNoteImage(MultipartFile file) throws IOException {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String generatedFileName = UUID.randomUUID().toString().replace("-", "");
        generatedFileName = generatedFileName + "." + fileExtension;

        Path storageFolder = Paths.get(uploadPath);
        // Tạo thư mục nếu chưa tồn tại
            try {
                Files.createDirectories(storageFolder);
            }catch (Exception e){
                throw new RuntimeException(AppConstants.CANNOT_INITIALIZE_STORAGE,e);
            }
        Path destinationFilePath = storageFolder.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();
        if (!destinationFilePath.getParent().equals(storageFolder.toAbsolutePath())){
            throw new RuntimeException(AppConstants.CANNOT_STORE_OUTSIDE);
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
    public boolean isImage(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        assert fileExtension != null;
        return !Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }

    @Override
    public String removeImageByName(Media media) {
        try {
                if (media != null) {
                    Path storageFolder = Paths.get(uploadPath+media.getName());
                    File file = storageFolder.toFile();
                    if (file.exists()) {
                        if (file.delete()) {
                            mediaRepository.deleteById(media.getId());
                            return "File removed";
                        } else {
                            return "File cannot remove";
                        }
                    } else {
                        mediaRepository.deleteById(media.getId());
                        return "File do not exist";
                    }
                }
            }catch (Exception e) {
                logger.error("Error ",e);
            return "Error "+e;
            }
        return null;
    }

    @Override
    public Media saveMedia(MultipartFile file, String username) throws IOException {
            String media_name = storeNoteImage(file);
            if(media_name != null){
                Media media = new Media();
                Date date = new Date();
                media.setName(media_name);
                SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_PATTERN);
                media.setCreated_at(dateFormat.format(date));
                media.setAuthor(userRepository.findFirstByUsername(username));
                return mediaRepository.save(media);
            }else {
                return null;
            }
    }

    @Override
    public Media getMediaByName(String name) {
        return mediaRepository.getMediaByName(name);
    }
}
