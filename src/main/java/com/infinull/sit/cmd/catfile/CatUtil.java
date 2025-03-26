package com.infinull.sit.cmd.catfile;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.message.MessageUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

/*
 * NOTE : Object files in git are compressed using zlib deflate, u have to inflate (decompress)
 * NOTE : The structure of a Git object follows this format:
 * 1. A type identifier (`blob`, `commit`, `tag`, or `tree`).
 * 2. A single ASCII space (`0x20`).
 * 3. The size of the object in bytes, represented as an ASCII number.
 * 4. A null byte (`0x00`).
 * 5. The actual contents of the object.
 * Example : blob 13␀Hello, Git!
 */

public class CatUtil {
    static void printObject(String sha) {
        MessageUtil.printMsg(getContent(sha)); // Print content
    }

    static void printSize(String sha) {
        String header = getHeader(sha);
        if (header == null) {
            throw new SitException(1, "error.object.reading", sha);
        }
        String[] parts = header.split(" ");
        if (parts.length < 2) {
            throw new SitException(1, "error.object.reading", sha);
        }
        throw new SitException(0, parts[1]); // Print size
    }

    static void printType(String sha) {
        String header = getHeader(sha);
        if (header == null) {
            throw new SitException(1, "error.object.reading", sha);
        }
        String[] parts = header.split(" ");
        if (parts.length < 2) {
            throw new SitException(1, "error.object.reading", sha);
        }
        throw new SitException(0, parts[0]); // Print type (e.g., "blob")
    }

    static void checkExistence(String sha) {
        File objectFile = getObjectFile(sha);
        if (objectFile == null || !objectFile.exists()) {
            throw new SitException(1, "");
        }
    }

    // --- Helpers

    private static String getHeader(String sha) {
        String headerAndContent = getHeaderAndContent(sha);
        if (headerAndContent == null) {
            return null;
        }
        int headerEnd = headerAndContent.indexOf('\0');
        if (headerEnd == -1) {
            return null;
        }
        return headerAndContent.substring(0, headerEnd);
    }

    private static String getContent(String sha) {
        String headerAndContent = getHeaderAndContent(sha);
        if (headerAndContent == null) {
            return null;
        }
        int headerEnd = headerAndContent.indexOf('\0');
        if (headerEnd == -1) {
            return null;
        }
        return headerAndContent.substring(headerEnd + 1);
    }

    private static String getHeaderAndContent(String sha) {
        byte[] decompressed = readAndDecompress(sha);
        if (decompressed == null) {
            return null;
        }
        return new String(decompressed);
    }

    private static File getObjectFile(String sha) {
        if (sha.length() < 2) {
            return null;
        }
        String cwd = System.getProperty("user.dir");
        String shaFolder = sha.substring(0, 2);
        String shaObject = sha.substring(2);
        return new File(cwd, ".git/objects/" + shaFolder + "/" + shaObject);
    }

    private static byte[] readAndDecompress(String sha) {
        File objectFile = getObjectFile(sha);
        if (objectFile == null || !objectFile.exists()) {
            throw new SitException(1, "error.object.notfound", sha);
        }
        try (FileInputStream fis = new FileInputStream(objectFile); InflaterInputStream inflater = new InflaterInputStream(fis); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inflater.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new SitException(1, "error.object.reading", sha);
        }
    }
}
