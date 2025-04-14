package com.infinull.sit.util;

import com.infinull.sit.exception.SitException;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public final class Sha {
    private final String sha;      // Hex string
    private final byte[] shaBytes; // Raw bytes

    private static final String SHA1_REGEX = "^[0-9a-f]{40}$";
    private static final String SHA256_REGEX = "^[0-9a-f]{64}$";

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

    public static Sha computeSha(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(data);
            return new Sha(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new SitException(1, "error.sha1.compute.file", safeUtf8(data));
        }
    }

    public String toString() {
        return sha;
    }

    public byte[] toBytes() {
        return shaBytes.clone();
    }

    // -- Static Utils

    private static String convertBytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] convertHexToBytes(String hex) {
        int len = hex.length();
        byte[] result = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            result[i / 2] = (byte) Integer.parseInt(hex, i, i + 2, 16);
        }
        return result;
    }

    private static String safeUtf8(byte[] data) {
        try {
            return StandardCharsets.UTF_8
                    .newDecoder()
                    .onMalformedInput(CodingErrorAction.REPLACE)
                    .onUnmappableCharacter(CodingErrorAction.REPLACE)
                    .decode(ByteBuffer.wrap(data))
                    .toString();
        } catch (CharacterCodingException ignored) {
            return "[decode failed for safe UTF-8]";
        }
    }

    public static boolean isValidSha(String sha) {
        String s = sha.toLowerCase(Locale.ROOT);
        return s.matches(SHA1_REGEX) || s.matches(SHA256_REGEX);
    }

    public boolean equals(Sha other) {
        return this.sha.equals(other.sha);
    }
}
