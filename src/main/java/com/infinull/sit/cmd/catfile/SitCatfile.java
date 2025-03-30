package com.infinull.sit.cmd.catfile;

import com.infinull.sit.cmd.SitCommand;
import com.infinull.sit.exception.SitException;
import com.infinull.sit.object.Sha;

public class SitCatfile implements SitCommand {

    public void run(String[] args) {
        if (args.length < 2) { // Ensure 2 args are give flag & <obj_sha>
            throw new SitException(1, "usage.catfile");
        }

        String flag = args[0];
        Sha sha = new Sha(args[1]);

        switch (flag) {
            case "-p":
                CatUtil.printObject(sha);
                break;
            case "-s":
                CatUtil.printSize(sha);
                break;
            case "-t":
                CatUtil.printType(sha);
                break;
            case "-e":
                CatUtil.checkExistence(sha);
                break;
            default:
                throw new SitException(1, "usage.catfile");
        }
    }
}
