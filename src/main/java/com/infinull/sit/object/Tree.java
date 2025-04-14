package com.infinull.sit.object;

import com.infinull.sit.enums.FILEMODE;
import com.infinull.sit.enums.SITOBJECTTYPE;
import com.infinull.sit.exception.SitException;
import com.infinull.sit.util.Sha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Tree extends SitObject {
    private final HashSet<TreeEntry> children;

    public Tree() {
        super(SITOBJECTTYPE.TREE);
        children = new HashSet<>();
    }

    public void addEntry(FILEMODE filemode, String name, Sha sha) {
        TreeEntry entry = new TreeEntry(filemode, name, sha, type);
        children.add(entry);
    }

    public void removeEntry(String name) {
        children.removeIf(entry -> entry.getName().equals(name));
    }

    public void removeEntry(Sha sha) {
        children.removeIf(entry -> entry.getSha().equals(sha));
    }

    public void addEntry(TreeEntry entry) {
        children.add(entry);
    }

    @Override
    public String getFullContent() {
        return new String(serialize(), StandardCharsets.UTF_8);
    }

    @Override
    public String getContent() { // For Human Readability
        // Format: "<filemode> <object type> <sha>\t<filename>"
        return children.stream()
                .sorted(Comparator.comparing(TreeEntry::getName)) // Sort by name
                .map(child -> String.format("%06d %s %s\t%s",
                        Integer.parseInt(child.getMode().toString()),
                        child.getMode().toSitObjectType(),
                        child.getSha(),
                        child.getName()))
                .collect(Collectors.joining("\n")); // Join entries with a newline
    }

    @Override
    public int getSize() {
        return children.stream().mapToInt(TreeEntry::getSize).sum();
    }

    @Override
    public byte[] serialize() {
        // Tree Format: tree <length>\0<content>"
        // Content's Format: "<filemode> <filename>\0<sha>"
        try {
            // 1. Content
            ByteArrayOutputStream contentOut = new ByteArrayOutputStream();
            children.stream()
                    .sorted(Comparator.comparing(TreeEntry::getName)) // Sort by name
                    .forEach(child -> {
                        try {
                            contentOut.write(child.serialize());
                        } catch (IOException e) {
                            throw new SitException(1, "error.object.serialize");
                        }
                    });
            byte[] contentBytes = contentOut.toByteArray();

            // 2. Header
            String header = String.format("tree %d\u0000", contentBytes.length);
            byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);

            // 3. Concatenate header and content bytes.
            ByteArrayOutputStream fullOut = new ByteArrayOutputStream();
            fullOut.write(headerBytes);
            fullOut.write(contentBytes);
            return fullOut.toByteArray();
        } catch (IOException e) {
            throw new SitException(1, "error.object.serialize");
        }
    }


    @Override
    public String toString() {
        return "Tree{" +
                "type=" + type +
                ", size=" + children.size() +
                ", sha='" + getSha() + '\'' +
                ", children=" + children +
                '}';
    }
}
