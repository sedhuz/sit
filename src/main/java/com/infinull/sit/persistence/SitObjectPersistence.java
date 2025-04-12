package com.infinull.sit.persistence;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.object.SITOBJECTTYPE;
import com.infinull.sit.object.SitObject;
import com.infinull.sit.util.Sha;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class SitObjectPersistence {
    // 1. Get object
    public static SitObject get(Sha sha) throws SitException {
        File file = getObjectPath(sha).toFile();
        if (!file.exists()) {
            throw new SitException(1, "error.object.not_exist", sha.getShaString());
        }

        String objectData;
        try (FileInputStream fis = new FileInputStream(file);
             InflaterInputStream inflaterInputStream = new InflaterInputStream(fis);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = inflaterInputStream.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            objectData = new String(out.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SitException(1, "error.object.read", sha.getShaString());
        }

        // Parse object data
        String[] parts = objectData.split("\0", 2); // Split at null byte
        String[] headerParts = parts[0].split(" ", 2); // Split header at space

        if (headerParts.length != 2) {
            throw new SitException(1, "error.object.header_format", objectData);
        }

        SITOBJECTTYPE sitObjectType;
        int size;
        String content = parts[1];

        try {
            sitObjectType = SITOBJECTTYPE.fromString(headerParts[0]);
            size = Integer.parseInt(headerParts[1]);
        } catch (IllegalArgumentException e) {
            throw new SitException(1, "error.object.read", headerParts[0]);
        }

        return new SitObject(sitObjectType, size, content);
    }

    // 2. Check if exists
    public static boolean objectExists(Sha sha) {
        File file = getObjectPath(sha).toFile();
        return file.exists();
    }

    // 3. Delete
    public static boolean delete(Sha sha) {
        File file = getObjectPath(sha).toFile();
        return file.delete();
    }

    // 4. Save
    public static void save(SitObject sitObject) throws SitException {
        save(sitObject, false, false);
    }

    public static void save(SitObject sitObject, boolean force, boolean canThrowException) throws SitException {
        Sha sha = sitObject.getSha();
        File file = getObjectPath(sha).toFile();

        if (file.exists() && !force && canThrowException) {
            throw new SitException(1, "error.object.exist", sha.getShaString());
        }

        try {
            Files.createDirectories(file.toPath().getParent()); // Ensure parent directories exist

            // Write the object data to the file
            try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                 DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(fileOutputStream)) {
                deflaterOutputStream.write(sitObject.toByteArray());
                deflaterOutputStream.finish();
            }
        } catch (IOException e) {
            throw new SitException(1, "error.object.write", sha.getShaString());
        }
    }

    // -- Helper method
    private static Path getObjectPath(Sha sha) {
        String shaString = sha.getShaString();
        String objectFolder = shaString.substring(0, 2);
        String objectFile = shaString.substring(2);
        Path path = SitFileUtil.getObjectsDir().resolve(objectFolder).resolve(objectFile).normalize();

        if (!path.startsWith(SitFileUtil.getObjectsDir())) {
            throw new SitException(1, "error.file_object.illegal_access", shaString);
        }

        return path;
    }
}
