package com.infinull.sit.object;

import com.infinull.sit.exception.SitException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DeflaterOutputStream;

public class BlobObject extends SitObject {
    private String name;
    private String absoluteFilePath;
    private String fileMode;

    public BlobObject(String absoluteFilePath) {
        super();
        Path path = validateAndNormalizePath(absoluteFilePath);
        this.absoluteFilePath = path.toAbsolutePath().toString();
        this.name = path.getFileName().toString();
        File file = path.toFile();
        validateFileExists(file);
        try {
            this.type = SITOBJECTTYPES.BLOB;
            this.content = Files.readString(path);
            this.size = content.length();
            this.fileMode = getFileMode(file);
            this.sha = computeSha();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new SitException(1, "error.file.read", absoluteFilePath);
        }
    }

    public String getName() {
        return name;
    }

    public String getAbsoluteFilePath() {
        return absoluteFilePath;
    }

    public String getFileMode() {
        return fileMode;
    }

    // Actions
    public void persistToSit() {
        String shaString = sha.getShaString();
        String objectPath = OBJECTS_DIR + File.separator + shaString.substring(0, 2) + File.separator + shaString.substring(2);
        File objectFile = new File(objectPath);
        objectFile.getParentFile().mkdirs();
        try (OutputStream outputStream = new FileOutputStream(objectFile);
             DeflaterOutputStream deflater = new DeflaterOutputStream(outputStream)) {
            deflater.write(toStringForSit().getBytes(StandardCharsets.UTF_8));
            deflater.finish();
        } catch (IOException e) {
            throw new SitException(1, "error.file.write", shaString);
        }
    }

    public boolean doesObjectExist() {
        String shaString = sha.getShaString();
        File objectFile = new File(OBJECTS_DIR + File.separator + shaString.substring(0, 2) + File.separator + shaString.substring(2));
        return objectFile.exists();
    }

    // Helpers
    private Sha computeSha() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(toStringForSit().getBytes(StandardCharsets.UTF_8));
        byte[] bytes = digest.digest();
        return new Sha(bytes);
    }


    private String getFileMode(File file) {
        if (file.isDirectory()) {
            return "40000"; // Directory
        } else if (file.canExecute()) {
            return "100755"; // Executable file
        } else {
            return "100644"; // Regular file
        }
    }

    private void validateFileExists(File file) {
        if (!file.exists()) {
            throw new SitException(1, "error.file.not_exist_within_current_dir", absoluteFilePath, BASE_DIR);
        }
    }

    private Path validateAndNormalizePath(String absoluteFilePath) {
        Path path = Paths.get(BASE_DIR, absoluteFilePath).normalize();
        if (!path.startsWith(BASE_DIR)) {
            throw new SitException(1, "error.file.not_exist_within_current_dir", absoluteFilePath, BASE_DIR);
        }
        return path;
    }
}