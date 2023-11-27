import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.Key;

import static Metodillos.Comunicacion.*;

public class solClave {
    // Método para solicitar una clave a la Autoridad Certificadora (AC)
    public static Key solicitar_clave(Socket socket_hacia_AC, String tipoClave, String ipAsociadaClave)
            throws Exception {

        // Obtiene los flujos de entrada y salida del socket hacia la AC
        InputStream inputStream = socket_hacia_AC.getInputStream();
        OutputStream outputStream = socket_hacia_AC.getOutputStream();

        // Envia el tipo de clave y la IP asociada a la clave a la AC
        enviarMensaje(tipoClave, outputStream);
        enviarMensaje(ipAsociadaClave, outputStream);

        System.out.println("Esperando respuesta Autoridad Certificadora");

        // Recibe la respuesta de la AC
        String respuestaPeticion = recibirMensaje(inputStream);

        System.out.println("Solicitando " + tipoClave + " de la maquina " + ipAsociadaClave);
        System.out.println("Respuesta recibida: " + respuestaPeticion);

        // Recibe la clave desde la AC
        Key llaveRecibida = (Key) recibirObjeto(inputStream);
        System.out.println("Llave recibida: \n" + llaveRecibida);

        // Cierra la conexión con la AC
        socket_hacia_AC.close();

        // Retorna la clave recibida
        return llaveRecibida;
    }
}
