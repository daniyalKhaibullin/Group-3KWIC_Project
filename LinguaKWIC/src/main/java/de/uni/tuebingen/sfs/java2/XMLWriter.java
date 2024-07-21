package de.uni.tuebingen.sfs.java2;

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
 * XMLWriter class, simplified version, uses the XML Structure of the document
 * provided in the DSA II
 *
 * V1.4
 *
 * Example:
 * // Get the search results
 *                 List<TextSearch.Pair> results = linguaKWIC.getTextSearch().getLastSearchResults();
 *                 List<List<String>> tokens = linguaKWIC.getTokens();
 *                 List<List<String>> lemmas = linguaKWIC.getLemmas();
 *                 List<List<String>> posTags = linguaKWIC.getPosTags();
 *
 *                 // Write the XML file
 *                 XMLWriter xmlWriter = new XMLWriter();
 *                 xmlWriter.writeXML(filePath, results, tokens, lemmas, posTags);
 *
 * Try to use that, see if it works
 *
 */
public class XMLWriter {

    public void writeXML(String filePath, List<TextSearch.Pair> results, List<List<String>> tokens, List<List<String>> lemmas, List<List<String>> posTags) throws XMLStreamException, IOException {

        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        OutputStream outputStream = new FileOutputStream(filePath);
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(outputStream);
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");

        // Start document
        eventWriter.add(eventFactory.createStartDocument());
        eventWriter.add(end);

        // Write the root element
        eventWriter.add(eventFactory.createStartElement("", "", "searchresult"));
        eventWriter.add(end);

        // Write the "from" element with a placeholder URL (you can modify this based on your requirements)
        eventWriter.add(eventFactory.createStartElement("", "", "from"));
        eventWriter.add(eventFactory.createAttribute("url", "aUrl"));
        eventWriter.add(eventFactory.createEndElement("", "", "from"));
        eventWriter.add(end);

        // Write the "searchTerm" element with attributes (modify this based on your requirements)
        eventWriter.add(eventFactory.createStartElement("", "", "searchTerm"));
        eventWriter.add(eventFactory.createAttribute("value", "needle"));
        eventWriter.add(eventFactory.createAttribute("type", "pos|lemma|word"));
        eventWriter.add(eventFactory.createEndElement("", "", "searchTerm"));
        eventWriter.add(end);

        // Write the "results" element
        eventWriter.add(eventFactory.createStartElement("", "", "results"));
        eventWriter.add(end);

        // Write individual "result" elements
        for (TextSearch.Pair result : results) {
            writeResult(eventWriter, eventFactory, end, tokens.get(result.getSentenceIndex()).get(result.getTokenIndex()), lemmas.get(result.getSentenceIndex()).get(result.getTokenIndex()), posTags.get(result.getSentenceIndex()).get(result.getTokenIndex()));
        }

        // End "results" element
        eventWriter.add(eventFactory.createEndElement("", "", "results"));
        eventWriter.add(end);

        // End root element
        eventWriter.add(eventFactory.createEndElement("", "", "searchresult"));
        eventWriter.add(end);

        // End document
        eventWriter.add(eventFactory.createEndDocument());

        eventWriter.close();
        outputStream.close();
    }

    private void writeResult(XMLEventWriter eventWriter, XMLEventFactory eventFactory, XMLEvent end, String word, String lemma, String pos) throws XMLStreamException {
        // Write the "result" element
        eventWriter.add(eventFactory.createStartElement("", "", "result"));
        eventWriter.add(end);

        // Write the "t" element with attributes
        eventWriter.add(eventFactory.createStartElement("", "", "t"));
        eventWriter.add(eventFactory.createAttribute("word", word));
        eventWriter.add(eventFactory.createAttribute("lemma", lemma));
        eventWriter.add(eventFactory.createAttribute("pos", pos));
        eventWriter.add(eventFactory.createEndElement("", "", "t"));
        eventWriter.add(end);

        // End "result" element
        eventWriter.add(eventFactory.createEndElement("", "", "result"));
        eventWriter.add(end);
    }
}
