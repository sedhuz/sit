package com.infinull.sit.object;

import com.infinull.sit.persistence.FILEMODE;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class BlobObject extends FileBasedObject {
    String content;

    BlobObject() {
        super(SITOBJECTTYPE.BLOB, null, null);
    }

    public BlobObject(Path absolutePath, FILEMODE fileMode, String content) {
        super(SITOBJECTTYPE.BLOB, fileMode, absolutePath);
        this.content = content;
    }

    @Override
    public SitObject toSitObject() {
        return new SitObject(type, content);
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public byte[] getContentBytes() {
        return content.getBytes(StandardCharsets.UTF_8);
    }
}