package com.infinull.sit.object;

import com.infinull.sit.persistence.FILEMODE;

import java.nio.file.Path;

public abstract class FileBasedObject {
    protected final SITOBJECTTYPE type;
    protected FILEMODE fileMode;
    Path absolutePath;

    public FileBasedObject(SITOBJECTTYPE type, FILEMODE fileMode, Path absolutePath) {
        this.type = type;
        this.fileMode = fileMode;
        this.absolutePath = absolutePath;
    }

    public SITOBJECTTYPE getType() {
        return type;
    }

    public String getTypeString() {
        return type.getTypeString();
    }

    public FILEMODE getFileMode() {
        return fileMode;
    }

    public Path getAbsolutePath() {
        return absolutePath;
    }

    public void setFileMode(FILEMODE fileMode) {
        this.fileMode = fileMode;
    }

    public abstract SitObject toSitObject();

    // readable content of the object
    public abstract String getContent();

    // for storing in the object database
    public abstract byte[] getContentBytes();
}
