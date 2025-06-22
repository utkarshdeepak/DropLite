package com.utkarshdeepak.droplite.repository;

import com.utkarshdeepak.droplite.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
//    FileEntity findByFileName(String fileName);
}
