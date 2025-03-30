package com.infinull.sit.object;

import com.infinull.sit.exception.SitException;

public class Sha {
    private String sha;

    private static final String SHA1_REGEX = "^[0-9a-f]{40}$";
    private static final String SHA256_REGEX = "^[0-9a-f]{64}$";


    public Sha(String sha) {
        if (!isValidSha(sha)) {
            throw new SitException(1, "error.sha1.invalid", sha);
        }
        this.sha = sha;
    }

    public Sha(byte[] shaBytes) {
        this(convertBytesToHex(shaBytes));
    }

    private static String convertBytesToHex(byte[] shaBytes) {
        StringBuilder shaStringBuilder = new StringBuilder();
        for (byte b : shaBytes) {
            shaStringBuilder.append(String.format("%02x", b));
        }
        return shaStringBuilder.toString();
    }

    public String getShaString() {
        return sha;
    }

    private boolean isValidSha(String sha) {
        return sha.matches(SHA1_REGEX) || sha.matches(SHA256_REGEX);
    }

    @Override
    public String toString() {
        return sha;
    }
}