package com.infinull.sit.cmd.init;

import com.infinull.sit.cmd.SitCommand;
import com.infinull.sit.exception.SitException;
import com.infinull.sit.message.MessageUtil;

import java.io.*;

public class SitInit implements SitCommand {

    public static void run(String[] args) {
        run();
    }

    public static void run() {
        String cwd = System.getProperty("user.dir");
        File sitDir = new File(cwd, ".git");

        if (sitDir.exists()) {
            if (sitDir.isDirectory()) {
                throw new SitException(1, "git.exists");
            } else {
                throw new SitException(1, "git.is.file");
            }
        } else {
            if (sitDir.mkdirs()) {
                new File(sitDir, "objects").mkdirs();
                new File(sitDir, "refs").mkdirs();
                new File(sitDir, "refs/heads").mkdirs();
                new File(sitDir, "hooks").mkdirs();
                new File(sitDir, "info").mkdirs();

                File configFile = new File(sitDir, "config");
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    throw new SitException(1, "config.error");
                }

                File headFile = new File(sitDir, "HEAD");
                try (FileWriter writer = new FileWriter(headFile)) {
                    writer.write("ref: refs/heads/master");
                } catch (IOException e) {
                    throw new SitException(1, "head.error");
                }

				MessageUtil.printMsg("init.success", sitDir.getPath());
            } else {
                throw new SitException(1, "no.git.dir");
            }
        }
    }
}
