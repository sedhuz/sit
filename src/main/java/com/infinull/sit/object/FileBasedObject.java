package com.infinull.sit.object;

import java.nio.file.Path;

public abstract class FileBasedObject {
    protected final SITOBJECTTYPES type;
    protected String fileMode;
    Path absolutePath;

    public FileBasedObject(SITOBJECTTYPES type, String fileMode, Path absolutePath) {
        this.type = type;
        this.fileMode = fileMode;
        this.absolutePath = absolutePath;
    }

    public SITOBJECTTYPES getType() {
        return type;
    }

    public String getTypeString() {
        return type.getTypeString();
    }

    public String getFileMode() {
        return fileMode;
    }

    public Path getAbsolutePath() {
        return absolutePath;
    }

    public void setFileMode(String fileMode) {
        this.fileMode = fileMode;
    }

    public abstract SitObject toSitObject();

    // readable content of the object
    public abstract String getContent();

    // for storing in the object database
    public abstract byte[] getContentBytes();
}
