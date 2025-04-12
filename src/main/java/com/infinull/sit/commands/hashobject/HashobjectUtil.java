package com.infinull.sit.commands.hashobject;

import com.infinull.sit.object.SitObject;
import com.infinull.sit.persistence.SitFilePersistence;
import com.infinull.sit.persistence.SitObjectPersistence;

public class HashobjectUtil {
    public static String getSha(String filePath, boolean canWriteFile) {
        SitObject sitObject = SitFilePersistence.get(filePath).toSitObject();
        if (canWriteFile) {
            SitObjectPersistence.save(sitObject);
        }
        return sitObject.getSha().getShaString();
    }
}