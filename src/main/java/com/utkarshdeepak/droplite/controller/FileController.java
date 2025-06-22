package com.utkarshdeepak.droplite.controller;

import com.utkarshdeepak.droplite.DTO.InitialFileRequest;
import com.utkarshdeepak.droplite.entity.FileChunkEntity;
import com.utkarshdeepak.droplite.entity.FileEntity;
import com.utkarshdeepak.droplite.repository.FileChunkRepository;
import com.utkarshdeepak.droplite.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileRepository fileRepository;
    private final FileChunkRepository fileChunkRepository;

    @Value("${file.storage.root}")
    private String storageRoot;
    @Value("${file.chunk.directory}")
    private String chunkDirectory;


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

    @Transactional
    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadChunk(@RequestParam("fileId") Long fileId,
                                                            @RequestParam("chunkIndex") int chunkIndex,
                                                            @RequestParam("chunk") MultipartFile chunk) {

        FileEntity file = fileRepository.findById(fileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        List<FileChunkEntity> chunkList = fileChunkRepository.findAllByFileIdOrderByChunkIndex(fileId);

        //check if we have received duplicate chunk
//        boolean isDuplictae = chunkList.stream().anyMatch(chunkEntity -> chunkEntity.getChunkIndex() == chunkIndex);

        FileChunkEntity isDuplictae = fileChunkRepository.findFileEntityByChunkIndexAndFileId(chunkIndex,fileId);
        long chunkCount = fileChunkRepository.countByFileId(fileId);

        if(isDuplictae != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        try {

        //if last chunk then donot store, directly combine and store in files table, and remove all chunks for given fileId
        if(++chunkCount == file.getChunkCount()){
            List<FileChunkEntity> chunkList = fileChunkRepository.findAllByFileIdOrderByChunkIndex(fileId);

            Path finalFilePath = Paths.get(storageRoot, String.valueOf(file.getId()));
            Files.createDirectories(finalFilePath.getParent());

            // Combine chunks
            try (OutputStream os = Files.newOutputStream(finalFilePath)) {
                for (FileChunkEntity currentChunk : chunkList) {
                    Files.copy(Paths.get(currentChunk.getChunkPath()), os);
                }
                os.write(chunk.getBytes());
            }

            file.setPath(finalFilePath.toString());
            fileRepository.save(file);
            fileChunkRepository.deleteByFileId(fileId);


            return ResponseEntity.ok(Map.of("status", "success",
                    "message", "File uploaded successfully"));

        }else {

            Path chunkDir = Paths.get(chunkDirectory, fileId.toString());
            Files.createDirectories(chunkDir);

            String chunkName = chunkIndex + ".chunk";
            Path chunkPath = chunkDir.resolve(chunkName);
            Files.write(chunkPath, chunk.getBytes());
            FileChunkEntity chunkEntity = new FileChunkEntity();
            chunkEntity.setFileId(fileId);
            chunkEntity.setChunkIndex(chunkIndex);
            chunkEntity.setChunkPath(chunkPath.toString());
            fileChunkRepository.save(chunkEntity);
            return ResponseEntity.ok(Map.of("status", "success",
                    "message", "Chunk uploaded successfully"));
        }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping
    public ResponseEntity<List<FileEntity>> getAllFiles() {
        List<FileEntity> files = fileRepository.findAll();
        return ResponseEntity.ok(files);
    }

    @GetMapping("{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long fileId) {
        FileEntity file = fileRepository.findById(fileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Path filePath = Path.of(file.getPath());

        Resource resource = new FileSystemResource(filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
