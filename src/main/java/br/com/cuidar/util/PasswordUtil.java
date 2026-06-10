package br.com.cuidar.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilitário para hashing e verificação de senhas usando PBKDF2-HMAC-SHA256.
 * <p>
 * Formato armazenado: {@code PBKDF2$<iterações>$<saltBase64>$<hashBase64>}.
 * Permite identificar senhas legadas (texto puro) para migração transparente.
 */
public final class PasswordUtil {

    private static final String ALGO = "PBKDF2WithHmacSHA256";
    private static final String PREFIX = "PBKDF2$";
    private static final int ITERATIONS = 120_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;

    private PasswordUtil() {}

    public static String hash(String plain) {
        if (plain == null) throw new IllegalArgumentException("Senha não pode ser nula.");
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] dk = derive(plain.toCharArray(), salt, ITERATIONS, KEY_BITS);
        return PREFIX + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt)
                + "$" + Base64.getEncoder().encodeToString(dk);
    }

    public static boolean verify(String plain, String stored) {
        if (plain == null || stored == null) return false;
        if (!isHashed(stored)) return false;
        String[] parts = stored.split("\\$");
        if (parts.length != 4) return false;
        try {
            int iters = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            byte[] actual = derive(plain.toCharArray(), salt, iters, expected.length * 8);
            return constantTimeEquals(expected, actual);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isHashed(String stored) {
        return stored != null && stored.startsWith(PREFIX);
    }

    private static byte[] derive(char[] pwd, byte[] salt, int iters, int keyBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(pwd, salt, iters, keyBits);
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGO);
            return f.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Falha ao derivar hash da senha.", e);
        }
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int r = 0;
        for (int i = 0; i < a.length; i++) r |= a[i] ^ b[i];
        return r == 0;
    }
}
