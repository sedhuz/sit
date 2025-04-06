package com.infinull.sit.commands.hashobject;

import com.infinull.sit.object.BlobObject;

public class HashobjectUtil {
    public static String getSha(String filePath, boolean canWriteFile) {
        BlobObject blobObject = new BlobObject(filePath);
        if (canWriteFile) {
            blobObject.objectSave();
        }
        return blobObject.getSha().getShaString();
    }
}