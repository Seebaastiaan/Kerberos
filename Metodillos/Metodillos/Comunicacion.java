package Metodillos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Base64;

public class Comunicacion {

    /**
     * Envia un mensaje utilizando el flujo de salida dado
     * 
     * @param mensaje_enviar
     * @param outputStream
     */
    public static void enviarMensaje(String mensaje_enviar, OutputStream outputStream) {
        // Crea el PrintWriter
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
        // Escribe una línea en el buffer
        printWriter.println(mensaje_enviar);
        // Vacía o envía el buffer
        printWriter.flush();
        return;
    }

    /**
     * Envia mensaje utilizando un PrintWriter dado
     * 
     * @param mensaje_enviar
     * @param printWriter
     */
    public static void enviarMensaje(String mensaje_enviar, PrintWriter printWriter) {
        printWriter.println(mensaje_enviar);
        printWriter.flush();
        return;
    }

    /**
     * Espera un mensaje entrante en el InputStream dado
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String recibirMensaje(InputStream inputStream) throws IOException {
        // Crea el BufferedReader para el flujo de entrada
        BufferedReader socketInputStreamReader = new BufferedReader(new InputStreamReader(inputStream));
        // Lee el mensaje entrante
        String message = socketInputStreamReader.readLine();
        // Retorna el mensaje
        return message;
    }

    /**
     * Espera un mensaje entrante utilizando un BufferedReader
     * 
     * @param socketInputStreamReader
     * @return
     * @throws IOException
     */
    public static String recibirMensaje(BufferedReader socketInputStreamReader) throws IOException {
        // Lee el mensaje y lo retorna
        String message = socketInputStreamReader.readLine();
        return message;
    }

    /**
     * Recibe un mensaje en el flujo de entrada
     * 
     * @param inputStream
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object recibirObjeto(InputStream inputStream) throws IOException, ClassNotFoundException {
        // Crea un flujo de entrada de objetos
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        // Recibe el objeto entrante
        Object objetoRecibido = objectInputStream.readObject();
        // Retorna el objeto
        return objetoRecibido;
    }

    /**
     * Recibe un objeto en el flujo dado
     * 
     * @param outputStream
     * @param objetoEnviar
     * @throws IOException
     */
    public static void enviarObjeto(OutputStream outputStream, Object objetoEnviar) throws IOException {
        // Crea flujo de entrada de objetos
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        // Escribe el objeto dado en el flujo
        objectOutputStream.writeObject(objetoEnviar);
        // Envía el objeto
        objectOutputStream.flush();
    }

    /**
     * Convierte o codifica un arreglo de bytes en su representación en String
     * 
     * @param bytesToEncode
     * @return
     */
    public static String encodeBytes(byte[] bytesToEncode) {
        return Base64.getEncoder().encodeToString(bytesToEncode);
    }

    /**
     * Convierte o decodifica un String en un arreglo de bytes
     * 
     * @param stringToDecode
     * @return
     */
    public static byte[] decodeString(String stringToDecode) {
        return Base64.getDecoder().decode(stringToDecode);
    }
}
