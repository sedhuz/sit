package com.infinull.sit.store;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.util.Sha;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public final class PathStore {

    private static boolean initialized = false;

    private static Path PROJECT_ROOT_DIR; // The root directory of sit project
    private static Path OBJECTS_DIR; // The directory where objects are stored
    private static Path USER_DIR; // The directory where command is called

    private PathStore() {
    }  // Prevent instantiation

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
        ensureInit();
        return PROJECT_ROOT_DIR;
    }

    public static Path getObjectsDir() {
        ensureInit();
        return OBJECTS_DIR;
    }

    public static Path getUserDir() {
        ensureInit();
        return USER_DIR;
    }

    private static void ensureInit() {
        if (!initialized)
            throw new IllegalStateException("SITPaths not initialized.");
    }

    // -- Other Helpers

    public static Path getObjectPath(Sha sha) {
        ensureInit();
        String shaString = sha.toString();
        String objectFolder = shaString.substring(0, 2);
        String objectFile = shaString.substring(2);
        Path path = OBJECTS_DIR.resolve(objectFolder).resolve(objectFile).normalize();

        if (!path.startsWith(OBJECTS_DIR)) {
            throw new SitException(1, "error.file_object.illegal_access", shaString);
        }

        return path;
    }

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
}
