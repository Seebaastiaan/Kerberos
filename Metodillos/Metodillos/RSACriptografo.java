package Metodillos;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class RSACriptografo {
    final String algoritmo;

    /**
     * Constructor que inicializa el objeto con el algoritmo de cifrado
     * especificado.
     * 
     * @param algoritmo Algoritmo de cifrado RSA a utilizar.
     */
    public RSACriptografo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    /**
     * Cifra una cadena de texto utilizando la llave de cifrado RSA proporcionada.
     * 
     * @param stringCifrar  Cadena de texto que se desea cifrar.
     * @param llave_cifrado Llave de cifrado RSA a utilizar.
     * @return Cadena de texto cifrada.
     * @throws Exception Si hay algún problema durante la operación de cifrado.
     */
    public String cifrarString(String stringCifrar, Key llave_cifrado) throws Exception {
        byte[] bytesClave = stringCifrar.getBytes(StandardCharsets.UTF_8);
        EncriptadorB encriptadorBytes = new EncriptadorB(algoritmo);

        // Se encriptan los bytes de la cadena utilizando el cifrador RSA
        byte[] bytesEncriptados = encriptadorBytes.encriptarBytes(bytesClave, llave_cifrado);

        // Se codifican los bytes encriptados como una cadena Base64
        String stringCifrado = Base64.getEncoder().encodeToString(bytesEncriptados);
        return stringCifrado;
    }

    /**
     * Descifra una cadena de texto cifrada utilizando la llave de cifrado RSA
     * proporcionada.
     * 
     * @param stringDescifrar Cadena de texto cifrada que se desea descifrar.
     * @param llave_cifrado   Llave de cifrado RSA a utilizar.
     * @return Cadena de texto descifrada.
     * @throws Exception Si hay algún problema durante la operación de descifrado.
     */
    public String descifrarString(String stringDescifrar, Key llave_cifrado) throws Exception {
        // Decodifica la cadena cifrada de Base64 a bytes
        byte[] bytesClave = Base64.getDecoder().decode(stringDescifrar.getBytes(StandardCharsets.UTF_8));
        DesencriptadoB desencriptadorBytes = new DesencriptadoB(algoritmo);

        // Se descifran los bytes utilizando el descifrador RSA
        byte[] bytesDesencriptados = desencriptadorBytes.descencriptarBytes(bytesClave, llave_cifrado);

        // Se convierten los bytes desencriptados a una cadena de texto
        String stringDescifrado = new String(bytesDesencriptados, StandardCharsets.UTF_8);
        return stringDescifrado;
    }
}
