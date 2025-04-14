package com.infinull.sit.enums;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum FILEMODE {
    REGULAR_FILE("100644"),
    EXECUTABLE_FILE("100755"),
    SYMLINK("120000"),
    DIRECTORY("40000"),
    SUBMODULE("160000"); // GitLink == Submodule

    private final String value;
    private static final Set<String> VALID_MODES = new HashSet<>();

    static {
        for (FILEMODE mode : values()) {
            VALID_MODES.add(mode.value);
        }
    }

    FILEMODE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public SITOBJECTTYPE toSitObjectType() {
        switch (this) {
            case REGULAR_FILE:
            case EXECUTABLE_FILE:
            case SYMLINK:
                return SITOBJECTTYPE.BLOB;
            case DIRECTORY:
                return SITOBJECTTYPE.TREE;
            case SUBMODULE:
                return SITOBJECTTYPE.COMMIT;
            default:
                throw new IllegalArgumentException("Invalid FILEMODE for SITOBJECTTYPE: " + this);
        }
    }

    @Override
    public String toString() {
        return value;
    }

    public static boolean isValid(String mode) {
        return VALID_MODES.contains(mode);
    }

    public static FILEMODE fromValue(String value) {
        return Arrays.stream(values())
                .filter(fileMode -> fileMode.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid File Mode: " + value));
    }

    public static FILEMODE fromFile(File file) {
        if (file.isDirectory()) {
            return DIRECTORY;
        } else if (file.canExecute()) {
            return EXECUTABLE_FILE;
        } else if (file.canRead()) {
            return REGULAR_FILE;
        } else {
            return SYMLINK; // Default to SYMLINK if none of the above
        }
    }
}