package com.infinull.sit.commands.hashobject;

import com.infinull.sit.object.SitObject;
import com.infinull.sit.store.FileStore;
import com.infinull.sit.store.ObjectStore;

public class HashobjectUtil {
    public static String getSha(String filePath, boolean canWriteFile) {
        SitObject sitObject = FileStore.get(filePath);
        if (canWriteFile) {
            ObjectStore.save(sitObject);
        }
        return sitObject.getSha().toString();
    }
}