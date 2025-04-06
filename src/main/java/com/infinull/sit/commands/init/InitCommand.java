package com.infinull.sit.commands.init;

import com.infinull.sit.commands.SitCommand;
import com.infinull.sit.exception.SitException;
import com.infinull.sit.message.MessageUtil;

import java.io.File;
import java.io.IOException;

public class InitCommand implements SitCommand {

    public void run(String[] args) {
        run();
    }

    public void run() {
        String cwd = System.getProperty("user.dir");
        File gitDir = new File(cwd, ".git");

        if (gitDir.exists()) {
            if (gitDir.isDirectory()) {
                throw new SitException(1, "git.exists");
            } else {
                throw new SitException(1, "git.is.file");
            }
        } else {
            if (gitDir.mkdirs()) {
                new File(gitDir, "objects").mkdirs();
                new File(gitDir, "refs").mkdirs();
                new File(gitDir, "refs/heads").mkdirs();
                new File(gitDir, "hooks").mkdirs();
                new File(gitDir, "info").mkdirs();

                File configFile = new File(gitDir, "config");
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    throw new SitException(1, "error.config.create");
                }

                InitUtil.createHeadFile(gitDir);
                InitUtil.createSitMeta(gitDir);

                MessageUtil.printMsg("success.init", gitDir.getPath());
            } else {
                throw new SitException(1, "no.git.dir");
            }
        }
    }
}
