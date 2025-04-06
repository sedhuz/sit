package com.infinull.sit.util;

import com.infinull.sit.exception.SitException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha {
    private String sha;

    private static final String SHA1_REGEX = "^[0-9a-f]{40}$";
    private static final String SHA256_REGEX = "^[0-9a-f]{64}$";


    // --Constructor & Factory methods
    public Sha(String sha) {
        if (!isValidSha(sha)) {
            throw new SitException(1, "error.sha1.invalid", sha);
        }
        this.sha = sha;
    }

    public Sha(byte[] shaBytes) {
        this(convertBytesToHex(shaBytes));
    }

    public static Sha computeSha(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] shaBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return new Sha(shaBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new SitException(1, "error.sha1.compute.file", data);
        }
    }

    // --Actions
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

    public boolean isValidSha(String sha) {
        return sha.matches(SHA1_REGEX) || sha.matches(SHA256_REGEX);
    }

    @Override
    public String toString() {
        return sha;
    }
}