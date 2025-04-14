package com.infinull.sit.commands.view;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.object.SitObject;
import com.infinull.sit.store.ObjectStore;
import com.infinull.sit.util.Sha;

public class ViewUtil {
    static void printObject(Sha sha) {
        SitObject sitObject = ObjectStore.get(sha);
        System.out.printf("%s %s\n%s%n", sitObject.getType(), sitObject.getSize(), sitObject.getContent());
    }

    static void printContent(Sha sha) {
        SitObject sitObject = ObjectStore.get(sha);
        System.out.println(sitObject.getContent());
    }

    static void printSize(Sha sha) {
        SitObject sitObject = ObjectStore.get(sha);
        System.out.println(sitObject.getSize());
    }

    static void printType(Sha sha) {
        SitObject sitObject = ObjectStore.get(sha);
        System.out.println(sitObject.getType());
    }

    static void checkExistence(Sha sha) {
        if (!ObjectStore.objectExists(sha)) {
            throw new SitException(1, "");
        } else {
            throw new SitException(0, "");
        }
    }
}
