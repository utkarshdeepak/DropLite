package com.utkarshdeepak.droplite.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="file_chunks")
@Getter
@Setter
public class FileChunkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Column(name = "chunk_index", nullable = false)
    private int chunkIndex;

    @Column(name = "chunk_path", length = 500 )
    private String chunkPath;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
}
