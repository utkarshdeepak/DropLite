import React, { useState, useRef } from 'react';
import { Button } from "@heroui/react";


const CHUNK_SIZE = 250 * 1024;

const FileUploadButton = () => {

    const fileInputRef = useRef(null);
    const [uploading, setUploading] = useState(false);

    const handleButtonClick = () => {
        fileInputRef.current.click(); // triggers hidden input
    };

    const getFileId = async (fileName, totalChunkCount) => {
        const response = await fetch("http://localhost:8080/api/files/initiate", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ fileName ,totalChunkCount }),
        });
        const data = await response.json();
        return data.fileId;
    };

    const uploadChunk = async (fileId, chunk, index) => {
        const formData = new FormData();
        formData.append("fileId", fileId);
        formData.append("chunkIndex", index);
        formData.append("chunk", chunk);

        await fetch("http://localhost:8080/api/files", {
            method: "POST",
            body: formData,
        });
    };

    const handleFileSelect = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        setUploading(true);

        try {

            const totalChunks = Math.ceil(file.size / CHUNK_SIZE);
            const fileId = await getFileId(file.name,totalChunks);

            for (let i = 0; i < totalChunks; i++) {
                const start = i * CHUNK_SIZE;
                const end = Math.min(start + CHUNK_SIZE, file.size);
                const chunk = file.slice(start, end);

                await uploadChunk(fileId, chunk, i);
            }

            alert("File uploaded successfully.");
        } catch (error) {
            console.error("Upload failed:", error);
            alert("Upload failed. See console for details.");
        }

        setUploading(false);
    };

    return (
        <>

                <input
                    type="file"
                    ref={fileInputRef}
                    onChange={handleFileSelect}
                    style={{ display: "none" }}
                />
                <Button
                    onPress={handleButtonClick}

                >
                    {uploading ? "Uploading..." : "Select & Upload File"}
                </Button>


        </>
    );
};

export default FileUploadButton;