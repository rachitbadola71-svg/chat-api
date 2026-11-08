package com.RACTEC.CHATAPI.Controller;

import com.RACTEC.CHATAPI.Service.FileStorageService;                    // The desk that handles files. Type the imports yourself.
import org.springframework.core.io.Resource;                             // The type Spring uses to send a file back.
import org.springframework.http.MediaType;                               // The "kind of file" label (image/png...).
import org.springframework.http.ResponseEntity;                          // Reply with a status code and a body.
import org.springframework.web.bind.annotation.GetMapping;               // Marks a method that answers GET requests.
import org.springframework.web.bind.annotation.PathVariable;             // Takes a blank out of the URL.
import org.springframework.web.bind.annotation.PostMapping;              // Marks a method that answers POST requests.
import org.springframework.web.bind.annotation.RequestMapping;           // Starting address for the class.
import org.springframework.web.bind.annotation.RequestParam;             // Picks one field out of the request.
import org.springframework.web.bind.annotation.RestController;           // Answers requests with data.
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;
    public FileController(FileStorageService fileStorageService) {this.fileStorageService = fileStorageService;}

@PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {

    String error = fileStorageService.validateFile(file) ;
    if(error!=null){return ResponseEntity.badRequest().body(error);}

    String savedName = fileStorageService.save(file);
    return ResponseEntity.ok(Map.of("fileName",savedName));

    }
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> view(@PathVariable String fileName) throws IOException {
        Resource resource = fileStorageService.load(fileName);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileStorageService.contentTypeOf(fileName)))   // Tell the receiver the file type
                .body(resource);
    }
    }

