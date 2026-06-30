import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CriptoUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static byte[] stringParaBytes(String texto) {
        return texto.getBytes(StandardCharsets.UTF_8);
    }

    public static String bytesParaString(byte[] dados) {
        return new String(dados, StandardCharsets.UTF_8);
    }

    public static long[] gerarChavePrivada(){
        byte[] bytesChave = new byte[32];
        RANDOM.nextBytes(bytesChave);
        return bytesParaLongs(bytesChave);
    }

    public static long[] gerarNonce192() {
        byte[] bytesNonce = new byte[24];
        RANDOM.nextBytes(bytesNonce);        
        return bytesParaLongs(bytesNonce);
    }

    public static long[] bytesParaLongs(byte[] bytes) {
        if (bytes.length % 8 != 0) {
            throw new IllegalArgumentException("O array de bytes deve ser múltiplo de 8.");
        }
        long[] longs = new long[bytes.length / 8];
        ByteBuffer.wrap(bytes).asLongBuffer().get(longs);
        return longs;
    }

    public static byte[] longsParaBytes(long[] longs) {
        byte[] bytes = new byte[longs.length * 8];
        ByteBuffer.wrap(bytes).asLongBuffer().put(longs);
        return bytes;
    }

    public static void printArrayLong(long[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; i++) {
            System.out.printf("0x%016X", array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(" ]");
    }

    public static long[] gerarChaveDaString(String senhaUsuario) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            byte[] hashBytes = digest.digest(senhaUsuario.getBytes(StandardCharsets.UTF_8));
            
            return bytesParaLongs(hashBytes);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 não encontrado", e);
        }
    }

    public static String bytesParaHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }

}