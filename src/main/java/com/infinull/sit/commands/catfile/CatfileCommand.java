package com.infinull.sit.commands.catfile;

import com.infinull.sit.commands.SitCommand;
import com.infinull.sit.exception.SitException;
import com.infinull.sit.util.Sha;

public class CatfileCommand implements SitCommand {

    public void run(String[] args) {
        if (args.length < 2) { // Ensure 2 args are give flag & <obj_sha>
            throw new SitException(1, "usage.catfile");
        }

        String flag = args[0];
        Sha sha = new Sha(args[1]);

        switch (flag) {
            case "-p":
                CatfileUtil.printObject(sha);
                break;
            case "-s":
                CatfileUtil.printSize(sha);
                break;
            case "-t":
                CatfileUtil.printType(sha);
                break;
            case "-e":
                CatfileUtil.checkExistence(sha);
                break;
            default:
                throw new SitException(1, "usage.catfile");
        }
    }
}
