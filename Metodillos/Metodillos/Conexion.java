package Metodillos;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexion {

    /**
     * Establece una conexión a un destino específico.
     * 
     * @param puertoDestino Puerto al que se desea conectar.
     * @param ipDestino     Dirección IP del destino al que se desea conectar.
     * @return Socket que representa la conexión establecida.
     * @throws IOException
     */
    public static Socket obtenerConexion(int puertoDestino, String ipDestino) throws IOException {
        // Obtiene la dirección IP del destino
        InetAddress direccionDestino = InetAddress.getByName(ipDestino);
        // Crea un socket y se conecta al destino en el puerto especificado
        Socket socketHaciaDestino = new Socket(direccionDestino, puertoDestino);

        // Muestra información sobre la conexión realizada
        System.out.println("Conexion realizada con la IP: " + direccionDestino + " en puerto: " + puertoDestino);
        return socketHaciaDestino;
    }

    /**
     * Acepta una conexión entrante en el puerto especificado.
     * 
     * @param puertoEscucha Puerto en el que se espera la conexión entrante.
     * @param serverSocket  Socket del servidor que está esperando conexiones.
     * @return Socket que representa la conexión entrante aceptada.
     * @throws IOException
     */
    public static Socket aceptarConexionEntrante(int puertoEscucha, ServerSocket serverSocket) throws IOException {
        // Acepta la conexión entrante en el servidor
        Socket socketConexion = serverSocket.accept();

        // Muestra información sobre la conexión recibida
        System.out.println(
                "Conexion recibida con la IP: " + socketConexion.getInetAddress() + " en puerto: " + puertoEscucha);

        return socketConexion;
    }
}
