import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.Scanner;

import Metodillos.Comunicacion;
import Metodillos.Conexion;
import Metodillos.RSACriptografo;

import static Metodillos.Adendum.cifrarAdendum;
import static Metodillos.Adendum.obtenerAdendumClave;

public class EmisorClaveSec extends solClave {
    static int puertoEntrada = 4000;

    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);

        // Crear una instancia de EmisorClaveSecreta
        EmisorClaveSec emisorClave = new EmisorClaveSec();
        int puerto_AC = 5001;

        // Solicitar la dirección IP de la Autoridad Certificadora (AC)
        System.out.println("Ingresa la ip de la Autoridad Certificadora");
        String ip_AC = scan.nextLine();
        System.out.println(ip_AC);

        // Conectar con la Autoridad Certificadora (AC)
        Socket conexion_con_AC1 = Conexion.obtenerConexion(puerto_AC, ip_AC);

        // Solicitar la dirección IP del emisor de la clave
        System.out.println("Ingresa la ip del emisor clave");
        String ip_emisor = scan.nextLine();
        System.out.println(ip_emisor);

        // Solicitar la clave privada del emisor al AC
        Key llave_privada_emisor = solicitar_clave(conexion_con_AC1, "clave-privada", ip_emisor);

        // Solicitar la dirección IP del receptor de la clave
        System.out.println("\nIngresa la ip del receptor clave");
        String ip_receptor = scan.nextLine();
        System.out.println(ip_receptor);

        // Conectar nuevamente con la Autoridad Certificadora (AC)
        Socket conexion_con_AC2 = Conexion.obtenerConexion(puerto_AC, ip_AC);

        // Solicitar la clave pública del receptor al AC
        Key llave_publica_receptor = solicitar_clave(conexion_con_AC2, "clave-publica", ip_receptor);

        // Solicitar la clave secreta al usuario
        System.out.println("Ingresa la clave Secreta");
        String claveSecreta = scan.nextLine();
        System.out.println(claveSecreta);

        // Cifrar la clave secreta usando RSA con la clave privada del emisor
        RSACriptografo RSACriptografo = new RSACriptografo("RSA");
        String claveSecretaCifrada = RSACriptografo.cifrarString(claveSecreta, llave_privada_emisor);
        System.out.println("Clave Secreta Cifrada " + claveSecretaCifrada);

        // Obtener un adendum (byte) de la clave secreta y cifrarlo con la clave pública
        // del receptor
        byte adendum_clave_secreta = (obtenerAdendumClave(claveSecreta));
        System.out.println("\nadendum clave " + adendum_clave_secreta);
        String adendum_cifrado = cifrarAdendum(adendum_clave_secreta, llave_publica_receptor);
        System.out.println("adendum clave cifrado " + adendum_cifrado);

        // Esperar conexión con el receptor en un puerto específico
        System.out.println("Esperando conexion con receptor");
        ServerSocket socketDistribucionClaves = new ServerSocket(puertoEntrada);
        Socket conexion_receptor = Conexion.aceptarConexionEntrante(puertoEntrada, socketDistribucionClaves);

        // Enviar la clave secreta cifrada al receptor
        OutputStream outputStreamReceptor = conexion_receptor.getOutputStream();
        InputStream inputStreamReceptor = conexion_receptor.getInputStream();
        Comunicacion.enviarObjeto(outputStreamReceptor, claveSecretaCifrada);
        System.out.println("Clave Secreta Enviada");

        // Enviar el adendum cifrado al receptor
        Comunicacion.enviarObjeto(outputStreamReceptor, adendum_cifrado);
        System.out.println("Adendum Secreto Enviado");

        // Conectar con el chat a través de otro puerto y comenzar el chat
        Socket conexionChat = Conexion.obtenerConexion(6000, "127.0.0.1");
        IniChat chat = new IniChat(conexionChat, "mordecai");
        chat.iniciarChat();
    }
}
