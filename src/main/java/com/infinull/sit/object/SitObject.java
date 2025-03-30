package com.infinull.sit.object;

import com.infinull.sit.exception.SitException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.InflaterInputStream;


/*
 * NOTE : Object files in git are compressed using zlib deflate, u have to inflate (decompress)
 * NOTE : The structure of a Git object follows this format:
 * 1. A type identifier (`blob`, `commit`, `tag`, or `tree`).
 * 2. A single ASCII space (`0x20`).
 * 3. The size of the object in bytes, represented as an ASCII number.
 * 4. A null byte (`0x00`).
 * 5. The actual contents of the object.
 * Example : blob 13␀Hello, Git!
 */

public class SitObject {
    protected Sha sha;
    protected SITOBJECTTYPES type;
    protected Integer size;
    protected String content;

    protected static final String BASE_DIR;
    protected static final String OBJECTS_DIR;

    static {
        BASE_DIR = System.getProperty("user.dir");
        OBJECTS_DIR = BASE_DIR + File.separator + ".git" + File.separator + "objects";
    }

    protected SitObject() {
    }

    public SitObject(Sha sha) {
        this.sha = sha;
        parseIntoObject(getObjectFile(sha));
    }

    public Sha getSha() {
        return sha;
    }

    public SITOBJECTTYPES getType() {
        return type;
    }

    public Integer getSize() {
        return size;
    }

    public String getContent() {
        return content;
    }

    public void setSha(Sha sha) {
        this.sha = sha;
    }

    public void setType(SITOBJECTTYPES type) {
        this.type = type;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SitObject{" +
                "sha='" + sha + '\'' +
                ", type=" + type +
                ", size=" + size +
                ", content='" + content + '\'' +
                '}';
    }

    public String toStringForSit() {
        return type.getTypeString() + " " + size + "\0" + content;
    }

    public byte[] toByteArray() {
        return toStringForSit().getBytes(StandardCharsets.UTF_8);
    }

    private static File getObjectFile(Sha sha) {
        String shaString = sha.getShaString();
        if (shaString.length() < 2) {
            throw new SitException(1, "error.object.exist", shaString);
        }
        String objectFolder = shaString.substring(0, 2);
        String objectFile = shaString.substring(2);
        Path path = Paths.get(OBJECTS_DIR, objectFolder, objectFile).normalize();
        if (!path.startsWith(OBJECTS_DIR)) {
            throw new SitException(1, "error.object.exist", shaString);
        }
        File file = path.toFile();
        if (!file.exists()) {
            throw new SitException(1, "error.object.exist", shaString);
        }
        return file;
    }

    private void parseIntoObject(File file) {
        try {
            byte[] objectBytes = Files.readAllBytes(file.toPath());
            try (InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(objectBytes))) {
                byte[] buffer = new byte[1024];
                StringBuilder objectData = new StringBuilder();
                int bytesRead;
                while ((bytesRead = inflater.read(buffer)) != -1) {
                    objectData.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
                }
                String[] parts = objectData.toString().split("\0", 2);
                if (parts.length != 2) {
                    throw new SitException(1, "error.object.read", file.getName());
                }
                String[] headerParts = parts[0].split(" ");
                this.type = SITOBJECTTYPES.fromString(headerParts[0]);
                this.size = Integer.parseInt(headerParts[1]);
                this.content = parts[1];
            }
        } catch (IOException e) {
            throw new SitException(1, "error.file.read", file.getName());
        }
    }
}