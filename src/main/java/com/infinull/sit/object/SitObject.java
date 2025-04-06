package com.infinull.sit.object;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.util.Sha;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Represents a generic object in the SIT version control system.
 * This is the base class for all SIT objects, including blobs, trees, commits, and tags.
 *
 * <p>Objects in SIT are compressed using zlib deflate and must be decompressed (inflated) when reading.
 * Each object follows a specific format:
 * <ul>
 *   <li>A type identifier ({@code blob},{@code commit},{@code tag}, or{@code tree}).</li>
 *   <li>A single ASCII space ({@code 0x20}).</li>
 *   <li>The size of the object in bytes, represented as an ASCII number.</li>
 *   <li>A null byte ({@code 0x00}).</li>
 *   <li>The actual contents of the object.</li>
 * </ul>
 * <p>Example of a blob object:</p>
 * <pre>{@code
 * blob 13␀Hello, Sit!
 * }</pre>
 *
 * <p>This class provides basic attributes such as SHA hash, type, size.</p>
 */

public class SitObject {
    protected Sha sha;
    protected SITOBJECTTYPES objectType;
    protected Integer size;
    protected Path objectPath; // internal variable that uses sha to get the object file path

    // -- Constructor & Getters/Setters
    protected SitObject() {}

    protected SitObject(Sha sha) {
        setSha(sha);
    }

    public Sha getSha() {
        return sha;
    }

    public SITOBJECTTYPES getObjectType() {
        return objectType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSha(Sha sha) {
        this.sha = sha;
        this.objectPath = getObjectFile(sha).toPath();
    }

    public void setObjectType(SITOBJECTTYPES objectType) {
        this.objectType = objectType;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    // -- Custom getters
    public String getObjectContent() {
        String[] parts = readObject().split("\0", 2);
        return parts[1];
    }

    // -- Actions
    @Override
    public abstract String toString();

    public abstract String toStringForSit();

    public byte[] toByteArray() {
        return toStringForSit().getBytes(StandardCharsets.UTF_8);
    }

    public boolean objectExists() {
        return objectPath.toFile().exists();
    }

    public void objectSave() {
        objectSave(false, false);
    }

    public void objectSave(boolean canRewrite, boolean canThrowExceptionIfExists) {
        if (objectExists()) {
            if (canThrowExceptionIfExists) {
                throw new SitException(1, "error.object.exist", sha.getShaString());
            } else if (!canRewrite) {
                return;
            }
        }

        try {
            Files.createDirectories(objectPath.getParent()); // Create parent directories if they don't exist

            try (FileOutputStream fileOutputStream = new FileOutputStream(objectPath.toFile());
                 DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(fileOutputStream)) {
                deflaterOutputStream.write(toByteArray());
                deflaterOutputStream.finish();
            }
        } catch (IOException e) {
            throw new SitException(1, "error.object.write", sha.getShaString());
        }
    }

    public void objectDelete() {
        try {
            Files.deleteIfExists(objectPath);
        } catch (IOException e) {
            throw new SitException(1, "error.object.delete", sha.getShaString());
        }
    }

    public String readObject() {
        if (!objectExists()) {
            throw new SitException(1, "error.object.not_exist", sha.getShaString());
        }

        try (InputStream fileInputStream = Files.newInputStream(objectPath);
             InflaterInputStream inflater = new InflaterInputStream(fileInputStream)) {
            return new String(inflater.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SitException(1, "error.object.read", sha.getShaString());
        }
    }

    // -- Static Helpers --

    public static final String PROJECT_ROOT_DIR; // The root directory of sit project
    public static final String OBJECTS_DIR; // The directory where objects are stored

    static {
        PROJECT_ROOT_DIR = getProjectRoot();
        OBJECTS_DIR = PROJECT_ROOT_DIR + File.separator + ".git" + File.separator + "objects";
    }

    protected static String getProjectRoot() {
        Path current = Paths.get("").toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve(".git"))) {
                return current.toString();
            }
            current = current.getParent();
        }
        throw new SitException(1, "error.repo.not_found");
    }

    protected static File getObjectFile(Sha sha) {
        return getObjectFile(sha, false);
    }

    public static File getObjectFile(Sha sha, boolean canThrowException) {
        String shaString = sha.getShaString();
        String objectFolder = shaString.substring(0, 2);
        String objectFile = shaString.substring(2);
        Path path = Paths.get(OBJECTS_DIR, objectFolder, objectFile).normalize();
        File file = path.toFile();
        if (!path.startsWith(OBJECTS_DIR)) {
            throw new SitException(1, "error.file_object.illegal_access", shaString);  // This should always be thrown as it is a security check
        }
        if (canThrowException && !file.exists()) {
            throw new SitException(1, "error.object.not_exist", shaString);
        }
        return file;
    }
}
