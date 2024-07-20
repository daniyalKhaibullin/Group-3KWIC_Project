package de.uni.tuebingen.sfs.java2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * XML Writer V1.3
 *
 * This class provides functionality to write structured XML files for the KWIC application.
 * It uses StAX (Streaming API for XML) to create and write XML elements in a structured format.
 *
 * How It Works:
 * - Initializes an XMLEventWriter and XMLEventFactory for writing XML events.
 * - Writes the session details, uploaded text files, and web-scraped articles into the XML document.
 * - Each linguistic element (token, POS, lemma) is encapsulated within a word element.
 *
 * Usage:
 * 1. Ensure the directory "xml_outputs" exists in your project or it will be created automatically.
 * 2. Prepare the necessary data (session ID, start time, end time, user, uploaded texts, web scraped articles).
 * 3. Call the writeXML method with the required parameters.
 *
 * Example:
 * XMLWriter.writeXML("001", "2024-07-19T12:00:00", "2024-07-19T13:00:00",
 *                    "JohnDoe", searchTerm, searchResults, "xml_outputs/output.xml");
 *
 *
 * Note:
 * - This class uses StAX for writing XML data.
 * - Ensure all data passed to the method is correctly formatted and non-null to avoid exceptions.
 */
public class XMLWriter {

    public static void writeXML(String sessionId, String startTime, String endTime,
                                String user, String searchTerm, List<String> searchResults) {
        try {
            // Ensure the output directory exists
            File dir = new File("xml_outputs");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Create a unique output file path using the current timestamp
            String outputFilePath = "xml_outputs/output_" + System.currentTimeMillis() + ".xml";

            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            OutputStream outputStream = new FileOutputStream(outputFilePath);
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(outputStream, "UTF-8");
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            XMLEvent end = eventFactory.createDTD("\n");
            XMLEvent tab = eventFactory.createDTD("\t");

            eventWriter.add(eventFactory.createStartDocument("UTF-8", "1.0"));
            eventWriter.add(end);

            StartElement searchResultStartElement = eventFactory.createStartElement("", "", "searchresult");
            eventWriter.add(searchResultStartElement);
            eventWriter.add(end);

            StartElement fromStartElement = eventFactory.createStartElement("", "", "from");
            eventWriter.add(tab);
            eventWriter.add(fromStartElement);
            eventWriter.add(eventFactory.createAttribute("url", "aUrl"));
            eventWriter.add(eventFactory.createEndElement("", "", "from"));
            eventWriter.add(end);

            StartElement searchTermStartElement = eventFactory.createStartElement("", "", "searchTerm");
            eventWriter.add(tab);
            eventWriter.add(searchTermStartElement);
            eventWriter.add(eventFactory.createAttribute("value", searchTerm));
            eventWriter.add(eventFactory.createAttribute("type", "pos|lemma|word"));
            eventWriter.add(eventFactory.createEndElement("", "", "searchTerm"));
            eventWriter.add(end);

            StartElement resultsStartElement = eventFactory.createStartElement("", "", "results");
            eventWriter.add(tab);
            eventWriter.add(resultsStartElement);
            eventWriter.add(end);

            for (String result : searchResults) {
                StartElement resultStartElement = eventFactory.createStartElement("", "", "result");
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(resultStartElement);
                eventWriter.add(end);

                // Assuming result string is formatted as "word,lemma,pos"
                String[] parts = result.split(",");
                if (parts.length == 3) {
                    StartElement tStartElement = eventFactory.createStartElement("", "", "t");
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(tStartElement);
                    eventWriter.add(eventFactory.createAttribute("word", parts[0]));
                    eventWriter.add(eventFactory.createAttribute("lemma", parts[1]));
                    eventWriter.add(eventFactory.createAttribute("pos", parts[2]));
                    eventWriter.add(eventFactory.createEndElement("", "", "t"));
                    eventWriter.add(end);
                }

                EndElement resultEndElement = eventFactory.createEndElement("", "", "result");
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(resultEndElement);
                eventWriter.add(end);
            }

            EndElement resultsEndElement = eventFactory.createEndElement("", "", "results");
            eventWriter.add(tab);
            eventWriter.add(resultsEndElement);
            eventWriter.add(end);

            EndElement searchResultEndElement = eventFactory.createEndElement("", "", "searchresult");
            eventWriter.add(searchResultEndElement);
            eventWriter.add(end);

            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createNode(XMLEventWriter eventWriter, String name, String value, XMLEvent... indent) throws Exception {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        StartElement sElement = eventFactory.createStartElement("", "", name);

        for (XMLEvent event : indent) {
            eventWriter.add(event);
        }
        eventWriter.add(sElement);
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }
}
