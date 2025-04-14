package com.infinull.sit.commands.hash;

import com.infinull.sit.commands.SitCommand;
import com.infinull.sit.exception.SitException;

public class HashCommand implements SitCommand {

    @Override
    public void run(String[] args) {
        if (args.length < 1 || args.length > 2) {
            throw new SitException(1, "usage.hash");
        }

        String filePath = args[0];
        boolean writeToGit = args.length == 2 && "-w".equals(args[0]);
        if (writeToGit) {
            filePath = args[1];
        }

        String sha = HashUtil.getSha(filePath, writeToGit);
        System.out.println(sha);
    }
}
