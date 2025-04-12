package com.infinull.sit.persistence;

import com.infinull.sit.exception.SitException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SitFileUtil {

    private static boolean initialized = false;
    private static Path PROJECT_ROOT_DIR; // The root directory of sit project
    private static Path OBJECTS_DIR; // The directory where objects are stored
    private static Path USER_DIR; // The directory where command is called

    public static void initialize() throws SitException {
        if (!initialized) {
            PROJECT_ROOT_DIR = getProjectRoot();
            OBJECTS_DIR = PROJECT_ROOT_DIR.resolve(".git").resolve("objects");
            USER_DIR = Paths.get(System.getProperty("user.dir"));
            initialized = true;
        }
    }

    private static Path getProjectRoot() throws SitException {
        Path current = Paths.get("").toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve(".git"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new SitException(1, "error.repo.not_found");
    }

    // -- PATH Getters

    public static Path getProjectRootDir() {
        return PROJECT_ROOT_DIR;
    }

    public static Path getObjectsDir() {
        return OBJECTS_DIR;
    }

    public static Path getUserDir() {
        return USER_DIR;
    }

    // -- Other Helpers

    public static String getRelativePathFromProjectRoot(Path path) {
        return PROJECT_ROOT_DIR.relativize(path).toString();
    }

    public static String getRelativePathFromProjectRoot(String relativePath) {
        Path path = USER_DIR.resolve(relativePath).normalize();
        if (path.startsWith(PROJECT_ROOT_DIR)) {
            return PROJECT_ROOT_DIR.relativize(path).toString();
        } else {
            return path.toString();
        }
    }

    public static String getFileMode(File file) {
        if (file.isDirectory()) {
            return "40000"; // directory
        } else if (file.canExecute()) {
            return "100755"; // executable
        } else if (!file.canWrite()) {
            return "100444"; // read-only
        } else if (file.canWrite()) {
            return "100644"; // read-write
        } else {
            return "100644"; // default
        }
    }
}