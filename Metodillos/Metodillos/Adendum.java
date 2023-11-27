package Metodillos;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class Adendum {
    // Método para obtener el adendum de una clave secreta
    public static byte obtenerAdendumClave(String claveSecreta) {
        char[] caracteres = claveSecreta.toCharArray();
        byte ultimoXor = 0;

        // Calcula el XOR entre caracteres consecutivos de la clave secreta
        for (int i = 0; i < caracteres.length - 1; i++) {
            byte xorActual;
            if (i == 0)
                xorActual = (byte) ((int) caracteres[i] ^ (int) caracteres[i + 1]);
            else
                xorActual = (byte) (ultimoXor ^ (int) caracteres[i + 1]);

            ultimoXor = xorActual;
        }

        // Convierte el resultado XOR a una representación binaria
        String representacionBinaria = Integer.toBinaryString(ultimoXor);

        // Imprime información sobre el adendum
        System.out.format("El adendum para la cadena -> \"%s\" es %08d = %d", claveSecreta,
                Integer.parseInt(representacionBinaria), ultimoXor);

        return ultimoXor;
    }

    // Método para cifrar el adendum usando RSA
    public static String cifrarAdendum(byte adendumCifrar, Key llaveCifrado) throws Exception {
        // Convierte el adendum a una cadena
        String adendum_clave_secreta = String.valueOf(adendumCifrar);
        byte[] bytesClaveSecreta = adendum_clave_secreta.getBytes(StandardCharsets.UTF_8);

        // Encripta los bytes del adendum usando RSA
        byte[] bytes_encriptadosAdendum = new EncriptadorB("RSA")
                .encriptarBytes(bytesClaveSecreta, llaveCifrado);

        // Convierte los bytes encriptados a una cadena
        String adendum_cifrado = new String(bytes_encriptadosAdendum, StandardCharsets.UTF_8);

        return adendum_cifrado;
    }

    // Método para descifrar el adendum usando RSA
    public static byte descifrarAdendum(String adendumCifrado, Key llaveDescifrado) throws Exception {
        // Descifra los bytes del adendum usando RSA
        byte[] bytes_desencriptados = new DesencriptadoB("RSA")
                .descencriptarBytes(Comunicacion.decodeString(adendumCifrado), llaveDescifrado);

        // Convierte los bytes desencriptados a una cadena y luego a un byte
        String adendum_descifrado = new String(bytes_desencriptados, StandardCharsets.UTF_8);
        byte adendumDescifrado = Byte.valueOf(adendum_descifrado);

        return adendumDescifrado;
    }
}
