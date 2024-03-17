package lt.eif.viko.iorlovic.javaserver.transformer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to transform XML data to Plain Old Java Objects (POJOs).
 * @author Ilja
 * @version 1.0
 */
public class TransformToPojo {

    /**
     * Transforms XML data into a list of maps representing authors' information.
     *
     * @param xmlData The XML data to be transformed.
     * @return A list of maps, each containing data for an author.
     * @throws ParserConfigurationException If a DocumentBuilder cannot be created.
     * @throws IOException                  If an I/O error occurs.
     */
    public static List<Map<String, String>> transformToPojo(byte[] xmlData) throws ParserConfigurationException, IOException {
        List<Map<String, String>> authors = new ArrayList<>();

        try (InputStream is = new ByteArrayInputStream(xmlData)) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(is));

            NodeList authorNodes = doc.getElementsByTagName("author");
            for (int i = 0; i < authorNodes.getLength(); i++) {
                Node authorNode = authorNodes.item(i);
                if (authorNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element authorElement = (Element) authorNode;

                    Map<String, String> authorMap = new HashMap<>();
                    authorMap.put("firstName", authorElement.getElementsByTagName("firstName").item(0).getTextContent());
                    authorMap.put("lastName", authorElement.getElementsByTagName("lastName").item(0).getTextContent());

                    Element accountElement = (Element) authorElement.getElementsByTagName("account").item(0);
                    authorMap.put("userName", accountElement.getElementsByTagName("userName").item(0).getTextContent());
                    authorMap.put("password", accountElement.getElementsByTagName("password").item(0).getTextContent());

                    NodeList bookNodes = authorElement.getElementsByTagName("books");
                    List<String> books = new ArrayList<>();
                    for (int j = 0; j < bookNodes.getLength(); j++) {
                        Element bookElement = (Element) bookNodes.item(j);
                        String bookInfo = String.format("Name: %s, Year: %s",
                                bookElement.getElementsByTagName("name").item(0).getTextContent(),
                                bookElement.getElementsByTagName("years").item(0).getTextContent());
                        books.add(bookInfo);
                    }
                    authorMap.put("books", String.join("; ", books));

                    NodeList awardNodes = authorElement.getElementsByTagName("awards");
                    List<String> awards = new ArrayList<>();
                    for (int j = 0; j < awardNodes.getLength(); j++) {
                        Element awardElement = (Element) awardNodes.item(j);
                        String awardInfo = String.format("Title: %s, Year: %s",
                                awardElement.getElementsByTagName("title").item(0).getTextContent(),
                                awardElement.getElementsByTagName("years").item(0).getTextContent());
                        awards.add(awardInfo);
                    }
                    authorMap.put("awards", String.join("; ", awards));

                    authors.add(authorMap);
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return authors;
    }
}
