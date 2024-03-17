package lt.eif.viko.iorlovic.javaserver.javaclient;

import java.io.*;
import java.net.Socket;

/**
 * JavaClient class provides functionality to connect to a server and receive files over a network.
 * @author Ilja
 * @version 1.0
 */
public class JavaClient {
    private Socket clientSocket;

    /**
     * Establishes a connection to the server.
     *
     * @param ip   The IP address of the server.
     * @param port The port number of the server.
     * @throws IOException If an I/O error occurs when creating the socket.
     */
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
    }

    /**
     * Receives files from the server and saves them locally.
     *
     * @param xmlFileName The name of the XML file to be received.
     * @param xsdFileName The name of the XSD file to be received.
     * @throws IOException If an I/O error occurs when receiving files.
     */
    public void receiveFiles(String xmlFileName, String xsdFileName) throws IOException {
        try (InputStream is = clientSocket.getInputStream();
             FileOutputStream xmlFos = new FileOutputStream(xmlFileName);
             FileOutputStream xsdFos = new FileOutputStream(xsdFileName)) {

            DataInputStream dis = new DataInputStream(is);

            receiveFile(xmlFos, dis);

            receiveFile(xsdFos, dis);

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Files received");

            System.out.println("Files received successfully.");
        }
    }

    private void receiveFile(FileOutputStream fos, DataInputStream dis) throws IOException {
        int fileSize = dis.readInt();
        byte[] buffer = new byte[1024];
        int bytesRead;
        int totalBytesRead = 0;

        while ((bytesRead = dis.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
            if (totalBytesRead >= fileSize) {
                break;
            }
        }
    }

    /**
     * Closes the connection to the server.
     */
    public void stopConnection() {
        try {
            clientSocket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Main method to demonstrate the usage of JavaClient.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        JavaClient client = new JavaClient();
        try {
            client.startConnection("localhost", 7777);
            client.receiveFiles("received_file.xml", "received_file.xsd");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.stopConnection();
        }
    }
}
