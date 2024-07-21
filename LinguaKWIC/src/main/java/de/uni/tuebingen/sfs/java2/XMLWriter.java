package de.uni.tuebingen.sfs.java2;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * XMLWriter class, simplified version, uses the XML Structure of the document
 * provided in the DSA II
 * <p>
 * V1.5
 * <p>
 * Example:
 * // Get the search results
 * List<TextSearch.Pair> results = linguaKWIC.getTextSearch().getLastSearchResults();
 * List<List<String>> tokens = linguaKWIC.getTokens();
 * List<List<String>> lemmas = linguaKWIC.getLemmas();
 * List<List<String>> posTags = linguaKWIC.getPosTags();
 * <p>
 * // Write the XML file
 * XMLWriter xmlWriter = new XMLWriter();
 * xmlWriter.writeXML(filePath, results, tokens, lemmas, posTags);
 * <p>
 * Try to use that, see if it works
 *
 * edited by Aida:
 * in this version (V1.5) we are also showing the result in the <t>result </t> and it has also a well indentation.
 */
public class XMLWriter {

    public void writeXML(String filePath, List<TextSearch.Pair> results, List<List<String>> tokens, List<List<String>> lemmas,
                         List<List<String>> posTags, String target ,String needle, String type) throws XMLStreamException, IOException {

        RecordKeeper record = new RecordKeeper(results, tokens, lemmas, posTags, target, needle,type);
        List<String> theRecord = record.getSingleResult();

        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        OutputStream outputStream = new FileOutputStream(filePath);
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(outputStream);
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");

        // Create indentation strings
        String indent1 = "  ";
        String indent2 = indent1 + indent1;

        // Start document
        eventWriter.add(eventFactory.createStartDocument());
        eventWriter.add(end);

        // Write the root element
        eventWriter.add(eventFactory.createStartElement("", "", "searchresult"));
        eventWriter.add(end);

        // Write the "from" element with a placeholder URL
        eventWriter.add(eventFactory.createCharacters(indent1));
        eventWriter.add(eventFactory.createStartElement("", "", "from"));
        eventWriter.add(eventFactory.createAttribute("url", target));
        eventWriter.add(eventFactory.createEndElement("", "", "from"));
        eventWriter.add(end);

        // Write the "searchTerm" element manually as a self-closing tag
        eventWriter.add(eventFactory.createCharacters(indent1));
//        eventWriter.add(eventFactory.createCharacters("<searchTerm value=\"needle\" type=\"pos|lemma|word\"/>"));
        eventWriter.add(eventFactory.createStartElement("", "", "searchTerm"));
        eventWriter.add(eventFactory.createAttribute("value(s)", needle));
        eventWriter.add(eventFactory.createAttribute("type", type));
        eventWriter.add(eventFactory.createEndElement("", "", "searchTerm"));
        eventWriter.add(end);


        // Write the "results" element
        eventWriter.add(eventFactory.createCharacters(indent1));
        eventWriter.add(eventFactory.createStartElement("", "", "results"));
        eventWriter.add(end);

        int counter = 0;
        // Write individual "result" elements
        for (TextSearch.Pair result : results) {
            writeResult(eventWriter, eventFactory, end, tokens.get(result.getSentenceIndex()).get(result.getTokenIndex()), lemmas.get(result.getSentenceIndex()).get(result.getTokenIndex()), posTags.get(result.getSentenceIndex()).get(result.getTokenIndex()), theRecord.get(counter), indent2);
            counter++;
        }

        // End "results" element
        eventWriter.add(eventFactory.createCharacters(indent1));
        eventWriter.add(eventFactory.createEndElement("", "", "results"));
        eventWriter.add(end);

        // End root element
        eventWriter.add(eventFactory.createCharacters(""));
        eventWriter.add(eventFactory.createEndElement("", "", "searchresult"));
        eventWriter.add(end);

        // End document
        eventWriter.add(eventFactory.createEndDocument());

        eventWriter.close();
        outputStream.close();
    }

    private void writeResult(XMLEventWriter eventWriter, XMLEventFactory eventFactory, XMLEvent end, String word, String lemma, String pos, String resultOfSearch, String indent) throws XMLStreamException {
        // Write the "result" element
        eventWriter.add(eventFactory.createCharacters(indent));
        eventWriter.add(eventFactory.createStartElement("", "", "result"));
        eventWriter.add(end);

        // Write the "t" element with attributes
        eventWriter.add(eventFactory.createCharacters(indent + "  "));
        eventWriter.add(eventFactory.createStartElement("", "", "t"));
        eventWriter.add(eventFactory.createAttribute("word", word));
        eventWriter.add(eventFactory.createAttribute("lemma", lemma));
        eventWriter.add(eventFactory.createAttribute("pos", pos));
        eventWriter.add(eventFactory.createEndElement("", "", "t"));
        eventWriter.add(end);
        //write the context of result
        eventWriter.add(eventFactory.createCharacters(indent + "    "));
        eventWriter.add(eventFactory.createStartElement("", "", "p"));
        eventWriter.add(eventFactory.createCharacters(resultOfSearch));
        eventWriter.add(eventFactory.createCharacters(indent + "    "));
        eventWriter.add(eventFactory.createEndElement("", "", "p"));
        eventWriter.add(end);

        // End "result" element
        eventWriter.add(eventFactory.createCharacters(indent));
        eventWriter.add(eventFactory.createEndElement("", "", "result"));
        eventWriter.add(end);
    }
}
