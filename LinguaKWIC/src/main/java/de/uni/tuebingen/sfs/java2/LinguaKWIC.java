/**
 * V1.0 : in this version the program reads a String as input stream and get the sentences, tokens, postags and lemmas of
 * it. for now, it is working only with english. plan for the next version (v1.1) : instead of getting string as input, get a txt file and read it and do all the things
 * on the txt inside the txt file.
 * V1.2 : a language detector added, and now we can work with both german and english. the presses method now is divided to sub
 * methods
 * V1.3 : add the wikipediaScraper
 * V1.4 : adding some more helper methods and creating a TextSearch object for the future purposes
 */

package de.uni.tuebingen.sfs.java2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.util.ArrayList;

public class LinguaKWIC {


    private File fileName; //name of the input file
    /**
     * -- GETTER --
     * Returns the url
     */
    @Getter
    private String url; //url to wikipedia
    /**
     * -- GETTER --
     * Returns the text of url
     */
    @Getter
    private String text; //content of the text that we want to do the analysis on it
    private String lang; // language of text
    private String searchTopic; // the topic that we want to search in wikipedia- for bonus version

    /**
     * -- GETTER --
     * Return an array with the sentences of the CorpusBuilder
     */
    @Getter
    private List<String> sentences = new ArrayList<>();  // list of sentences
    /**
     * -- GETTER --
     * Return a List of List with the tokens/words of the text of CorpusBuilder. The
     * first list holds the words of the
     * first sentence, the second list holds the words of the second sentence and so
     * on.
     */
    @Getter
    private List<List<String>> tokens = new ArrayList<>(); // list of list of tokens for each sentence
    /**
     * -- GETTER --
     * Return a List of List with the POS tags of the text of CorpusBuilder. The
     * first list holds the POS tags of the
     * first sentence, the second list holds the POS tags of the second sentence and
     * so on.
     */
    @Getter
    private List<List<String>> posTags = new ArrayList<>(); // .... tags
    /**
     * -- GETTER --
     * Return a List of List with the Lemmas of the text of CorpusBuilder. The first
     * list holds the lemmas of the
     * first sentence, the second list holds the Lemmas of the second sentence and
     * so on.
     */
    @Getter
    private List<List<String>> lemmas = new ArrayList<>(); // lemmas

    public TextSearch textSearch;


    /**
     * Create a LinguaKWIC which generates POS tags and Lemmas for text.
     *
     * @param url The text which should be annotated.
     */
    public LinguaKWIC(String url) throws IOException {
        this.url = url;
        WikipediaScraper scraper = new WikipediaScraper(this.url);
        getTextUrl(scraper);
        process();
    }

    /**
     * Create a LinguaKWIC which generates POS tags and Lemmas for text.
     *
     * @param topic    The text which should be annotated.
     * @param language The text which should be annotated.
     */
    public LinguaKWIC(String topic, String language) throws IOException {
        this.lang = language;
        this.searchTopic = topic;
        WikipediaScraper scraper = new WikipediaScraper(this.searchTopic, this.lang);
        getTextUrl(scraper);
        process();
    }

    /**
     * Create a LinguaKWIC which generates POS tags and Lemmas for text.
     *
     * @param fileName The file containing text which should be annotated.
     */
    public LinguaKWIC(File fileName) {
        this.fileName = fileName;
        readFile(fileName.getAbsolutePath());
        process();
    }


    /**
     * saving the text with tag p from the url to our text
     *
     * @param scraper our scraper object
     */
    private void getTextUrl(WikipediaScraper scraper) {
        List<String> paragraphs = scraper.extractTextByTag("p");
        this.text = String.join(" ", paragraphs);
    }

    /**
     * Return the language of the text
     *
     * @return The language of the text
     */
    public String getLang() {
        return this.lang != null && this.lang.length() >= 2 ? this.lang.substring(0, 2) : "en"; // Default to "en" if lang is not properly set
    }

    /**
     * Detect the language of the input text
     */
    private void detectLanguage() {
        try (InputStream detectorModelIn = Files.newInputStream(Paths.get("langdetect-183.bin"))) {
            LanguageDetectorModel model = new LanguageDetectorModel(detectorModelIn);
            LanguageDetectorME languageDetector = new LanguageDetectorME(model);
            Language bestLanguage = languageDetector.predictLanguage(this.text);
            this.lang = bestLanguage.getLang();
        } catch (Exception e) {
            System.err.println("Error detecting language: " + e.getMessage());
        }
    }

    /**
     * Load NLP models and process the text to extract sentences, tokens, POS tags, and lemmas
     */
    private void loadModelsAndProcessText() {
        String lang = getLang(); // Ensure language is set properly

        try (InputStream sentenceModelIn = Files.newInputStream(Paths.get(lang + "/" + lang + "-sent.bin"));
             InputStream tokenModelIn = Files.newInputStream(Paths.get(lang + "/" + lang + "-token.bin"));
             InputStream posModelIn = Files.newInputStream(Paths.get(lang + "/" + lang + "-pos-maxent.bin"));
             InputStream lemmaModelIn = Files.newInputStream(Paths.get(lang + "/" + lang + "-lemmatizer.bin"))) {

            SentenceDetectorME sentenceDetector = new SentenceDetectorME(new SentenceModel(sentenceModelIn));
            Tokenizer tokenizer = new TokenizerME(new TokenizerModel(tokenModelIn));
            POSTaggerME posTagger = new POSTaggerME(new POSModel(posModelIn));
            LemmatizerME lemmatizer = new LemmatizerME(new LemmatizerModel(lemmaModelIn));

            String[] sentencesArray = sentenceDetector.sentDetect(getText());
            this.sentences.addAll(Arrays.asList(sentencesArray));

            for (String sentence : sentencesArray) {

                String[] sentenceTokens = tokenizer.tokenize(sentence);
                String[] tags = posTagger.tag(sentenceTokens);
                String[] lemmasArray = lemmatizer.lemmatize(sentenceTokens, tags);

                List<String> tokenList = new ArrayList<>(Arrays.asList(sentenceTokens));
                List<String> posList = new ArrayList<>(Arrays.asList(tags));
                List<String> lemmaList = new ArrayList<>(Arrays.asList(lemmasArray));

                tokens.add(tokenList);
                posTags.add(posList);
                lemmas.add(lemmaList);
            }

        } catch (Exception e) {
            System.err.println("Error processing text" + e.getMessage());
        }
    }

    /**
     * creating a TextSearch object out of our text
     */
    private void getReadyToSearch() {
        this.textSearch = new TextSearch(getTokens(), getPosTags(), getLemmas());
    }

    /**
     * convert a file to an arrayList
     *
     * @param filePath
     */
    public void readFile(String filePath) {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            StringBuilder context = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                context.append(line);
            }
            this.text = context.toString();
        } catch (Exception e) {
            System.err.println("Error opening file" + e.getMessage());
        }
    }

    /**
     * in this method, we extract the sentences, tokens and postagss and lemmas from
     * the input string
     */
    private void process() {
        detectLanguage();
        if (lang == null) {
            System.err.println("Language detection failed.");
            return;
        }

        // Validate the language. our program for now is only working with english and german
        if (!getLang().equals("en") && !getLang().equals("de")) {
            System.err.println("Unsupported language detected. Please provide a text in English or German.");
            return;
        }

        loadModelsAndProcessText();
        getReadyToSearch();
    }


    // Just testing whether it works or not, feel free to delete this part
    public static void main(String[] args) throws IOException {
        File file = new File("aRandomText.txt");
        LinguaKWIC linguaKWIC = new LinguaKWIC(file);
        System.out.println(linguaKWIC.getSentences());
        System.out.println(linguaKWIC.getTokens());
        System.out.println(linguaKWIC.getPosTags());
        System.out.println(linguaKWIC.getLemmas());
        System.out.println(linguaKWIC.getLang());

        LinguaKWIC linguaKWIC2 = new LinguaKWIC("https://en.wikipedia.org/wiki/Computer");
        System.out.println(linguaKWIC2.getSentences());
        System.out.println(linguaKWIC2.getTokens());
        System.out.println(linguaKWIC2.getPosTags());
        System.out.println(linguaKWIC2.getLemmas());
        System.out.println(linguaKWIC2.getLang());
    }

}





