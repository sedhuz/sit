package com.infinull.sit.commands.lstree;

import com.infinull.sit.object.SitObject;
import com.infinull.sit.persistence.SitObjectPersistence;
import com.infinull.sit.util.Sha;

public class LstreeUtil {
    public static void printTreeContent(Sha sha) {
        SitObject sitObject = SitObjectPersistence.get(sha);
    }
}
