package com.infinull.sit.object;

import com.infinull.sit.enums.FILEMODE;
import com.infinull.sit.enums.SITOBJECTTYPE;
import com.infinull.sit.exception.SitException;
import com.infinull.sit.util.Sha;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public final class SitObjectFactory {

    private SitObjectFactory() {
    } // Prevent instantiation

    // -- From Byte[]

    /**
     * Parses raw object data into a SitObject.
     * Expected format: "<type> <size>\0<content bytes>"
     */
    public static SitObject parse(byte[] data) {
        int headerEnd = indexOfNullByte(data, 0);
        if (headerEnd < 0) {
            throw new SitException(1, "error.object.header_format");
        }

        String header = new String(data, 0, headerEnd, StandardCharsets.UTF_8);
        String[] headerParts = header.split(" ", 2);
        if (headerParts.length != 2) {
            throw new SitException(1, "error.object.header_format", header);
        }

        SITOBJECTTYPE type = SITOBJECTTYPE.fromName(headerParts[0]);
        int size;
        try {
            size = Integer.parseInt(headerParts[1]);
        } catch (NumberFormatException e) {
            throw new SitException(1, "error.object.size_parse", headerParts[1]);
        }

        byte[] content = Arrays.copyOfRange(data, headerEnd + 1, data.length);
        if (content.length != size) {
            throw new SitException(1, "error.object.size_mismatch", size + " != " + content.length);
        }

        switch (type) {
            case BLOB:
                return new Blob(content);
            case TREE:
                return parseTreeFromByteContent(content);
            // Extend with COMMIT, TAG etc. as needed.
            default:
                throw new SitException(1, "error.object.type.unknown", type.toString());
        }
    }

    private static Tree parseTreeFromByteContent(byte[] content) {
        Tree tree = new Tree();
        int pos = 0;
        while (pos < content.length) {
            int nullPos = indexOfNullByte(content, pos);
            if (nullPos < 0) {
                throw new SitException(1, "error.tree.entry.header_format");
            }

            String entryHeader = new String(content, pos, nullPos - pos, StandardCharsets.UTF_8);
            String[] headerParts = entryHeader.split(" ", 2);
            if (headerParts.length != 2) {
                throw new SitException(1, "error.tree.entry.header_format", entryHeader); // Assert 2 parts in header
            }

            String modeStr = headerParts[0];
            String name = headerParts[1];

            if (nullPos + 20 >= content.length + 1) {
                throw new SitException(1, "error.tree.entry.sha_missing"); // Assert 20 bytes for SHA
            }
            byte[] shaBytes = Arrays.copyOfRange(content, nullPos + 1, nullPos + 21);
            Sha sha = new Sha(shaBytes);

            // Convert the mode string to FILEMODE (requires a fromValue method in FILEMODE)
            FILEMODE mode = FILEMODE.fromValue(modeStr);
            // Optionally determine the child object's type via mode.toSitObjectType()
            tree.addEntry(mode, name, sha);

            pos = nullPos + 21;
        }
        return tree;
    }

    // -- From File

    /**
     * WARNING : Don't pass .git folder or any of it's contents
     */
    public static SitObject parse(File file) {
        if (file.isDirectory()) {
            return parseTreeFromFolder(file);
        } else {
            return parseBlobFromFile(file);
        }
    }

    private static Blob parseBlobFromFile(File file) {
        try {
            byte[] content = Files.readAllBytes(file.toPath());
            return new Blob(content);
        } catch (IOException e) {
            throw new SitException(1, "error.object.read", file.getAbsolutePath());
        }
    }

    // todo : medium : need to consider other file types too
    private static Tree parseTreeFromFolder(File folder) {
        if (!folder.isDirectory()) {
            throw new SitException(1, "error.not_a_directory", folder.getAbsolutePath());
        }
        Tree tree = new Tree();
        File[] childrenFiles = folder.listFiles();
        if (childrenFiles == null) {
            return tree;
        }
        for (File child : childrenFiles) {
            if (child.isDirectory()) {
                if (child.getName().equals(".git")) {
                    continue; // Skip .git folder
                }
                Tree childTree = parseTreeFromFolder(child);
                tree.addEntry(FILEMODE.DIRECTORY, child.getName(), childTree.getSha());
            } else {
                Blob blob = parseBlobFromFile(child);
                tree.addEntry(FILEMODE.REGULAR_FILE, child.getName(), blob.getSha());
            }
        }
        return tree;
    }

    // -- Helper Methods

    private static int indexOfNullByte(byte[] data, int from) {
        for (int i = from; i < data.length; i++) {
            if (data[i] == 0) return i;
        }
        return -1;
    }
}
