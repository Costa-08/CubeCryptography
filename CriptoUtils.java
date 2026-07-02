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

    public static int[] gerarChavePrivada(){
        byte[] bytesChave = new byte[32]; // 256 bits
        RANDOM.nextBytes(bytesChave);
        return bytesParaInts(bytesChave);
    }

    public static int[] gerarNonce192() {
        byte[] bytesNonce = new byte[24]; // 192 bits
        RANDOM.nextBytes(bytesNonce);        
        return bytesParaInts(bytesNonce);
    }

    public static int[] bytesParaInts(byte[] bytes) {
        if (bytes.length % 4 != 0) {
            throw new IllegalArgumentException("O array de bytes deve ser múltiplo de 4.");
        }
        int[] ints = new int[bytes.length / 4];
        ByteBuffer.wrap(bytes).asIntBuffer().get(ints);
        return ints;
    }

    public static byte[] intsParaBytes(int[] ints) {
        byte[] bytes = new byte[ints.length * 4];
        ByteBuffer.wrap(bytes).asIntBuffer().put(ints);
        return bytes;
    }

    public static void printArrayInt(int[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; i++) {
            // Imprime o int usando 8 caracteres hexadecimais
            System.out.printf("0x%08X", array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(" ]");
    }

    public static int[] gerarChaveDaString(String senhaUsuario) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(senhaUsuario.getBytes(StandardCharsets.UTF_8));
            return bytesParaInts(hashBytes);
            
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