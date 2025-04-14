package com.infinull.sit.commands.hashobject;

import com.infinull.sit.commands.SitCommand;
import com.infinull.sit.exception.SitException;

public class HashobjectCommand implements SitCommand {

    @Override
    public void run(String[] args) {
        if (args.length < 1 || args.length > 2) {
            throw new SitException(1, "usage.hashobject");
        }

        String filePath = args[0];
        boolean writeToGit = args.length == 2 && "-w".equals(args[0]);
        if (writeToGit) {
            filePath = args[1];
        }

        String sha = HashobjectUtil.getSha(filePath, writeToGit);
        System.out.println(sha);
    }
}
