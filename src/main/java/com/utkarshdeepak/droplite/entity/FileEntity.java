package com.utkarshdeepak.droplite.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="files")
@Getter
@Setter
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "file_name", nullable = false, length = 255)
    private String name;

    @Column(name = "file_type", length = 20)
    private String type;

    @Column(name = "file_size")
    private Long size;

    @Column(name = "file_path", length = 500 )
    private String path ;

    @Column(name="chunk_count")
    private int chunkCount;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
}

