package com.infinull.sit.cmd.catfile;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.message.MessageUtil;
import com.infinull.sit.object.Sha;
import com.infinull.sit.object.SitObject;

public class CatUtil {
    static void printObject(Sha shaObject) {
        SitObject sitObject = new SitObject(shaObject);
        System.out.println(sitObject.getContent());
    }

    static void printSize(Sha sha) {
        SitObject sitObject = new SitObject(sha);
        System.out.println(sitObject.getSize());
    }

    static void printType(Sha sha) {
        SitObject sitObject = new SitObject(sha);
        System.out.println(sitObject.getType().getTypeString());
    }

    static void checkExistence(Sha sha) {
        try {
            new SitObject(sha);
        } catch (SitException e) {
            if (e.getMessage().equals(MessageUtil.getMsg("error.object.exist", sha.getShaString()))) {
                throw new SitException(1, "");
            }
        }
    }
}
