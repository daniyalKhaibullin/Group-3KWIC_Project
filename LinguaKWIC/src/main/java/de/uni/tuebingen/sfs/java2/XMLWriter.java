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
 * List<LinguaKWIC> uploadedTexts = new ArrayList<>();
 * List<LinguaKWIC> webScrapedArticles = new ArrayList<>();
 * // Populate uploadedTexts and webScrapedArticles with appropriate data
 * XMLWriter.writeXML("001", "2024-07-19T12:00:00", "2024-07-19T13:00:00",
 *                    "JohnDoe", uploadedTexts, webScrapedArticles, "xml_outputs/output.xml");
 *
 *
 * Note:
 * - This class uses StAX for writing XML data.
 * - Ensure all data passed to the method is correctly formatted and non-null to avoid exceptions.
 */
public class XMLWriter {
    /**
     * Main writeXML method
     * @param sessionId
     * @param startTime
     * @param endTime
     * @param user
     * @param uploadedTexts
     * @param webScrapedArticles
     * @param outputFilePath
     */

    public static void writeXML(String sessionId, String startTime, String endTime,
                                String user, List<LinguaKWIC> uploadedTexts,
                                List<LinguaKWIC> webScrapedArticles, String outputFilePath) {
        try {
            // Ensure the output directory exists
            File dir = new File("xml_outputs");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            OutputStream outputStream = new FileOutputStream(outputFilePath);
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(outputStream, "UTF-8");
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            XMLEvent end = eventFactory.createDTD("\n");
            XMLEvent tab = eventFactory.createDTD("\t");

            // Start Document
            eventWriter.add(eventFactory.createStartDocument("UTF-8", "1.0"));
            eventWriter.add(end);

            // Start KWICApplication element
            StartElement kwicStartElement = eventFactory.createStartElement("", "", "KWICApplication");
            eventWriter.add(kwicStartElement);
            eventWriter.add(end);

            // Start Session element
            StartElement sessionStartElement = eventFactory.createStartElement("", "", "Session");
            eventWriter.add(tab);
            eventWriter.add(sessionStartElement);
            eventWriter.add(end);

            createNode(eventWriter, "SessionID", sessionId, tab);
            createNode(eventWriter, "StartTime", startTime, tab);
            createNode(eventWriter, "EndTime", endTime, tab);
            createNode(eventWriter, "User", user, tab);

            // Uploaded Texts
            StartElement uploadedTextsStartElement = eventFactory.createStartElement("", "", "UploadedTexts");
            eventWriter.add(tab);
            eventWriter.add(uploadedTextsStartElement);
            eventWriter.add(end);

            for (LinguaKWIC textFile : uploadedTexts) {
                StartElement textFileStartElement = eventFactory.createStartElement("", "", "TextFile");
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(textFileStartElement);
                eventWriter.add(end);

                createNode(eventWriter, "FileName", textFile.getFileName().getName(), tab, tab);
                createNode(eventWriter, "Content", textFile.getText(), tab, tab);

                // Linguistic Processing
                StartElement linguisticProcessingStartElement = eventFactory.createStartElement("", "", "LinguisticProcessing");
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(linguisticProcessingStartElement);
                eventWriter.add(end);

                for (int i = 0; i < textFile.getSentences().size(); i++) {
                    StartElement sentenceStartElement = eventFactory.createStartElement("", "", "Sentence");
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(sentenceStartElement);
                    eventWriter.add(end);

                    createNode(eventWriter, "Text", textFile.getSentences().get(i), tab, tab, tab);

                    StartElement wordsStartElement = eventFactory.createStartElement("", "", "Words");
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(wordsStartElement);
                    eventWriter.add(end);

                    for (int j = 0; j < textFile.getTokens().get(i).size(); j++) {
                        StartElement wordStartElement = eventFactory.createStartElement("", "", "Word");
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(wordStartElement);
                        eventWriter.add(end);

                        createNode(eventWriter, "Token", textFile.getTokens().get(i).get(j), tab, tab, tab, tab);
                        createNode(eventWriter, "POS", textFile.getPosTags().get(i).get(j), tab, tab, tab, tab);
                        createNode(eventWriter, "Lemma", textFile.getLemmas().get(i).get(j), tab, tab, tab, tab);

                        EndElement wordEndElement = eventFactory.createEndElement("", "", "Word");
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(wordEndElement);
                        eventWriter.add(end);
                    }

                    EndElement wordsEndElement = eventFactory.createEndElement("", "", "Words");
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(wordsEndElement);
                    eventWriter.add(end);

                    EndElement sentenceEndElement = eventFactory.createEndElement("", "", "Sentence");
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(sentenceEndElement);
                    eventWriter.add(end);
                }

                EndElement linguisticProcessingEndElement = eventFactory.createEndElement("", "", "LinguisticProcessing");
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(linguisticProcessingEndElement);
                eventWriter.add(end);

                EndElement textFileEndElement = eventFactory.createEndElement("", "", "TextFile");
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(textFileEndElement);
                eventWriter.add(end);
            }


            EndElement uploadedTextsEndElement = eventFactory.createEndElement("", "", "UploadedTexts");
            eventWriter.add(tab);
            eventWriter.add(uploadedTextsEndElement);
            eventWriter.add(end);

            // Web Scraped Content
            StartElement webScrapedContentStartElement = eventFactory.createStartElement("", "", "WebScrapedContent");
            eventWriter.add(tab);
            eventWriter.add(webScrapedContentStartElement);
            eventWriter.add(end);

            for (LinguaKWIC article : webScrapedArticles) {
                StartElement articleStartElement = eventFactory.createStartElement("", "", "Article");
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(articleStartElement);
                eventWriter.add(end);

                createNode(eventWriter, "URL", article.getUrl().toString(), tab, tab);
                createNode(eventWriter, "Content", article.getText(), tab, tab);

                // Linguistic Processing
                StartElement linguisticProcessingStartElement = eventFactory.createStartElement("", "", "LinguisticProcessing");
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(linguisticProcessingStartElement);
                eventWriter.add(end);

                for (int i = 0; i < article.getSentences().size(); i++) {
                    StartElement sentenceStartElement = eventFactory.createStartElement("", "", "Sentence");
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(sentenceStartElement);
                    eventWriter.add(end);

                    createNode(eventWriter, "Text", article.getSentences().get(i), tab, tab, tab);

                    StartElement wordsStartElement = eventFactory.createStartElement("", "", "Words");
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(wordsStartElement);
                    eventWriter.add(end);

                    for (int j = 0; j < article.getTokens().get(i).size(); j++) {
                        StartElement wordStartElement = eventFactory.createStartElement("", "", "Word");
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(wordStartElement);
                        eventWriter.add(end);

                        createNode(eventWriter, "Token", article.getTokens().get(i).get(j), tab, tab, tab, tab);
                        createNode(eventWriter, "POS", article.getPosTags().get(i).get(j), tab, tab, tab, tab);
                        createNode(eventWriter, "Lemma", article.getLemmas().get(i).get(j), tab, tab, tab, tab);

                        EndElement wordEndElement = eventFactory.createEndElement("", "", "Word");
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(tab);
                        eventWriter.add(wordEndElement);
                        eventWriter.add(end);
                    }

                    EndElement wordsEndElement = eventFactory.createEndElement("", "", "Words");
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(wordsEndElement);
                    eventWriter.add(end);

                    EndElement sentenceEndElement = eventFactory.createEndElement("", "", "Sentence");
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(tab);
                    eventWriter.add(sentenceEndElement);
                    eventWriter.add(end);
                }

                EndElement linguisticProcessingEndElement = eventFactory.createEndElement("", "", "LinguisticProcessing");
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(linguisticProcessingEndElement);
                eventWriter.add(end);

                EndElement articleEndElement = eventFactory.createEndElement("", "", "Article");
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(articleEndElement);
                eventWriter.add(end);
            }

            EndElement webScrapedContentEndElement = eventFactory.createEndElement("", "", "WebScrapedContent");
            eventWriter.add(tab);
            eventWriter.add(webScrapedContentEndElement);
            eventWriter.add(end);

            // End Session element
            EndElement sessionEndElement = eventFactory.createEndElement("", "", "Session");
            eventWriter.add(tab);
            eventWriter.add(sessionEndElement);
            eventWriter.add(end);

            // End KWICApplication element
            EndElement kwicEndElement = eventFactory.createEndElement("", "", "KWICApplication");
            eventWriter.add(kwicEndElement);
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
