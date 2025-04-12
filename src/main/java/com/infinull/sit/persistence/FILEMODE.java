package com.infinull.sit.persistence;

import java.io.File;

public enum FILEMODE {
    TREE("40000", "directory"),
    REGULAR_FILE("100644", "regular file (read-write)"),
    EXECUTABLE_FILE("100755", "executable file"),
    READ_ONLY_FILE("100444", "read-only file"),
    SYMLINK("120000", "symbolic link"),
    SUBMODULE("160000", "git submodule"),
    UNKNOWN("000000", "unknown or unsupported");

    private final String code;
    private final String description;

    FILEMODE(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static FILEMODE fromCode(String code) {
        for (FILEMODE mode : values()) {
            if (mode.code.equals(code)) {
                return mode;
            }
        }
        return UNKNOWN;
    }

    public static FILEMODE fromFile(File file) {
        if (file.isDirectory()) {
            return TREE;
        } else if (file.isFile()) {
            if (file.canExecute()) return EXECUTABLE_FILE;
            else if (!file.canWrite()) return READ_ONLY_FILE;
            else return REGULAR_FILE;
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return code + " (" + description + ")";
    }
}
