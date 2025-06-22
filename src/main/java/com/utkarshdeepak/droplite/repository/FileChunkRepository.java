package com.utkarshdeepak.droplite.repository;

import com.utkarshdeepak.droplite.entity.FileChunkEntity;
import com.utkarshdeepak.droplite.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileChunkRepository  extends JpaRepository<FileChunkEntity, Long> {
    List<FileChunkEntity> findAllByFileIdOrderByChunkIndex(Long fileId);

    FileChunkEntity findFileEntityByChunkIndexAndFileId(int chunkIndex, Long fileId);

    int countByFileId(Long fileId);

    void deleteByFileId(Long fileId);
}
