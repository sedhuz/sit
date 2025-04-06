package com.infinull.sit.object;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.util.Sha;

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
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;

public class TreeObject extends SitObject {
    private String name;
    private String absoluteFolderPath;
    private String fileMode;
    private ArrayList<SitObject> children;

    public TreeObject(String absoluteFolderPath) {
        super();
        Path path = validateAndNormalizePath(absoluteFolderPath);
        this.absoluteFolderPath = path.toAbsolutePath().toString();
        this.name = path.getFileName().toString();
        File file = path.toFile();
        validateFileExists(file);
        try {
            this.objectType = SITOBJECTTYPES.TREE;
            this.content = Files.readString(path);
            this.size = content.length();
            this.fileMode = getFileMode(file);
            this.sha = computeSha();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new SitException(1, "error.file.read", absoluteFolderPath);
        }
    }

    public String getName() {
        return name;
    }

    public String getAbsoluteFolderPath() {
        return absoluteFolderPath;
    }

    public String getFileMode() {
        return fileMode;
    }

    public ArrayList<SitObject> getChildren() {
        return children;
    }

    // Actions
    public void persistToSit() {
        String shaString = sha.getShaString();
        String objectPath = OBJECTS_DIR + File.separator + shaString.substring(0, 2) + File.separator + shaString.substring(2);
        File objectFile = new File(objectPath);
        objectFile.getParentFile().mkdirs();
        try (OutputStream outputStream = new FileOutputStream(objectFile); DeflaterOutputStream deflater = new DeflaterOutputStream(outputStream)) {
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

    private void validateFileExists(File file) {
        if (!file.exists()) {
            throw new SitException(1, "error.file.not_exist_within_current_dir", absoluteFolderPath, BASE_DIR);
        }
    }

    private Path validateAndNormalizePath(String absoluteFilePath) {
        Path path = Paths.get(BASE_DIR, absoluteFilePath).normalize();
        if (!path.startsWith(BASE_DIR)) {
            throw new SitException(1, "error.file.not_exist_within_current_dir", absoluteFilePath, BASE_DIR);
        }
        return path;
    }

    private void writeContent() {
        StringBuilder contentStringBuilder = new StringBuilder();
        for (SitObject child : children) {
            contentStringBuilder.append(child.getFileMode() + " ")
        }
    }

    @Override
    public String toStringForSit() {
        return super.toStringForSit();
    }
}