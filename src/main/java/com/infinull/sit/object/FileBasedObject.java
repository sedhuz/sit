package com.infinull.sit.object;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Paths;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.util.Sha;
import com.infinull.sit.util.Shas;

/**
 * Represents an abstract file-based object in the SIT version control system.
 * This class is extended by {@link BlobObject} (for files) and {@link TreeObject} (for directories).
 *
 *  @author Infinull
 *  @see com.infinull.sit.object.SitObject
 */
public abstract class FileBasedObject extends SitObject {
    protected Path filePath;

    protected FileBasedObject() {}

    protected FileBasedObject(Path filePath) {
        super();
        this.filePath = filePath;
    }

    protected FileBasedObject(String fileAbsolutePathString) {
        super();
        this.filePath = FileBasedObject.getFile(fileAbsolutePathString).toPath();
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public File getFile() {
        return filePath.toFile();
    }

    public String getFileName() {
        return filePath.getFileName().toString();
    }

    public boolean fileExists() {
        return Files.exists(filePath);
    }

    public boolean isReadable() {
        return Files.isReadable(filePath);
    }

    public boolean isWritable() {
        return Files.isWritable(filePath);
    }

    public boolean isExecutable() {
        return Files.isExecutable(filePath);
    }

    public String getFileMode() {
        try {
            if (Files.isDirectory(filePath)) {
                return "40000"; // Directory
            } else if (Files.isExecutable(filePath)) {
                return "100755"; // Executable file
            } else {
                return "100644"; // Regular file
            }
        } catch (Exception e) {
            return "000000"; // Unknown/invalid mode
        }
    }

    public void fileDelete() {
        try {
            if (Files.isDirectory(filePath)) {
                deleteDirectory(filePath);
            } else {
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            throw new SitException(1, "error.file.delete", filePath.toString());
        }
    }

    private void deleteDirectory(Path dir) throws IOException {
            Files.walk(dir)
                    .sorted((p1, p2) -> -p1.compareTo(p2)) // Reverse order to delete files first
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            throw new SitException(1, "error.file.delete", path.toString());
                        }
                    });
    }

    protected Sha computeSha() {
        return Shas.computeSha(toStringForSit());
    }

    // -- Static Helpers

    public static final String USER_DIR; // The directory where command was invoked

    static {
        USER_DIR = System.getProperty("user.dir");
    }

    protected static File getFile(String absoluteFilePathString) {
        return getFile(absoluteFilePathString, false);
    }

    protected static File getFile(String absoluteFilePathString, boolean canThrowException) {
        Path path = Paths.get(USER_DIR, absoluteFilePathString).normalize();
        File file = path.toFile();
        if (!path.startsWith(USER_DIR)) {
            throw new SitException(1, "error.file_object.illegal_access", path.toString()); // This should always be thrown as it is a security check
        }
        if (canThrowException && !file.exists()) {
            throw new SitException(1, "error.file.not_exist", path.toString());
        }
        return file.exists() ? file : null;
    }

}
