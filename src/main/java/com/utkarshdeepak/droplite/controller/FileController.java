package com.utkarshdeepak.droplite.controller;

import com.utkarshdeepak.droplite.DTO.InitialFileRequest;
import com.utkarshdeepak.droplite.entity.FileEntity;
import com.utkarshdeepak.droplite.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileRepository fileRepository;


    @PostMapping("/initiate")
    public ResponseEntity<Map<String, Object>> initiateFile(@RequestBody InitialFileRequest request) {
        FileEntity file = new FileEntity();
        file.setName(request.getFileName());
        file.setChunkCount(request.getTotalChuckCount());

        FileEntity savedFile = fileRepository.save(file);

        Map<String, Object> response = new HashMap<>();
        response.put("fileId", savedFile.getId());
        response.put("status", "initiated");
        response.put("message", "Upload initiated successfully.");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FileEntity>> getAllFiles() {
        List<FileEntity> files = fileRepository.findAll();
        return ResponseEntity.ok(files);
    }

    @GetMapping("{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long fileId) {
        FileEntity file = fileRepository.findById(fileId).orElseThrow();
        Path filePath = Path.of(file.getPath());

        Resource resource = new FileSystemResource(filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "uttt" + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
