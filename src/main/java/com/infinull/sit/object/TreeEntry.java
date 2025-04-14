package com.infinull.sit.object;

import com.infinull.sit.enums.FILEMODE;
import com.infinull.sit.enums.SITOBJECTTYPE;
import com.infinull.sit.util.Sha;

public class TreeEntry {
    private FILEMODE mode;
    private String name;
    private Sha sha;
    private SITOBJECTTYPE type;

    public TreeEntry(FILEMODE mode, String name, Sha sha, SITOBJECTTYPE type) {
        this.mode = mode;
        this.name = name;
        this.sha = sha;
        this.type = type;
    }

    public FILEMODE getMode() {
        return mode;
    }

    public void setMode(FILEMODE mode) {
        this.mode = mode;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sha getSha() {
        return sha;
    }

    public void setSha(Sha sha) {
        this.sha = sha;
    }

    public SITOBJECTTYPE getType() {
        return type;
    }

    public void setType(SITOBJECTTYPE type) {
        this.type = type;
    }

    public byte[] serialize() {
        // Format: "<mode> <name>\0<sha>"
        String header = String.format("%s %s\0", mode.getValue(), name);
        byte[] headerBytes = header.getBytes();
        byte[] shaBytes = sha.toBytes();
        byte[] fullEntry = new byte[headerBytes.length + shaBytes.length];
        System.arraycopy(headerBytes, 0, fullEntry, 0, headerBytes.length);
        System.arraycopy(shaBytes, 0, fullEntry, headerBytes.length, shaBytes.length);
        return fullEntry;
    }

    public int getSize() {
        return serialize().length;
    }

    @Override
    public String toString() {
        return "TreeEntry{" +
                "mode=" + mode +
                ", name='" + name + '\'' +
                ", sha=" + sha +
                ", type=" + type +
                '}';
    }
}
