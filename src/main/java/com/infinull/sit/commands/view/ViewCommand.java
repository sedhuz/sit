package com.infinull.sit.commands.view;

import com.infinull.sit.commands.SitCommand;
import com.infinull.sit.exception.SitException;
import com.infinull.sit.util.Sha;

public class ViewCommand implements SitCommand {

    public void run(String[] args) {
        if (args.length < 1 || args.length > 2) { // Ensure valid input
            throw new SitException(1, "usage.view");
        }

        if (args.length == 1) { // Handle `git view <sha>` case
            Sha sha = new Sha(args[0]);
            ViewUtil.printObject(sha);
            return;
        }

        String flag = args[0];
        Sha sha = new Sha(args[1]);

        switch (flag) {
            case "-p":
                ViewUtil.printContent(sha);
                break;
            case "-s":
                ViewUtil.printSize(sha);
                break;
            case "-t":
                ViewUtil.printType(sha);
                break;
            case "-e":
                ViewUtil.checkExistence(sha);
                break;
            default:
                throw new SitException(1, "usage.view");
        }
    }
}
