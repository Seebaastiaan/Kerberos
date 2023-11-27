import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.HashMap;
import java.util.Map;

import Metodillos.Comunicacion;
import Metodillos.Conexion;

public class AutoridadCertificadora {
    private final int puertoEscuchaDistribucionClave = 5001;
    // Servidor de distribución de claves
    private ServerSocket socketDistribucionClaves;
    // Generador de pares de claves pública y privada (RSA)
    private KeyPairGenerator generadorclavesRSA;
    // Lista que relaciona direcciones IP con pares de claves
    private Map<InetAddress, KeyPair> listaipclave = new HashMap<>();

    // Constructor que inicializa el generador de pares de claves RSA
    public AutoridadCertificadora() throws Exception {
        generadorclavesRSA = KeyPairGenerator.getInstance("RSA");
    }

    // Método principal que crea un objeto AutoridadCertificadora y atiende
    // peticiones indefinidamente
    public static void main(String[] args) throws Exception {
        AutoridadCertificadora AC = new AutoridadCertificadora();
        while (true) {
            AC.atenderPeticion();
        }
    }

    // Obtiene una clave (pública o privada) asociada a la dirección IP
    private Key obtenerClave(InetAddress ip_host, String tipo_clave) {
        KeyPair parClavesHost = obtenerParClaves(ip_host);
        Key claveSolicitada;

        switch (tipo_clave) {
            case "publica":
                claveSolicitada = parClavesHost.getPublic();
                break;
            case "privada":
                claveSolicitada = parClavesHost.getPrivate();
                break;
            default:
                claveSolicitada = null;
        }

        return claveSolicitada;
    }

    // Genera un nuevo par de claves (pública y privada) para la dirección IP
    private KeyPair generarNuevoParDeClaves(InetAddress ip_host) {
        KeyPair nuevoParClaves = this.generadorclavesRSA.generateKeyPair();
        listaipclave.put(ip_host, nuevoParClaves);
        return nuevoParClaves;
    }

    // Obtiene un par de claves para la dirección IP
    private KeyPair obtenerParClaves(InetAddress ip_host) {
        KeyPair parClaves = listaipclave.containsKey(ip_host) ? listaipclave.get(ip_host)
                : generarNuevoParDeClaves(ip_host);
        return parClaves;
    }

    // Método que atiende la petición del solicitante
    public void atenderPeticion() throws IOException {
        System.out.println("\n--------------------------------------------------------------------------------\n"
                + "Esperando nueva conexión\n");

        // Asigna un nuevo socket
        socketDistribucionClaves = new ServerSocket(puertoEscuchaDistribucionClave);
        Socket socketConexion = Conexion.aceptarConexionEntrante(puertoEscuchaDistribucionClave,
                this.socketDistribucionClaves);

        OutputStream outputStream = socketConexion.getOutputStream();
        InputStream inputStream = socketConexion.getInputStream();

        System.out.println("Conexión realizada con la IP: \t " + socketConexion.getInetAddress());
        // Obtiene la clave solicitada por el solicitante
        Key clave_solicitada = procesar_solicitud_de_clave(outputStream, inputStream, socketConexion.getInetAddress());

        // Envía la clave al solicitante
        Comunicacion.enviarObjeto(outputStream, clave_solicitada);
        System.out.println("CLAVE ENVIADA: \n" + clave_solicitada);

        // Cierra la conexión
        socketDistribucionClaves.close();
    }

    // Método que procesa la solicitud de clave
    private Key procesar_solicitud_de_clave(OutputStream outputStream, InputStream inputStream,
            InetAddress inetAddress_solicitante) throws IOException {
        BufferedReader socketInputStreamReader = new BufferedReader(new InputStreamReader(inputStream));

        // Recibe el tipo de clave solicitada (clave-publica o clave-privada)
        String tipo_de_peticion_clave = socketInputStreamReader.readLine();
        System.out.println("Tipo Peticion " + tipo_de_peticion_clave);

        // Recibe la IP solicitada
        String ip_vinculada_a_clave_solicitada = socketInputStreamReader.readLine();
        System.out.println("IP solicitada " + ip_vinculada_a_clave_solicitada);

        // Obtiene la IP del solicitante
        String ip_solicitante = inetAddress_solicitante.getHostAddress();
        InetAddress ipSolicitada = InetAddress.getByName(ip_vinculada_a_clave_solicitada);

        String respuesta_peticion;
        Key clave_solicitada;

        switch (tipo_de_peticion_clave) {
            case "clave-privada":
                boolean puedeRecibirPrivada = ip_solicitante.equals(ip_vinculada_a_clave_solicitada);
                puedeRecibirPrivada = true;

                if (puedeRecibirPrivada) {
                    clave_solicitada = obtenerClave(ipSolicitada, "privada");
                    respuesta_peticion = "La clave privada fue encontrada y enviada";
                    System.out.format("La clave privada fue enviada \t IP-SOLICITANTE: %s | IP-SOLICITADA: %s \n",
                            ip_solicitante, ipSolicitada);
                } else {
                    clave_solicitada = null;
                    respuesta_peticion = "Solo el dueño de la claves puede recibir la clave privada";
                    System.out.format(
                            "El solicitante no puede recibir la clave privada\t IP-SOLICITANTE: %s | IP-SOLICITADA: %s \n",
                            ip_solicitante, ipSolicitada);
                }
                break;
            case "clave-publica":
                clave_solicitada = obtenerClave(ipSolicitada, "publica");
                respuesta_peticion = "La clave publica fue encontrada y enviada";
                System.out.format("La clave publica fue enviada \t IP-SOLICITANTE: %s | IP-SOLICITADA: %s \n",
                        ip_solicitante, ipSolicitada);
                break;
            default:
                clave_solicitada = null;
                respuesta_peticion = "Tipo peticion no válida";
                System.out.format(
                        "El solicitante no realizo una petición invalida\t IP-SOLICITANTE: %s | IP-SOLICITADA: %s \n",
                        ip_solicitante, ipSolicitada);
        }

        Comunicacion.enviarMensaje(respuesta_peticion, outputStream);
        return clave_solicitada;
    }
}
