package de.uni.tuebingen.sfs.java2;

import lombok.Getter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * XMLWriter
 *
 * Version 1.2
 *
 * This class is responsible for writing session data to an XML file.
 * The session data includes uploaded text files, linguistic processing results (tokens, lemmas, POS tags),
 * and web scraping results from Wikipedia articles.
 *
 * Methods:
 * - writeXML(String fileName, List<UploadedFile> uploadedFiles, List<ProcessedFile> processedFiles, List<WikipediaArticle> articles)
 *   Writes the provided data to an XML file specified by the fileName.
 *
 * Usage:
 * XMLWriter.writeXML("output.xml", uploadedFiles, processedFiles, articles);
 *
 * Each file, linguistic processing result, and Wikipedia article is represented by respective classes:
 * - UploadedFile
 * - ProcessedFile
 * - WikipediaArticle
 *
 * Integration with Swing:
 * To integrate this class with a Swing application, follow these steps:
 * 1. Create GUI components such as buttons and text fields.
 * 2. Add action listeners to the buttons to trigger the XML writing process.
 * 3. Collect the data from the text fields and other input components.
 * 4. Call XMLWriter.writeXML() with the collected data when the button is clicked.
 *
 * Example:
 * JButton saveButton = new JButton("Save to XML");
 * saveButton.addActionListener(e -> {
 *     List<UploadedFile> uploadedFiles = ...; // Collect data from GUI
 *     List<ProcessedFile> processedFiles = ...; // Collect data from GUI
 *     List<WikipediaArticle> articles = ...; // Collect data from GUI
 *     XMLWriter.writeXML("output.xml", uploadedFiles, processedFiles, articles);
 * });
 *
 * Detailed Integration Steps:
 * 1. **Button Creation**:
 *    Create a JButton in your Swing GUI for saving XML.
 *
 * 2. **Add Action Listener**:
 *    Attach an action listener to the button that gathers the necessary data from your GUI components.
 *
 * 3. **Collect Data**:
 *    Ensure you have methods to collect data from your text fields, tables, or other components that hold the session data.
 *
 * 4. **Call writeXML**:
 *    Within the action listener, call the `writeXML` method of `XMLWriter`, passing the collected data.
 *
 * 5. **File Dialog**:
 *    Optionally, use a `JFileChooser` to let the user specify the location and name of the XML file to save.
 *
 * Example Integration:
 * ```java
 * saveButton.addActionListener(e -> {
 *     // Assuming methods getUploadedFiles(), getProcessedFiles(), getWikipediaArticles() are defined to collect data
 *     List<UploadedFile> uploadedFiles = getUploadedFiles();
 *     List<ProcessedFile> processedFiles = getProcessedFiles();
 *     List<WikipediaArticle> articles = getWikipediaArticles();
 *
 *     // Use JFileChooser to select file save location
 *     JFileChooser fileChooser = new JFileChooser();
 *     fileChooser.setDialogTitle("Save XML");
 *     int userSelection = fileChooser.showSaveDialog(null);
 *     if (userSelection == JFileChooser.APPROVE_OPTION) {
 *         File fileToSave = fileChooser.getSelectedFile();
 *         XMLWriter.writeXML(fileToSave.getAbsolutePath(), uploadedFiles, processedFiles, articles);
 *         JOptionPane.showMessageDialog(null, "File saved successfully!");
 *     }
 * });
 * ```
 *
 * This makes the XMLWriter class straightforward to integrate into any Swing-based application, providing a seamless way to save session data to an XML file.
 */

public class XMLWriter {

    public static void writeXML(String fileName, List<UploadedFile> uploadedFiles, List<ProcessedFile> processedFiles, List<WikipediaArticle> articles) {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            XMLEventWriter eventWriter = xmlOutputFactory.createXMLEventWriter(outputStream, "UTF-8");
            XMLEvent newLine = eventFactory.createCharacters("\n");
            XMLEvent indent = eventFactory.createCharacters("    ");

            eventWriter.add(eventFactory.createStartDocument("UTF-8", "1.0"));
            eventWriter.add(newLine);

            // Start root element
            eventWriter.add(eventFactory.createStartElement("", "", "session"));
            eventWriter.add(newLine);

            // Metadata
            eventWriter.add(indent);
            eventWriter.add(eventFactory.createStartElement("", "", "metadata"));
            eventWriter.add(newLine);

            eventWriter.add(indent);
            eventWriter.add(indent);
            createElement(eventWriter, "date", "2024-06-20");
            createElement(eventWriter, "time", "15:00");

            eventWriter.add(indent);
            eventWriter.add(eventFactory.createEndElement("", "", "metadata"));
            eventWriter.add(newLine);

            // Uploaded Files
            eventWriter.add(indent);
            eventWriter.add(eventFactory.createStartElement("", "", "uploadedFiles"));
            eventWriter.add(newLine);

            for (UploadedFile file : uploadedFiles) {
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createStartElement("", "", "file"));
                eventWriter.add(newLine);

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                createElement(eventWriter, "fileName", file.getFileName());
                createElement(eventWriter, "content", file.getContent());

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createEndElement("", "", "file"));
                eventWriter.add(newLine);
            }

            eventWriter.add(indent);
            eventWriter.add(eventFactory.createEndElement("", "", "uploadedFiles"));
            eventWriter.add(newLine);

            // Linguistic Processing
            eventWriter.add(indent);
            eventWriter.add(eventFactory.createStartElement("", "", "linguisticProcessing"));
            eventWriter.add(newLine);

            for (ProcessedFile processedFile : processedFiles) {
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createStartElement("", "", "file"));
                eventWriter.add(newLine);

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                createElement(eventWriter, "fileName", processedFile.getFileName());

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createStartElement("", "", "tokens"));
                eventWriter.add(newLine);

                for (String token : processedFile.getTokens()) {
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    createElement(eventWriter, "token", token);
                }

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createEndElement("", "", "tokens"));
                eventWriter.add(newLine);

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createStartElement("", "", "lemmas"));
                eventWriter.add(newLine);

                for (String lemma : processedFile.getLemmas()) {
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    createElement(eventWriter, "lemma", lemma);
                }

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createEndElement("", "", "lemmas"));
                eventWriter.add(newLine);

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createStartElement("", "", "posTags"));
                eventWriter.add(newLine);

                for (String pos : processedFile.getPosTags()) {
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    eventWriter.add(indent);
                    createElement(eventWriter, "pos", pos);
                }

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createEndElement("", "", "posTags"));
                eventWriter.add(newLine);

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createEndElement("", "", "file"));
                eventWriter.add(newLine);
            }

            eventWriter.add(indent);
            eventWriter.add(eventFactory.createEndElement("", "", "linguisticProcessing"));
            eventWriter.add(newLine);

            // Web Scraping
            eventWriter.add(indent);
            eventWriter.add(eventFactory.createStartElement("", "", "webScraping"));
            eventWriter.add(newLine);

            for (WikipediaArticle article : articles) {
                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createStartElement("", "", "article"));
                eventWriter.add(newLine);

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(indent);
                createElement(eventWriter, "url", article.getUrl());
                createElement(eventWriter, "content", article.getContent());

                eventWriter.add(indent);
                eventWriter.add(indent);
                eventWriter.add(eventFactory.createEndElement("", "", "article"));
                eventWriter.add(newLine);
            }

            eventWriter.add(indent);
            eventWriter.add(eventFactory.createEndElement("", "", "webScraping"));
            eventWriter.add(newLine);

            // End root element
            eventWriter.add(eventFactory.createEndElement("", "", "session"));
            eventWriter.add(newLine);

            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();

            System.out.println("File saved!");

        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void createElement(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent newLine = eventFactory.createCharacters("\n");
        XMLEvent indent = eventFactory.createCharacters("    ");

        eventWriter.add(indent);
        eventWriter.add(eventFactory.createStartElement("", "", name));
        eventWriter.add(eventFactory.createCharacters(value));
        eventWriter.add(eventFactory.createEndElement("", "", name));
        eventWriter.add(newLine);
    }
}

class UploadedFile {
    @Getter
    private String fileName;

    @Getter
    private String content;

    // Constructor, getters, and setters

    public UploadedFile(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }
}

class ProcessedFile {
    @Getter
    private String fileName;

    @Getter
    private List<String> tokens;

    @Getter
    private List<String> lemmas;

    @Getter
    private List<String> posTags;

    // Constructor, getters, and setters

    public ProcessedFile(String fileName, List<String> tokens, List<String> lemmas, List<String> posTags) {
        this.fileName = fileName;
        this.tokens = tokens;
        this.lemmas = lemmas;
        this.posTags = posTags;
    }
}

class WikipediaArticle {
    @Getter
    private String url;
    @Getter
    private String content;

    // Constructor, getters, and setters

    public WikipediaArticle(String url, String content) {
        this.url = url;
        this.content = content;
    }
}
