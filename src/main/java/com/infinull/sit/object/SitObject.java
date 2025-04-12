package com.infinull.sit.object;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.util.Sha;

import java.nio.charset.StandardCharsets;

public class SitObject {
    private SITOBJECTTYPES objectType;
    private int size;
    private String content;          // Used for text-based objects
    private byte[] contentBytes;     // Used for binary-safe content (like tree)

    // Constructors
    SitObject() {
    }

    SitObject(SITOBJECTTYPES objectType, String content) {
        this.objectType = objectType;
        this.content = content;
        this.contentBytes = content.getBytes(StandardCharsets.UTF_8);
        this.size = contentBytes.length;
    }

    SitObject(SITOBJECTTYPES objectType, byte[] contentBytes) {
        this.objectType = objectType;
        this.contentBytes = contentBytes;
        this.size = contentBytes.length;
        this.content = new String(contentBytes, StandardCharsets.ISO_8859_1); // reversible
    }

    public SitObject(SITOBJECTTYPES objectType, int size, String content) {
        this.objectType = objectType;
        this.content = content;
        this.contentBytes = content.getBytes(StandardCharsets.UTF_8);
        if (size != getContentBytes().length) {
            throw new SitException(1, "error.object.size.mismatch", size, getContentBytes().length);
        }
        this.size = size;
    }

    // Getters
    public SITOBJECTTYPES getObjectType() {
        return objectType;
    }

    public void setObjectType(SITOBJECTTYPES objectType) {
        this.objectType = objectType;
    }

    public int getSize() {
        return size;
    }

    public String getContent() {
        return content;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    // SHA + Serialization
    public byte[] toByteArray() {
        byte[] header = (objectType.getTypeString() + " " + size + "\0").getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[header.length + contentBytes.length];
        System.arraycopy(header, 0, result, 0, header.length);
        System.arraycopy(contentBytes, 0, result, header.length, contentBytes.length);
        return result;
    }

    public Sha getSha() {
        return Sha.computeSha(toByteArray());
    }

    public byte[] getShaBytes() {
        return getSha().getShaBytes();
    }

    @Override
    public String toString() {
        return "SitObject{" +
                "objectType=" + objectType +
                ", size=" + size +
                ", sha='" + getSha() + '\'' +
                '}';
    }
}
