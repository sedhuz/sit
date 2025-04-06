package com.infinull.sit.util;

import com.infinull.sit.exception.SitException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for {@link com.infinull.sit.util.Sha} computing SHA-1 hashes used in the SIT version control system.
 *
 * <p>
 * The design of this class follows the naming conventions of other utility classes in the Java
 * standard library, such as {@link java.util.Arrays}, and is intended to be stateless.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * byte[] content = "Hello, SIT!".getBytes(StandardCharsets.UTF_8);
 * Sha sha = Sha.computeSha(content);
 * System.out.println("SHA-1: " + sha.getShaString());
 * }</pre>
 *
 * @author Infinull
 * @see java.security.MessageDigest
 * @see com.infinull.sit.object.SitObject
 */


public class Shas {
    public static Sha computeSha(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] shaBytes = digest.digest(data);
            return new Sha(shaBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new SitException(1, "error.sha1.compute.data", new String(data, StandardCharsets.UTF_8));
        }
    }

    public static Sha computeSha(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] shaBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return new Sha(shaBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new SitException(1, "error.sha1.compute.data", data);
        }
    }
}
