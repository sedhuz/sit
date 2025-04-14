package com.infinull.sit.commands.lstree;

import com.infinull.sit.commands.SitCommand;
import com.infinull.sit.exception.SitException;
import com.infinull.sit.util.Sha;

public class LstreeCommand implements SitCommand {

    public void run(String[] args) {
        if (args.length < 2) {
            throw new SitException(1, "usage.lstree");
        }

        Sha sha = new Sha(args[1]);
        LstreeUtil.printTreeContent(sha);
    }
}
