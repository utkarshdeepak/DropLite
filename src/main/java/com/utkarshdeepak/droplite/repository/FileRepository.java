package com.utkarshdeepak.droplite.repository;

import com.utkarshdeepak.droplite.entity.FileEntity;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface FileRepository extends JpaRepository<FileEntity, Long> {
//    FileEntity findByFileName(String fileName);
}
