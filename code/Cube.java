package code;

import java.util.Scanner;

public class Cube {

    private final int numRodadas = 16;

    final int[][] rota = {
        
        {0,0,0, 1,0,0, 2,0,0}, {0,0,1, 1,0,1, 2,0,1}, {0,0,2, 1,0,2, 2,0,2},
        {0,1,0, 1,1,0, 2,1,0}, {0,1,1, 1,1,1, 2,1,1}, {0,1,2, 1,1,2, 2,1,2},
        {0,2,0, 1,2,0, 2,2,0}, {0,2,1, 1,2,1, 2,2,1}, {0,2,2, 1,2,2, 2,2,2},

        {0,0,0, 0,1,0, 0,2,0}, {0,0,1, 0,1,1, 0,2,1}, {0,0,2, 0,1,2, 0,2,2},
        {1,0,0, 1,1,0, 1,2,0}, {1,0,1, 1,1,1, 1,2,1}, {1,0,2, 1,1,2, 1,2,2},
        {2,0,0, 2,1,0, 2,2,0}, {2,0,1, 2,1,1, 2,2,1}, {2,0,2, 2,1,2, 2,2,2},

        {0,0,0, 0,0,1, 0,0,2}, {0,1,0, 0,1,1, 0,1,2}, {0,2,0, 0,2,1, 0,2,2},
        {1,0,0, 1,0,1, 1,0,2}, {1,1,0, 1,1,1, 1,1,2}, {1,2,0, 1,2,1, 1,2,2},
        {2,0,0, 2,0,1, 2,0,2}, {2,1,0, 2,1,1, 2,1,2}, {2,2,0, 2,2,1, 2,2,2},

        {0,0,0, 1,1,1, 2,2,2}, {0,0,1, 1,1,2, 2,2,0}, {0,0,2, 1,1,0, 2,2,1},
        {0,1,0, 1,2,1, 2,0,2}, {0,1,1, 1,2,2, 2,0,0}, {0,1,2, 1,2,0, 2,0,1},
        {0,2,0, 1,0,1, 2,1,2}, {0,2,1, 1,0,2, 2,1,0}, {0,2,2, 1,0,0, 2,1,1}
    };

    private static final int[] CONSTANTES = { 
        0x885A308D, 
        0x313198A2, 
        0x2297931D, 0x082EFA98, 0x452821E6, 
        0xBE5466CF, 0xC0AC29B7, 0x3F84D5B5, 
        0x9216D5D9, 0x5A8E9C01, 0xFF4012B3
    };
    
private int[][][] iniCubo(int[] chave256, int[] nonce192, long contador) {
        int[][][] cubo = new int[3][3][3];

        cubo[1][1][1] = chave256[0];
        cubo[1][0][1] = chave256[1];
        cubo[1][2][1] = chave256[2];
        cubo[0][1][1] = chave256[3];
        cubo[2][1][1] = chave256[4];
        cubo[1][1][0] = chave256[5];
        cubo[1][1][2] = chave256[6]; 
        cubo[0][1][0] = chave256[7]; 

        cubo[0][0][0] = (int) (contador >>> 32);
        cubo[2][2][2] = (int) contador;

        cubo[2][0][0] = nonce192[0];
        cubo[0][2][0] = nonce192[1];
        cubo[0][0][2] = nonce192[2];
        cubo[2][2][0] = nonce192[3];
        cubo[2][0][2] = nonce192[4];
        cubo[0][2][2] = nonce192[5];

        cubo[1][0][0] = CONSTANTES[0];
        cubo[0][0][1] = CONSTANTES[1];
        cubo[2][0][1] = CONSTANTES[2];
        cubo[1][0][2] = CONSTANTES[3];

        cubo[2][1][0] = CONSTANTES[4];
        cubo[0][1][2] = CONSTANTES[5];
        cubo[2][1][2] = CONSTANTES[6];

        cubo[1][2][0] = CONSTANTES[7];
        cubo[0][2][1] = CONSTANTES[8];
        cubo[2][2][1] = CONSTANTES[9];
        cubo[1][2][2] = CONSTANTES[10];

        return cubo;
    }

    private int[][][] clonaCubo(int[][][] original){
        int[][][] novoCubo = new int[3][3][3];
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                System.arraycopy(original[i][j], 0, novoCubo[i][j], 0, 3);
            }
        }
        return novoCubo;
    }

    private int[][][] gerarKeystream(int[] chave, int[] nonce, long contador) {
        int[][][] estadoOriginal = iniCubo(chave, nonce, contador);
        int[][][] keyStream = clonaCubo(estadoOriginal);
        
        for (int rodada = 0; rodada < this.numRodadas; rodada++) {
            for (int[] trio : this.rota) {

                int a = keyStream[trio[0]][trio[1]][trio[2]];
                int b = keyStream[trio[3]][trio[4]][trio[5]];
                int c = keyStream[trio[6]][trio[7]][trio[8]];
                
                a += b; c ^= a; c = Integer.rotateLeft(c, 16);
                a += c; b ^= a; b = Integer.rotateLeft(b, 12);
                a += b; c ^= a; c = Integer.rotateLeft(c, 8);
                a += c; b ^= a; b = Integer.rotateLeft(b, 7);
                
                keyStream[trio[0]][trio[1]][trio[2]] = a;
                keyStream[trio[3]][trio[4]][trio[5]] = b;
                keyStream[trio[6]][trio[7]][trio[8]] = c;
            }
        }
        
        // Davies Meyer
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    keyStream[x][y][z] += estadoOriginal[x][y][z];
                }
            }
        }
        
        return keyStream;
    }

    private byte[] processarDados(byte[] entrada, int[] chave, int[] nonce) {
        byte[] saida = new byte[entrada.length];
        long contador = 0;        
        int[][][] blocoKeystream = null;

        for (int i = 0; i < entrada.length; i++) {

            int posNoBloco = i % 108;

            if (posNoBloco == 0) {
                blocoKeystream = gerarKeystream(chave, nonce, contador);
                contador++;
            }

            int indicePalavra = posNoBloco / 4;
            int indiceByte = posNoBloco % 4;

            int z = indicePalavra / 9;
            int y = (indicePalavra % 9) / 3;
            int x = indicePalavra % 3;

            int palavraAtual = blocoKeystream[x][y][z];
            int shift = indiceByte * 8;
            byte byteDoKeystream = (byte) ((palavraAtual >>> shift) & 0xFF);

            saida[i] = (byte) (entrada[i] ^ byteDoKeystream);
        }

        return saida;
    }

    private static void printMenu(){
        System.out.println("--- Menu ---");
        System.out.println("Digite a opção desejada: ");
        System.out.println("0 - Escolher chave privada");
        System.out.println("1 - Escolher texto claro");
        System.out.println("2 - Cifrar texto claro");
        System.out.println("3 - Decifrar texto cifrado");
        System.out.println("4 - Sair");
        System.out.print("Opção escolhida: ");
    }

    private static void decifraTexto(Cube c, int[] chave, byte[] cifrado){
        if (chave!=null){
            if (cifrado!=null){
        
                byte[] nonceBytes = new byte[24];
                System.arraycopy(cifrado, 0, nonceBytes, 0, 24);
                int[] nonceRecuperado = CriptoUtils.bytesParaInts(nonceBytes);

                byte[] cifradoPuro = new byte[cifrado.length - 24];
                System.arraycopy(cifrado, 24, cifradoPuro, 0, cifradoPuro.length);

                byte[] decifrado = c.processarDados(cifradoPuro, chave, nonceRecuperado);
                System.out.println("Texto decifrado: " + CriptoUtils.bytesParaString(decifrado));

            } else System.out.println("Texto claro não escolhido");
        } else System.out.println("Chave privada não escolhida");
    }
    
    private static byte[] cifrarTexto(Cube c, int[] chave, byte[] claro){
        if (chave!=null){
            if (claro!=null){

                int[] nonceSessao = CriptoUtils.gerarNonce192();
                byte[] nonceBytes = CriptoUtils.intsParaBytes(nonceSessao);

                byte[] cifradoPuro = c.processarDados(claro, chave, nonceSessao);
                
                byte[] cifradoConcatenado = new byte[nonceBytes.length + cifradoPuro.length];
                System.arraycopy(nonceBytes, 0, cifradoConcatenado, 0, nonceBytes.length);
                System.arraycopy(cifradoPuro, 0, cifradoConcatenado, nonceBytes.length, cifradoPuro.length);
                
                System.out.println("Texto cifrado (Hex): "+ CriptoUtils.bytesParaHex(cifradoConcatenado));
                return cifradoConcatenado;

            } else System.out.println("Texto claro não escolhido");
        } else System.out.println("Chave privada não escolhida");
        return null;
    }

    private static byte[] escolherMsgClara(Scanner scan){
        scan.nextLine();
        System.out.print("Digite o texto claro: ");
        String textoClaro = scan.nextLine();
        return CriptoUtils.stringParaBytes(textoClaro);
    }

    private static int[] escolherChavePrivada(Scanner scan){
        System.out.println("Digite a opção desejada: ");
        System.out.println("0 - Escolher chave privada");
        System.out.println("1 - Gerar chave privada aleatória");
        System.out.print("Opção escolhida: ");

        int opc = scan.nextInt();
        int[] chavePrivada = null;
        while (chavePrivada==null){
            switch (opc){
                
                case 0 -> {
                    scan.nextLine();
                    System.out.println("Digite a chave privada (256 bits): ");
                    String chaveStr = scan.nextLine();
                    chavePrivada = CriptoUtils.gerarChaveDaString(chaveStr);
                    System.out.print("Chave privada: ");
                    CriptoUtils.printArrayInt(chavePrivada);
                }

                case 1 -> {
                    chavePrivada = CriptoUtils.gerarChavePrivada();
                    System.out.print("Chave privada gerada: ");
                    CriptoUtils.printArrayInt(chavePrivada);
                }

                default ->{
                    System.out.println("Opção inválida");
                    System.out.print("Nova opção escolhida: ");
                    opc = scan.nextInt();
                }

            }
        }

        return chavePrivada;
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Cube cifraCubo = new Cube();

        int[] chavePrivada = null;
        byte[] msgClara = null;
        byte[] msgCifrada = null;
        
        int opc = -1;

        while (opc!=4){
            printMenu();
            opc = scan.nextInt();
            switch (opc){
                case 0 -> chavePrivada = escolherChavePrivada(scan);
                case 1 -> msgClara = escolherMsgClara(scan);
                case 2 -> msgCifrada = cifrarTexto(cifraCubo, chavePrivada, msgClara);
                case 3 -> decifraTexto(cifraCubo, chavePrivada, msgCifrada);
                case 4 -> {}
                default -> System.out.println("Opção inválida");
            }
        }

        scan.close();
    }
}