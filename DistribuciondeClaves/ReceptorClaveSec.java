import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.Scanner;

import Metodillos.Adendum;
import Metodillos.Comunicacion;
import Metodillos.Conexion;
import Metodillos.RSACriptografo;

public class ReceptorClaveSec extends solClave {

    private int puertoEscuchaClave = 6000;
    private ServerSocket socketDistribucionClave = new ServerSocket(puertoEscuchaClave);

    public ReceptorClaveSec() throws IOException {
    }

    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);
        int puerto_AC = 5001;

        System.out.println("Ingresa la ip de la Autoridad Autentificadora");
        String ip_AC = scan.nextLine();
        System.out.println(ip_AC);

        System.out.println("Conectando con AC");

        Socket conexion_con_AC1 = Conexion.obtenerConexion(puerto_AC, ip_AC);

        System.out.println("Ingresa la ip del emisor clave");
        String ip_emisor = scan.nextLine();
        System.out.println(ip_emisor);

        Key llave_publica_emisor = solicitar_clave(conexion_con_AC1, "clave-publica", ip_emisor);

        System.out.println("\nIngresa la ip del receptor clave");
        String ip_receptor = scan.nextLine();
        System.out.println(ip_receptor);

        Socket conexion_con_AC2 = Conexion.obtenerConexion(puerto_AC, ip_AC);
        Key llave_privada_receptor = solicitar_clave(conexion_con_AC2, "clave-privada", ip_receptor);

        System.out.println("conectando con emisor clave");

        Socket conexionEmisorClave = Conexion.obtenerConexion(EmisorClaveSec.puertoEntrada, ip_emisor);

        InputStream inputStream = conexionEmisorClave.getInputStream();
        String claveSecretaCifrada = (String) Comunicacion.recibirObjeto(inputStream);
        System.out.println("clave secreta Cifrada" + claveSecretaCifrada);

        RSACriptografo RSACriptografo = new RSACriptografo("RSA");
        String claveSecreta = RSACriptografo.descifrarString(claveSecretaCifrada, llave_publica_emisor);
        System.out.println("clave secreta" + claveSecreta);

        byte adendum_clave_secreta = Adendum.obtenerAdendumClave(claveSecreta);
        System.out.println("Adendum clave recibida " + adendum_clave_secreta);

        String adendumCifrado = (String) Comunicacion.recibirObjeto(inputStream);
        byte adendumDescifrado = Adendum.descifrarAdendum(adendumCifrado, llave_privada_receptor);

        System.out.println("Adendum descifrado " + adendumDescifrado);

        ServerSocket serverSocketChat = new ServerSocket(6000);

        Socket socketChat = Conexion.aceptarConexionEntrante(6000, serverSocketChat);
        IniChat chat = new IniChat(socketChat, "queOndaMau");
        chat.iniciarChat();
    }
}
