package com.infinull.sit.util;

import com.infinull.sit.exception.SitException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class Sha {
    private final String sha;      // Hex string
    private final byte[] shaBytes; // Raw bytes

    private static final String SHA1_REGEX = "^[0-9a-f]{40}$";
    private static final String SHA256_REGEX = "^[0-9a-f]{64}$";

    // -- Constructor & Factory methods

    public Sha(String sha) {
        if (!isValidSha(sha)) {
            throw new SitException(1, "error.sha1.invalid", sha);
        }
        this.sha = sha.toLowerCase(Locale.ROOT);
        this.shaBytes = convertHexToBytes(this.sha);
    }

    public Sha(byte[] shaBytes) {
        this.shaBytes = shaBytes.clone();
        this.sha = convertBytesToHex(this.shaBytes);
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

    public static Sha computeSha(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] shaBytes = digest.digest(data);
            return new Sha(shaBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new SitException(1, "error.sha1.compute.file", new String(data));
        }
    }

    // -- Utility

    private static String convertBytesToHex(byte[] shaBytes) {
        StringBuilder shaStringBuilder = new StringBuilder();
        for (byte b : shaBytes) {
            shaStringBuilder.append(String.format("%02x", b));
        }
        return shaStringBuilder.toString();
    }

    private static byte[] convertHexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }

        return data;
    }

    // -- Accessors

    public String getShaString() {
        return sha;
    }

    public byte[] getShaBytes() {
        return shaBytes.clone(); // Safe copy
    }

    public boolean isValidSha(String sha) {
        return sha.matches(SHA1_REGEX) || sha.matches(SHA256_REGEX);
    }

    @Override
    public String toString() {
        return sha;
    }
}
