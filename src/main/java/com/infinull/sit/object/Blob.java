package com.infinull.sit.object;

import com.infinull.sit.enums.SITOBJECTTYPE;

import java.nio.charset.StandardCharsets;

public class Blob extends SitObject {
    private final byte[] content;

    public Blob(byte[] content) {
        super(SITOBJECTTYPE.BLOB);
        this.content = content.clone(); // defensive copy
    }

    @Override
    public String getFullContent() {
        return new String(serialize(), StandardCharsets.UTF_8);
    }

    @Override
    public String getContent() {
        return new String(content, StandardCharsets.UTF_8);
    }

    @Override
    public int getSize() {
        return content.length;
    }

    @Override
    public byte[] serialize() {
        // Format: "blob <length>\0<content>"
        byte[] header = ("blob " + content.length + "\0").getBytes(StandardCharsets.UTF_8);
        byte[] full = new byte[header.length + content.length];
        System.arraycopy(header, 0, full, 0, header.length);
        System.arraycopy(content, 0, full, header.length, content.length);
        return full;
    }

    @Override
    public String toString() {
        return "Blob{" +
                "type=" + type +
                ", size=" + content.length +
                ", sha='" + getSha() + '\'' +
                "content='" + getContent() + '\'' +
                '}';
    }
}
