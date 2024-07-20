package de.uni.tuebingen.sfs.java2;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "searchresult")
class SearchResult {
    private String url;
    private String searchTerm;
    private List<Result> results;

    @XmlAttribute(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlElement(name = "searchTerm")
    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @XmlElement(name = "results")
    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}

class Result {
    private List<Token> tokens;

    @XmlElement(name = "result")
    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}

class Token {
    private String word;
    private String lemma;
    private String pos;

    @XmlAttribute(name = "word")
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @XmlAttribute(name = "lemma")
    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    @XmlAttribute(name = "pos")
    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}

public class XMLWriter {

    public static void writeXML(String sessionId, String startTime, String endTime,
                                String user, List<LinguaKWIC> uploadedTexts,
                                List<LinguaKWIC> webScrapedArticles) {
        try {
            // Create JAXB context and initialize Marshaller
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchResult.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Initialize search result object
            SearchResult searchResult = new SearchResult();
            searchResult.setUrl("aUrl");
            searchResult.setSearchTerm("needle");

            // Convert LinguaKWIC objects to Result objects
            List<Result> results = new ArrayList<>();
            for (LinguaKWIC text : uploadedTexts) {
                Result result = new Result();
                List<Token> tokens = new ArrayList<>();
                for (int i = 0; i < text.getTokens().size(); i++) {
                    for (int j = 0; j < text.getTokens().get(i).size(); j++) {
                        Token token = new Token();
                        token.setWord(text.getTokens().get(i).get(j));
                        token.setLemma(text.getLemmas().get(i).get(j));
                        token.setPos(text.getPosTags().get(i).get(j));
                        tokens.add(token);
                    }
                }
                result.setTokens(tokens);
                results.add(result);
            }
            searchResult.setResults(results);

            // Write to file
            File file = new File("xml_outputs/output_" + System.currentTimeMillis() + ".xml");
            jaxbMarshaller.marshal(searchResult, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
