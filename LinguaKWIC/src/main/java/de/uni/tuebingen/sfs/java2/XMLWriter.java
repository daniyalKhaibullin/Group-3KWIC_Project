/**
 * XMLWriter
 *
 * Version 1.0
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
 */



package de.uni.tuebingen.sfs.java2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import lombok.Setter;
import lombok.Getter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XMLWriter {

    public static void writeXML(String fileName, List<UploadedFile> uploadedFiles, List<ProcessedFile> processedFiles, List<WikipediaArticle> articles) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Create new document
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("session");
            doc.appendChild(rootElement);

            // Metadata
            Element metadata = doc.createElement("metadata");
            rootElement.appendChild(metadata);

            Element date = doc.createElement("date");
            date.appendChild(doc.createTextNode("2024-06-20"));
            metadata.appendChild(date);

            Element time = doc.createElement("time");
            time.appendChild(doc.createTextNode("15:00"));
            metadata.appendChild(time);

            // Uploaded Files
            Element uploadedFilesElement = doc.createElement("uploadedFiles");
            rootElement.appendChild(uploadedFilesElement);

            for (UploadedFile file : uploadedFiles) {
                Element fileElement = doc.createElement("file");
                uploadedFilesElement.appendChild(fileElement);

                Element fileNameElement = doc.createElement("fileName");
                fileNameElement.appendChild(doc.createTextNode(file.getFileName()));
                fileElement.appendChild(fileNameElement);

                Element contentElement = doc.createElement("content");
                contentElement.appendChild(doc.createTextNode(file.getContent()));
                fileElement.appendChild(contentElement);
            }

            // Linguistic Processing
            Element linguisticProcessingElement = doc.createElement("linguisticProcessing");
            rootElement.appendChild(linguisticProcessingElement);

            for (ProcessedFile processedFile : processedFiles) {
                Element fileElement = doc.createElement("file");
                linguisticProcessingElement.appendChild(fileElement);

                Element fileNameElement = doc.createElement("fileName");
                fileNameElement.appendChild(doc.createTextNode(processedFile.getFileName()));
                fileElement.appendChild(fileNameElement);

                Element tokensElement = doc.createElement("tokens");
                fileElement.appendChild(tokensElement);
                for (String token : processedFile.getTokens()) {
                    Element tokenElement = doc.createElement("token");
                    tokenElement.appendChild(doc.createTextNode(token));
                    tokensElement.appendChild(tokenElement);
                }

                Element lemmasElement = doc.createElement("lemmas");
                fileElement.appendChild(lemmasElement);
                for (String lemma : processedFile.getLemmas()) {
                    Element lemmaElement = doc.createElement("lemma");
                    lemmaElement.appendChild(doc.createTextNode(lemma));
                    lemmasElement.appendChild(lemmaElement);
                }

                Element posTagsElement = doc.createElement("posTags");
                fileElement.appendChild(posTagsElement);
                for (String pos : processedFile.getPosTags()) {
                    Element posElement = doc.createElement("pos");
                    posElement.appendChild(doc.createTextNode(pos));
                    posTagsElement.appendChild(posElement);
                }
            }

            // Web Scraping
            Element webScrapingElement = doc.createElement("webScraping");
            rootElement.appendChild(webScrapingElement);

            for (WikipediaArticle article : articles) {
                Element articleElement = doc.createElement("article");
                webScrapingElement.appendChild(articleElement);

                Element urlElement = doc.createElement("url");
                urlElement.appendChild(doc.createTextNode(article.getUrl()));
                articleElement.appendChild(urlElement);

                Element contentElement = doc.createElement("content");
                contentElement.appendChild(doc.createTextNode(article.getContent()));
                articleElement.appendChild(contentElement);
            }

            // Write the content into an XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
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

