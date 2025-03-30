package com.infinull.sit.cmd.hashobject;

import com.infinull.sit.object.BlobObject;

public class HashObjectUtil {
    public static String getSha(String filePath, boolean canWriteFile) {
        BlobObject blobObject = new BlobObject(filePath);
        if (canWriteFile) {
            blobObject.persistToSit();
        }
        return blobObject.getSha().getShaString();
    }
}