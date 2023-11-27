import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

import Metodillos.Comunicacion;
import Metodillos.DesencriptadoB;
import Metodillos.EncriptadorB;

public class AEScripto {
    private static final String ALGORITMO = "AES/ECB/PKCS5Padding";

    private SecretKeySpec secretKeySpec;

    // Constructor que inicializa la clase con una llave secreta.
    public AEScripto(String llaveSecreta) throws Exception {
        this.setLlave(llaveSecreta);
    }

    // Método privado que convierte la cadena de la llave secreta en un objeto
    // SecretKeySpec.
    private void setLlave(final String myKey) {
        this.secretKeySpec = StringToSecretKey(myKey);
    }

    // Método que desencripta un mensaje dado.
    public String desencriptarMensaje(final String strToEncrypt) throws Exception {
        String mensajeDesencriptado = null;
        DesencriptadoB DesencriptadoB = new DesencriptadoB(ALGORITMO);
        try {
            // Decodifica la cadena del mensaje en bytes.
            byte[] bytesCifrado = Comunicacion.decodeString(strToEncrypt);
            // Desencripta los bytes y obtiene el mensaje desencriptado.
            byte[] bytesDescifrados = DesencriptadoB.descencriptarBytes(bytesCifrado, this.secretKeySpec);
            // Codifica los bytes desencriptados a una cadena.
            mensajeDesencriptado = Comunicacion.encodeBytes(bytesDescifrados);
        } catch (Exception e) {
            System.out.println("No desencripto" + e.toString());
            e.printStackTrace();
        }
        return mensajeDesencriptado;
    }

    // Método que encripta un mensaje dado.
    public String encriptarMensaje(final String cadenaEncriptar) throws Exception {
        String mensajeEncriptado = null;
        EncriptadorB EncriptadorB = new EncriptadorB(ALGORITMO);
        try {
            // Decodifica la cadena del mensaje en bytes.
            byte[] bytesMensaje = Comunicacion.decodeString(cadenaEncriptar);
            // Encripta los bytes y obtiene el mensaje encriptado.
            byte[] bytesCifrados = EncriptadorB.encriptarBytes(bytesMensaje, this.secretKeySpec);
            // Codifica los bytes encriptados a una cadena.
            mensajeEncriptado = Comunicacion.encodeBytes(bytesCifrados);
        } catch (Exception e) {
            System.out.println("Error al encriptar el mensaje : " + e.toString());
            e.printStackTrace();
        }
        return mensajeEncriptado;
    }

    // Método que convierte una cadena en una clave secreta (objeto SecretKeySpec).
    public SecretKeySpec StringToSecretKey(String stringSecKey) {
        SecretKeySpec secretKey = null;
        try {
            // Obtiene los bytes de la clave secreta.
            byte[] byteSecKey = stringSecKey.getBytes(StandardCharsets.UTF_8);
            // Convierte los bytes de la clave secreta a un arreglo de 16 bytes o 128 bits.
            byteSecKey = Arrays.copyOf(byteSecKey, 16);
            // Asigna valor al objeto SecretKeySpec.
            secretKey = new SecretKeySpec(byteSecKey, ALGORITMO.split("/")[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey;
    }
}
