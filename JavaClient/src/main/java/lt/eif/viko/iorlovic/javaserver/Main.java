package lt.eif.viko.iorlovic.javaserver;

import lt.eif.viko.iorlovic.javaserver.javaclient.JavaClient;
import lt.eif.viko.iorlovic.javaserver.transformer.TransformToPojo;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Main class to demonstrate the usage of JavaClient and XML transformation.
 * @author Ilja
 * @version 1.0
 */
public class Main {
    /**
     * Main method to connect to the server, receive XML and XSD files,
     * transform XML data to POJOs, and print the transformed data.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        JavaClient client = new JavaClient();
        try {
            client.startConnection("localhost", 7777);

            client.receiveFiles("received_file.xml", "received_file.xsd");

            byte[] xmlData = Files.readAllBytes(Paths.get("received_file.xml"));
            List<Map<String, String>> authors = TransformToPojo.transformToPojo(xmlData);

            for (Map<String, String> authorData : authors) {
                System.out.println("Author: ");
                System.out.println("\tFirst Name: " + authorData.get("firstName"));
                System.out.println("\tLast Name: " + authorData.get("lastName"));
                System.out.println("\tAccount:");
                System.out.println("\t\tUser Name: " + authorData.get("userName"));
                System.out.println("\t\tPassword: " + authorData.get("password"));
                System.out.println("\tBooks: ");
                System.out.println("\t\t" + authorData.get("books"));
                System.out.println("\tAwards: ");
                System.out.println("\t\t" + authorData.get("awards"));
            }

        } catch (IOException | ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            client.stopConnection();
        }
    }
}
