package com.infinull.sit.object;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class BlobObject extends FileBasedObject {
    private final String content;

    // When file exists
    public BlobObject(String absoluteFilePath) {
        super(absoluteFilePath);
        this.objectType = SITOBJECTTYPES.BLOB;
        this.content = readFileContent(filePath);
        this.size = content == null ? 0 : content.length();
        setSha(computeSha());
    }

    // When u want to make file from object
    public BlobObject(Path filePath, String content) {
        super(filePath);
        this.objectType = SITOBJECTTYPES.BLOB;
        this.content = content;
        this.size = content == null ? 0 : content.length();
        setSha(computeSha());
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "BlobObject{" +
                "Sha='" + sha + '\'' +
                ", objectType=" + objectType +
                ", size=" + size +
                ", objectExists=" + objectExists() +
                ", objectPath=" + objectPath +
                ", filePath=" + filePath +
                ", fileExists=" + fileExists() +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public String toStringForSit() {
        return "blob " + size + "\0" + content;
    }

    // -- Static Helpers
    private static String readFileContent(Path filePath) {
        String content;
        try {
            content = Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
        return content;
    }
}