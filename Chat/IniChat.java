import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import Metodillos.Comunicacion;

public class IniChat {
    Socket conexionEntrada;
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    private AEScripto AEScripto;
    String claveSecreta;

    // Constructor que inicializa la clase con una conexión de entrada y una clave
    // secreta.
    public IniChat(Socket conexionEntrada, String claveSecreta) throws Exception {
        this.claveSecreta = claveSecreta;
        this.conexionEntrada = conexionEntrada;
        this.printWriter = new PrintWriter(conexionEntrada.getOutputStream());
        this.bufferedReader = new BufferedReader(new InputStreamReader(conexionEntrada.getInputStream()));
        this.AEScripto = new AEScripto(claveSecreta);
    }

    // Método que inicia el chat.
    public void iniciarChat() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Clave Secreta: " + claveSecreta);

        while (true) {
            // Lee el mensaje desde la consola.
            String mensaje = scanner.nextLine();
            // Encripta el mensaje.
            String mensajeCifrado = this.AEScripto.encriptarMensaje(mensaje);
            // Envía el mensaje cifrado a través de la conexión.
            Comunicacion.enviarMensaje(mensajeCifrado, this.printWriter);

            // Recibe un mensaje cifrado.
            String mensajeCifradoRecibido = Comunicacion.recibirMensaje(this.bufferedReader);
            // Desencripta el mensaje recibido.
            String mensajeDescifrado = this.AEScripto.desencriptarMensaje(mensajeCifradoRecibido);
            // Imprime el mensaje descifrado.
            System.out.println(mensajeDescifrado);
        }
    }

    // Método que envía un saludo a través de la conexión.
    public void saludar() {
        Comunicacion.enviarMensaje("Hola desde la otra maquina", this.printWriter);
    }
}
