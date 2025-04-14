package com.infinull.sit.store;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.object.SitObject;
import com.infinull.sit.object.SitObjectFactory;
import com.infinull.sit.util.Sha;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ObjectStore {

    // 1. Get Object
    public static SitObject get(Sha sha) {
        Path objectPath = PathStore.getObjectPath(sha);
        if (!Files.exists(objectPath)) {
            throw new SitException(1, "error.object.not_exist", sha.toString());
        }

        byte[] decompressed = decompressObject(objectPath);
        return SitObjectFactory.parse(decompressed);
    }

    // 1.1 Get Helpers
    private static byte[] decompressObject(Path path) {
        try (InputStream fis = Files.newInputStream(path);
             InflaterInputStream inflater = new InflaterInputStream(fis);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = inflater.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new SitException(1, "error.object.read", path.toString());
        }
    }

    // 2. Check If Exists
    public static boolean objectExists(Sha sha) {
        File file = PathStore.getObjectPath(sha).toFile();
        return file.exists();
    }

    // 3. Delete
    public static boolean delete(Sha sha) {
        File file = PathStore.getObjectPath(sha).toFile();
        return file.delete();
    }

    // 4. Save Object
    public static void save(SitObject object) {
        save(object, false, false);
    }

    public static void save(SitObject object, boolean force, boolean canThrowException) {
        Path objectPath = PathStore.getObjectPath(object.getSha());

        if (objectPath.toFile().exists() && !force) {
            if (canThrowException) {
                throw new SitException(1, "error.object.already_exist", objectPath.toString());
            } else {
                return;
            }
        }

        try {
            Files.createDirectories(objectPath.getParent());
        } catch (IOException e) {
            throw new SitException(1, "error.object.create_directory", objectPath.getParent().toString());
        }

        try (OutputStream fos = Files.newOutputStream(objectPath);
             DeflaterOutputStream deflater = new DeflaterOutputStream(fos)) {
            byte[] data = object.serialize();
            deflater.write(data);
        } catch (IOException e) {
            throw new SitException(1, "error.object.write", objectPath.toString());
        }
    }
}
