package com.RACTEC.CHATAPI.Controller;


import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileController {

private static final Path UPLOAD_DIRECTORY = Paths.get("uploads")
        .toAbsolutePath()
        .normalize() ;

private  static final Map<String,String> EXTENSIONS = Map.of(
        "image/jpeg" , ".jpg" ,
        "image/png" , "png",
        "image/gif" , ".gif",
        "image/webp" , ".webp"

) ;

@PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {

    if (file.isEmpty()) {return ResponseEntity.badRequest().body("File is empty");}

    String extension = EXTENSIONS.get(file.getContentType());
    if (extension == null) {return ResponseEntity.badRequest().body("File type is not allowed only jpegpng gif webp are allowec");}

        String savedName = UUID.randomUUID().toString() + extension;

    Files.createDirectories(UPLOAD_DIRECTORY);
    file.transferTo(UPLOAD_DIRECTORY.resolve(savedName));

    return ResponseEntity.ok(Map.of("filename" , savedName  )) ;


}


    }

