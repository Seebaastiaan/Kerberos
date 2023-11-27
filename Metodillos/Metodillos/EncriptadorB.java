package Metodillos;

import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;

public class EncriptadorB {
    Cipher cifrador;

    /**
     * Constructor que inicializa el cifrador con el algoritmo de cifrado
     * especificado.
     * 
     * @param algoritmo_de_cifrado Algoritmo de cifrado a utilizar.
     * @throws Exception Si no se puede crear el cifrador con el algoritmo
     *                   especificado.
     */
    public EncriptadorB(String algoritmo_de_cifrado) throws Exception {
        try {
            cifrador = Cipher.getInstance(algoritmo_de_cifrado);
        } catch (GeneralSecurityException e) {
            throw new Exception("No se pudo crear el cifrador con el algoritmo " + algoritmo_de_cifrado);
        }
    }

    /**
     * Encripta un arreglo de bytes utilizando la llave de cifrado proporcionada.
     * 
     * @param bytes_a_encriptar Arreglo de bytes que se desea encriptar.
     * @param llave_de_cifrado  Llave de cifrado a utilizar.
     * @return Arreglo de bytes encriptados.
     * @throws Exception Si hay algún problema durante la operación de encriptación.
     */
    public byte[] encriptarBytes(byte[] bytes_a_encriptar, Key llave_de_cifrado) throws Exception {
        byte[] bytes_encriptados = null;
        try {
            cifrador.init(Cipher.ENCRYPT_MODE, llave_de_cifrado); // Inicializa el cifrador en modo encriptar, y se le
                                                                  // pasa la llave de cifrado
            bytes_encriptados = cifrador.doFinal(bytes_a_encriptar); // Se le asigna a bytes_encriptados, los bytes
                                                                     // encriptados que regresa el cifrador
        } catch (GeneralSecurityException e) {
            throw new Exception("Error durante la encriptación: " + e.getMessage());
        }
        return bytes_encriptados;
    }
}
