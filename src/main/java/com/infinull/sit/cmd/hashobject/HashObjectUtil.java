package com.infinull.sit.cmd.hashobject;

import com.infinull.sit.exception.SitException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DeflaterOutputStream;

public class HashObjectUtil {
    private static final byte[] BLOB_BYTES = "blob".getBytes(StandardCharsets.UTF_8);
    private static final byte[] SPACE_BYTES = " ".getBytes(StandardCharsets.UTF_8);
    private static final byte[] NULL_BYTES = "\0".getBytes(StandardCharsets.UTF_8);
    private static final String BASE_DIR = System.getProperty("user.dir");

    public static String getSha(String filePath, boolean canWriteFile) {
        File file = getFile(filePath);
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            byte[] fileContentLength = String.valueOf(fileContent.length).getBytes(StandardCharsets.UTF_8);
            byte[] objectData = concatenate(BLOB_BYTES, SPACE_BYTES, fileContentLength, NULL_BYTES, fileContent);
            String sha = computeSha1(objectData);
            if (canWriteFile) {
                writeObjectToGit(sha, objectData);
            }
            return sha;
        } catch (IOException e) {
            throw new SitException(1, "error.file.read", filePath);
        }
    }

    public static File getFile(String absolutePath) {
        Path path = Paths.get(BASE_DIR, absolutePath).normalize();
        if (!path.startsWith(BASE_DIR)) {
            throw new SitException(1, "error.file.exist", absolutePath);
        }
        File file = path.toFile();
        if (!file.exists()) {
            throw new SitException(1, "error.file.exist", absolutePath);
        }
        return file;
    }

    private static void writeObjectToGit(String sha, byte[] objectData) {
        String objectPath = ".git/objects/" + sha.substring(0, 2) + "/" + sha.substring(2);
        File objectFile = new File(objectPath);
        objectFile.getParentFile().mkdirs();
        try (OutputStream outputStream = new FileOutputStream(objectFile);
             DeflaterOutputStream deflater = new DeflaterOutputStream(outputStream)) {
            deflater.write(objectData);
            deflater.finish();
        } catch (IOException e) {
            throw new SitException(1, "error.file.write", sha);
        }
    }

    private static String computeSha1(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] shaBytes = digest.digest(data);
            StringBuilder sha = new StringBuilder();
            for (byte b : shaBytes) {
                sha.append(String.format("%02x", b));
            }
            return sha.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new SitException(1, "error.sha1.compute");
        }
    }

    private static byte[] concatenate(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] arr : arrays) {
            totalLength += arr.length;
        }

        byte[] result = new byte[totalLength];
        int position = 0;
        for (byte[] arr : arrays) {
            System.arraycopy(arr, 0, result, position, arr.length);
            position += arr.length;
        }

        return result;
    }
}