package com.RACTEC.CHATAPI.Service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Path UPLOAD_DIRECTORY = Paths.get("uploads")
            .toAbsolutePath()
            .normalize() ;

    private  static final Map<String,String> EXTENSIONS = Map.of(
            "image/jpeg" , ".jpg" ,
            "image/png" , ".png",
            "image/gif" , ".gif",
            "image/webp" , ".webp",
            "audio/mpeg" , ".mp3",
            "audio/wav" , ".wav",
            "audio/x-wav" , ".wav",
            "audio/ogg" , ".ogg",
            "audio/mp4" , ".m4a",
            "video/mp4" , ".mp4"

    ) ;

    public String validateFile(MultipartFile file){
        if(file == null || file.isEmpty()){return "file is empty";}
        String type = file.getContentType();
        if(type == null || !EXTENSIONS.containsKey(type))return "file is empty";
        long size = file.getSize();
        if(type.startsWith("image/") && size> 5 * 1024 * 1024)return "image size is too large max 55 mb is aloowed";
        if(type.startsWith("audio/") && size> 15 * 1024 * 1024)return "audio size is too large max 15 mb is aloowed";
        if(type.startsWith("video/") && size> 25 * 1024 * 1024)return "video size is too large max 25 mb is aloowed";
        return null ;
    }

    public String save(MultipartFile file) throws IOException {
        String extension = EXTENSIONS.get(file.getContentType());
        String savedName = UUID.randomUUID().toString() +   extension;
        Files.createDirectories(UPLOAD_DIRECTORY);
        file.transferTo(UPLOAD_DIRECTORY.resolve(savedName));
        return savedName;
    }

    public Resource load(String fileName) throws IOException {
        Path file = UPLOAD_DIRECTORY.resolve(fileName).normalize();
        if (!file.startsWith(UPLOAD_DIRECTORY) || !Files.isRegularFile(file)) {
            return null;
        }
        return new UrlResource(file.toUri());
    }
    public String contentTypeOf(String fileName) {
        for (Map.Entry<String, String> entry : EXTENSIONS.entrySet()) {
            if (fileName.endsWith(entry.getValue())) {
                return entry.getKey();
            }
        }
        return "application/octet-stream";
    }


}
