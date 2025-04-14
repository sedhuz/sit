package com.infinull.sit.commands.init;

import com.infinull.sit.exception.SitException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class InitUtil {
    public static void createHeadFile(File gitDir) {
        File headFile = new File(gitDir, "HEAD");
        try (FileWriter writer = new FileWriter(headFile)) {
            writer.write("ref: refs/heads/master\n");
        } catch (IOException e) {
            throw new SitException(1, "error.head.create");
        }
    }

    public static void createSitMeta(File gitDir) {
        File sitFile = new File(gitDir, "sitmeta");
        String timestamp = Instant.now().toString();
        String user = getSystemUser();

        try (FileWriter writer = new FileWriter(sitFile)) {
            writer.write("Sit repository created at: " + timestamp + "\n");
            writer.write("Created by: " + user + "\n");
        } catch (IOException e) {
            throw new SitException(1, "error.sitmeta.create");
        }
    }

    public static String getSystemUser() {
        return System.getProperty("user.name", "unknown");
    }
}
