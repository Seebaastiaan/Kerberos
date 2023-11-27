package Metodillos;

import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;

public class DesencriptadoB {

    private final Cipher descifrador;

    /**
     * Constructor que inicializa el descifrador con el algoritmo de cifrado
     * especificado.
     * 
     * @param algoritmo_de_cifrado Algoritmo de cifrado a utilizar.
     * @throws Exception Si no se puede crear el descifrador con el algoritmo
     *                   especificado.
     */
    public DesencriptadoB(String algoritmo_de_cifrado) throws Exception {
        try {
            descifrador = Cipher.getInstance(algoritmo_de_cifrado);
        } catch (GeneralSecurityException e) {
            throw new Exception("No se pudo crear el descifrador con el algoritmo " + algoritmo_de_cifrado);
        }
    }

    /**
     * Desencripta un arreglo de bytes utilizando la llave de cifrado proporcionada.
     * 
     * @param bytes_encriptados Arreglo de bytes que se desea desencriptar.
     * @param llave_de_cifrado  Llave de cifrado a utilizar.
     * @return Arreglo de bytes desencriptados.
     * @throws Exception Si hay algún problema durante la operación de
     *                   desencriptación.
     */
    public byte[] descencriptarBytes(byte[] bytes_encriptados, Key llave_de_cifrado) throws Exception {
        byte[] bytes_desencriptados = null;
        try {
            descifrador.init(Cipher.DECRYPT_MODE, llave_de_cifrado); // Inicializa el descifrador en modo desencriptar,
                                                                     // y se le pasa la llave para desencriptar
            bytes_desencriptados = descifrador.doFinal(bytes_encriptados); // Se le asigna a bytes_desencriptados, los
                                                                           // bytes desencriptados que regresa el
                                                                           // descifrador
        } catch (GeneralSecurityException e) {
            throw new Exception("Error durante la desencriptación: " + e.getMessage());
        }
        return bytes_desencriptados;
    }
}
