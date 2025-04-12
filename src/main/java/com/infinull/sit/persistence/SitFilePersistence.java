package com.infinull.sit.persistence;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.object.BlobObject;
import com.infinull.sit.object.FileBasedObject;
import com.infinull.sit.object.TreeObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SitFilePersistence {
    // 1. Get File or Folder
    public static FileBasedObject get(String relativePathString) throws SitException {
        File file = getFilePath(relativePathString).toFile();
        if (!file.exists()) {
            throw new SitException(1, "error.file_folder.not_exist", SitFileUtil.getRelativePathFromProjectRoot(file.toPath()));
        }

        // Check if the file or folder
        if (file.isDirectory()) {
            return getTreeObject(file);
        } else {
            return getBlobObject(file);
        }
    }

    private static TreeObject getTreeObject(File file) throws SitException {
        File[] childrenFiles = file.listFiles();
        ArrayList<FileBasedObject> children = new ArrayList<>();
        TreeObject treeObject = new TreeObject(file.toPath().toAbsolutePath(), SitFileUtil.getFileMode(file), children);

        if (childrenFiles == null) {
            return treeObject;
        }

        for (File childFile : childrenFiles) {
            if (childFile.isDirectory()) {
                treeObject.addChild(getTreeObject(childFile));
            } else {
                treeObject.addChild(getBlobObject(childFile));
            }
        }
        return treeObject;
    }

    private static BlobObject getBlobObject(File file) throws SitException {
        String fileData;
        Path filePath = file.toPath();
        try {
            fileData = Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SitException(1, "error.object.read", SitFileUtil.getRelativePathFromProjectRoot(filePath));
        }
        return new BlobObject(filePath.toAbsolutePath(), SitFileUtil.getFileMode(file), fileData);
    }

    // 2. Check if exists
    public static boolean fileExists(String relativePathString) {
        return getFilePath(relativePathString).toFile().exists();
    }

    // 3. Delete
    public static boolean delete(String relativePathString) {
        File file = getFilePath(relativePathString).toFile();
        if (!file.exists()) {
            throw new SitException(1, "error.file_folder.not_exist", SitFileUtil.getRelativePathFromProjectRoot(file.toPath()));
        }
        if (file.isDirectory()) {
            return deleteDirectory(file);
        } else {
            return file.delete();
        }
    }

    private static boolean deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }

    // 4. Save
    public static void save(BlobObject blobObject) throws SitException {
        save(blobObject, false);
    }


    public static void save(BlobObject blobObject, boolean force) throws SitException {
        try {
            File file = blobObject.getAbsolutePath().toFile();
            if (file.exists() && !force) {
                throw new SitException(1, "error.file.exist", SitFileUtil.getRelativePathFromProjectRoot(file.toPath()));
            }
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new SitException(1, "error.file.write", ". Failed to create parent directories for " + SitFileUtil.getRelativePathFromProjectRoot(file.toPath()));
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(blobObject.getContentBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SitException(1, "error.file.write", SitFileUtil.getRelativePathFromProjectRoot(blobObject.getAbsolutePath()));
        }
    }

    public static void save(TreeObject treeObject) throws SitException {
        save(treeObject, false);
    }

    public static void save(TreeObject treeObject, boolean force) throws SitException {

    }

    // -- Helper method
    private static Path getFilePath(String relativePathString) {
        Path path = SitFileUtil.getUserDir().resolve(relativePathString).normalize();
        if (!path.startsWith(SitFileUtil.getProjectRootDir())) {
            throw new SitException(1, "error.file_object.illegal_access", SitFileUtil.getProjectRootDir().relativize(path));
        }
        return path;
    }
}