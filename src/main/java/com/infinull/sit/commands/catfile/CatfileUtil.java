package com.infinull.sit.commands.catfile;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.object.SitObject;
import com.infinull.sit.persistence.SitObjectPersistence;
import com.infinull.sit.util.Sha;

public class CatfileUtil {
    static void printObject(Sha sha) {
        SitObject sitObject = SitObjectPersistence.get(sha);
        System.out.println(sitObject.getContent());
    }

    static void printSize(Sha sha) {
        SitObject sitObject = SitObjectPersistence.get(sha);
        System.out.println(sitObject.getSize());
    }

    static void printType(Sha sha) {
        SitObject sitObject = SitObjectPersistence.get(sha);
        System.out.println(sitObject.getObjectType().getTypeString());
    }

    static void checkExistence(Sha sha) {
        if (!SitObjectPersistence.objectExists(sha)) {
            throw new SitException(1, "");
        } else {
            throw new SitException(0, "");
        }
    }
}
