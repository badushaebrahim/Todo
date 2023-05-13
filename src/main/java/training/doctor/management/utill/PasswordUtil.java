package training.doctor.management.utill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
@Slf4j
@Service
public class PasswordUtil {

    private static final String algorithm = "AES/CBC/PKCS5Padding";
    private static final byte[] secretKeyBytes = "my-secret-key".getBytes(StandardCharsets.UTF_8);

    public  String decryptPassword(String encryptedPassword) throws Exception {
        String[] parts = encryptedPassword.split(":");
        byte[] ivBytes = hexToBytes(parts[0]);
        byte[] encryptedBytes = hexToBytes(parts[1]);

        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKeySpec secretKey = new SecretKeySpec(secretKeyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private  byte[] hexToBytes(String hex) {
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }

}