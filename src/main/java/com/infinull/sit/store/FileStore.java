package com.infinull.sit.store;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.object.SitObject;
import com.infinull.sit.object.SitObjectFactory;

import java.io.File;
import java.nio.file.Path;

public class FileStore {

    // 1. Get Object
    public static SitObject get(String relativePathString) throws SitException {
        Path path = getFilePath(relativePathString);
        File file = path.toFile();
        if (!file.exists()) {
            throw new SitException(1, "error.file_folder.not_exist", PathStore.getRelativePathFromProjectRoot(path));
        }
        return SitObjectFactory.parse(file);
    }


    // 2. Check If Exists
    public static boolean fileExists(String relativePathString) {
        return getFilePath(relativePathString).toFile().exists();
    }

    // 3. Delete
    public static boolean delete(String relativePathString) throws SitException {
        Path path = getFilePath(relativePathString);
        File file = path.toFile();
        if (!file.exists()) {
            throw new SitException(1, "error.file_folder.not_exist", PathStore.getRelativePathFromProjectRoot(path));
        }
        return file.isDirectory() ? deleteDirectory(file) : file.delete();
    }

    private static boolean deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
        return directory.delete();
    }

    // 4. Save
    // todo : Save data from commit object

    // -- Helper methods
    private static Path getFilePath(String relativePathString) {
        Path path = PathStore.getUserDir().resolve(relativePathString).normalize();
        if (!path.startsWith(PathStore.getProjectRootDir())) {
            throw new SitException(1, "error.file_object.illegal_access", PathStore.getProjectRootDir().relativize(path));
        }
        return path;
    }
}
