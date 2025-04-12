package com.infinull.sit.object;

import com.infinull.sit.exception.SitException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;

public class TreeObject extends FileBasedObject {
    private ArrayList<FileBasedObject> children;

    public TreeObject() {
        super(SITOBJECTTYPES.TREE, null, null);
        this.children = new ArrayList<>();
    }

    public TreeObject(Path absolutePath, String fileMode, ArrayList<FileBasedObject> children) {
        super(SITOBJECTTYPES.TREE, fileMode, absolutePath);
        this.children = children;
    }

    public void addChild(FileBasedObject fileBasedObject) {
        children.add(fileBasedObject);
    }

    public boolean removeChild(FileBasedObject fileBasedObject) {
        return children.remove(fileBasedObject);
    }

    public ArrayList<FileBasedObject> getChildren() {
        return children;
    }

    @Override
    public SitObject toSitObject() {
        return new SitObject(type, getContentBytes());
    }

    @Override
    public String getContent() {
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < children.size(); i++) {
            FileBasedObject child = children.get(i);
            contentBuilder.append(String.format("%s %s\0%s", child.getFileMode(), child.getAbsolutePath().getFileName(), child.toSitObject().getSha()));
            if (i < children.size() - 1) contentBuilder.append("\n");
        }
        return contentBuilder.toString();
    }

    @Override
    public byte[] getContentBytes() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            for (FileBasedObject child : children) {
                String modeAndName = String.format("%s %s", child.getFileMode(), child.getAbsolutePath().getFileName().toString());
                out.write(modeAndName.getBytes(StandardCharsets.UTF_8));
                out.write(0); // null byte
                out.write(child.toSitObject().getShaBytes()); // 20 bytes raw SHA
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new SitException(1, "error.object.write");
        }
    }
}