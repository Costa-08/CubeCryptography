import java.util.Scanner;

public class Cube{

    final int[][] trios = {

        {0,0,0, 1,0,0, 2,0,0},
        {0,0,0, 0,1,0, 0,2,0},
        {0,0,0, 0,0,1, 0,0,2},
        {0,0,0, 1,1,0, 2,2,0},
        {0,2,0, 1,1,0, 2,0,0},
        {0,0,1, 1,0,1, 2,0,1},
        {0,0,1, 0,1,1, 0,2,1},
        {0,1,0, 0,1,1, 0,1,2},
        {0,0,1, 1,1,1, 2,2,1},      
        {0,2,1, 1,1,1, 2,0,1},   

        {0,0,0, 1,1,1, 2,2,2}, // Diagonal Interna 1

        {0,0,2, 1,0,2, 2,0,2},
        {0,0,2, 0,1,2, 0,2,2},
        {0,2,0, 0,2,1, 0,2,2},
        {0,0,2, 1,1,2, 2,2,2},     
        {0,2,2, 1,1,2, 2,0,2},       
        {0,1,0, 1,1,0, 2,1,0},
        {1,0,0, 1,1,0, 1,2,0},
        {1,0,0, 1,0,1, 1,0,2},
        {0,0,0, 1,0,1, 2,0,2},     
        {0,0,2, 1,0,1, 2,0,0},        

        {0,2,0, 1,1,1, 2,0,2}, // Diagonal Interna 2

        
        {0,1,1, 1,1,1, 2,1,1},      
        {1,0,1, 1,1,1, 1,2,1},
        {1,1,0, 1,1,1, 1,1,2},
        {0,1,0, 1,1,1, 2,1,2},       
        {0,1,2, 1,1,1, 2,1,0}, 
        
        {0,1,2, 1,1,2, 2,1,2},
        {1,0,2, 1,1,2, 1,2,2},
        {1,2,0, 1,2,1, 1,2,2},
        {0,2,0, 1,2,1, 2,2,2},        
        {0,2,2, 1,2,1, 2,2,0}, 

        {2,0,0, 1,1,1, 0,2,2}, //Diagonal interna 3

        {0,2,0, 1,2,0, 2,2,0},
        {2,0,0, 2,1,0, 2,2,0},
        {2,0,0, 2,0,1, 2,0,2},
        {0,0,0, 0,1,1, 0,2,2},        
        {0,0,2, 0,1,1, 0,2,0}, 

        {0,2,1, 1,2,1, 2,2,1},
        {2,0,1, 2,1,1, 2,2,1},
        {2,1,0, 2,1,1, 2,1,2},
        {1,0,0, 1,1,1, 1,2,2},         
        {1,0,2, 1,1,1, 1,2,0}, 
        
        {2,2,0, 1,1,1, 0,0,2}, //Diagonal Interna 4

        {0,2,2, 1,2,2, 2,2,2},
        {2,0,2, 2,1,2, 2,2,2},
        {2,2,0, 2,2,1, 2,2,2},
        {2,0,0, 2,1,1, 2,2,2},
        {2,0,2, 2,1,1, 2,2,0} 
    };

    private static final long[] CONSTANTES_TOPO = { // Constantes Aleatórias (devem ficar para caso chave=0)
        0x3243F6A8885A308DL, 0x313198A2E0370734L, 0x4A4093822299F31DL,
        0x082EFA98EC4E6C89L, 0x452821E638D01377L, 0xBE5466CF34E90C6CL,
        0xC0AC29B7C97C50DDL, 0x3F84D5B5B5470917L, 0x9216D5D98979FB1BL
    };
    
    private static final long[] CONSTANTES_BASE = { // removíveis caso queira expandir o tamanho da chave
        0x6A09E667F3BCC908L, 0xBB67AE8584CAA73BL, 0x3C6EF372FE94F82BL,
        0xA54FF53A5F1D36F1L, 0x510E527FADE682D1L, 0x9B05688C2B3E6C1FL,
        0x1F83D9ABFB41BD6BL, 0x5BE0CD19137E2179L, 0xFD089D82E0608A7BL
    };

    private long[][][] iniCubo(long[] chave256, long[] nonce192, long contador) { // Topologia Tridimensional
        long[][][] cubo = new long[3][3][3];
        
        int i = 0;
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                cubo[x][0][z] = CONSTANTES_TOPO[i++];
            }
        }
        
        cubo[0][1][0] = chave256[0];
        cubo[1][1][0] = chave256[1];
        cubo[2][1][0] = chave256[2];
        cubo[0][1][1] = chave256[3]; 
        
        cubo[1][1][1] = nonce192[0];
        cubo[2][1][1] = nonce192[1];
        cubo[0][1][2] = nonce192[2];
        
        cubo[1][1][2] = contador;       
        cubo[2][1][2] = 0L;             

        i = 0;
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                cubo[x][2][z] = CONSTANTES_BASE[i++];
            }
        }
        
        return cubo;
    }

    private long[][][] clonaCubo(long[][][] original){
        long[][][] novoCubo = new long[3][3][3];
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                System.arraycopy(original[i][j], 0, novoCubo[i][j], 0, 3); // sugestão da IDE, não sabia que existia.
            }
        }
        return novoCubo;
    }

    private long[][][] gerarKeystream(long[] chave, long[] nonce, long contador) {

        long[][][] estadoOriginal = iniCubo(chave, nonce, contador);
        
        long[][][] keyStream = clonaCubo(estadoOriginal);
        
        for (int rodada = 0; rodada < 8; rodada++) {
            for (int[] rota : this.trios) {

                long a = keyStream[rota[0]][rota[1]][rota[2]];
                long b = keyStream[rota[3]][rota[4]][rota[5]];
                long c = keyStream[rota[6]][rota[7]][rota[8]];
                
                // ARX
                a += b; c ^= a; c = Long.rotateLeft(c, 32);
                a += c; b ^= a; b = Long.rotateLeft(b, 24);
                a += b; c ^= a; c = Long.rotateLeft(c, 14);
                a += c; b ^= a; b = Long.rotateLeft(b, 11);
                
                keyStream[rota[0]][rota[1]][rota[2]] = a;
                keyStream[rota[3]][rota[4]][rota[5]] = b;
                keyStream[rota[6]][rota[7]][rota[8]] = c;
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

    private byte[] processarDados(byte[] entrada, long[] chave, long[] nonce) {

        byte[] saida = new byte[entrada.length];
        long contador = 0;        
        long[][][] blocoKeystream = null;

        for (int i = 0; i < entrada.length; i++) {
            int posNoBloco = i % 216;

            if (posNoBloco == 0) {
                blocoKeystream = gerarKeystream(chave, nonce, contador);
                contador++;
            }

            int indicePalavra = posNoBloco / 8;
            int indiceByte = posNoBloco % 8;

            int z = indicePalavra / 9;
            int y = (indicePalavra % 9) / 3;
            int x = indicePalavra % 3;

            long palavraAtual = blocoKeystream[x][y][z];
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

    private static void decifraTexto(Cube c, long[] chave, byte[] cifrado){
        if (chave!=null){
            if (cifrado!=null){
        
                byte[] nonceBytes = new byte[24];
                System.arraycopy(cifrado, 0, nonceBytes, 0, 24);
                long[] nonceRecuperado = CriptoUtils.bytesParaLongs(nonceBytes);

                byte[] cifradoPuro = new byte[cifrado.length - 24];
                System.arraycopy(cifrado, 24, cifradoPuro, 0, cifradoPuro.length);

                byte[] decifrado = c.processarDados(cifradoPuro, chave, nonceRecuperado);
                System.out.println("Texto decifrado: " + CriptoUtils.bytesParaString(decifrado));

            } else System.out.println("Texto claro não escolhido");
        } else System.out.println("Chave privada não escolhida");
    }
    
    private static byte[] cifrarTexto(Cube c, long[] chave, byte[] claro){
        if (chave!=null){
            if (claro!=null){

                long[] nonceSessao = CriptoUtils.gerarNonce192();
                byte[] nonceBytes = CriptoUtils.longsParaBytes(nonceSessao);

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

    private static long[] escolherChavePrivada(Scanner scan){
        System.out.println("Digite a opção desejada: ");
        System.out.println("0 - Escolher chave privada");
        System.out.println("1 - Gerar chave privada aleatória");
        System.out.print("Opção escolhida: ");

        int opc = scan.nextInt();
        long[] chavePrivada = null;
        while (chavePrivada==null){
            switch (opc){
                
                case 0 -> {
                    scan.nextLine();
                    System.out.println("Digite a chave privada (256 bits): ");
                    String chaveStr = scan.nextLine();
                    chavePrivada = CriptoUtils.gerarChaveDaString(chaveStr);
                    System.out.print("Chave privada: ");
                    CriptoUtils.printArrayLong(chavePrivada);
                }

                case 1 -> {
                    chavePrivada = CriptoUtils.gerarChavePrivada();
                    System.out.print("Chave privada gerada: ");
                    CriptoUtils.printArrayLong(chavePrivada);
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

        long[] chavePrivada = null;
        byte[] msgClara = null;
        byte[] msgCifrada = null;
        
        int opc = -1;

        while (opc!=4){
            printMenu();
            opc = scan.nextInt();
            switch (opc){
                case 0 -> chavePrivada=escolherChavePrivada(scan);
                case 1 -> msgClara=escolherMsgClara(scan);
                case 2 -> msgCifrada=cifrarTexto(cifraCubo, chavePrivada, msgClara);
                case 3 -> decifraTexto(cifraCubo, chavePrivada, msgCifrada);
                case 4 -> {}
                default -> System.out.println("Opçaõ inválida");
            }
        }

        scan.close();
    }
}