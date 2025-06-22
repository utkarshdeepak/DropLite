ALTER SESSION SET CONTAINER=FREEPDB1;

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE droplite.file_chunks';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -942 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE droplite.files';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -942 THEN
            RAISE;
        END IF;
END;
/

CREATE TABLE droplite.files (
       id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       file_name VARCHAR2(255) NOT NULL,
       file_type VARCHAR2(20),
       file_size NUMBER,
       file_path VARCHAR2(500),
       chunk_count INTEGER,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE droplite.file_chunks (
       id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       file_id NUMBER NOT NULL,
       chunk_index INTEGER NOT NULL,
       chunk_path VARCHAR2(500) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMIT;
