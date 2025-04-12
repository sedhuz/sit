package com.infinull.sit.object;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class BlobObject extends FileBasedObject {
    String content;

    BlobObject() {
        super(SITOBJECTTYPES.BLOB, null, null);
    }

    public BlobObject(Path absolutePath, String fileMode, String content) {
        super(SITOBJECTTYPES.BLOB, fileMode, absolutePath);
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