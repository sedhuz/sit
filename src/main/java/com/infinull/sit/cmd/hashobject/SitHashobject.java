package com.infinull.sit.cmd.hashobject;

import com.infinull.sit.cmd.SitCommand;
import com.infinull.sit.exception.SitException;

public class SitHashobject implements SitCommand {

    @Override
    public void run(String[] args) {
        if (args.length < 2) { // Ensure 2 args are give flag & <obj_sha>
            throw new SitException(1, "usage.hashobject");
        }

//        String flag = args[0];
//        String filePathInRepo = args[1];
//
//        if (flag.charAt(0) == '-') {
//
//        }
//
//        switch (flag) {
//            case "-w":
//                CatUtil.printObject(filePath);
//            default:
//                throw new SitException(1, "usage.hashobject");
//        }
    }
}
