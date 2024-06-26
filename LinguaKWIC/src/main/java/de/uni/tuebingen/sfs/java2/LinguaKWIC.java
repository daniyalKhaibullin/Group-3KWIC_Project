/**
 * V1.0 : in this version the program reads a String as input stream and get the sentences, tokens, postags and lemmas of
 * it. for now, it is working only with english. plan for the next version (v1.1) : instead of getting string as input, get a txt file and read it and do all the things
 * on the txt inside the txt file.
 * V1.2 : a language detector added, and now we can work with both german and english. the presses method now is divided to sub
 * methods
 * V1.3 : add the wikipediaScraper
 * V1.4 : adding some more helper methods and creating a TextSearch object for the future purposes
 * V1.5 : add the cache and making the search for repeated url faster.
 */
package de.uni.tuebingen.sfs.java2;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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

@Getter
public class LinguaKWIC implements Serializable {

    private static final long serialVersionUID = 42L;
    private static final String CACHE_DIR = "cache/";

    private File fileName;
    private URL url;
    private String text;
    private String lang;
    private String searchTopic;

    private List<String> sentences = new ArrayList<>();
    private List<List<String>> tokens = new ArrayList<>();
    private List<List<String>> posTags = new ArrayList<>();
    private List<List<String>> lemmas = new ArrayList<>();
    private TextSearch textSearch;

    /**
     * Constructs a LinguaKWIC object from a URL, extracting and processing text content.
     *
     * @param url The URL from which text content will be extracted and processed.
     * @throws IOException If there is an error in accessing or processing the URL content.
     */
    public LinguaKWIC(String url) throws IOException {
        this.url = new URL(url);
        WikipediaScraper scraper = new WikipediaScraper(this.url.toString());
        if (!loadFromCache()) {
            getTextFromUrl(scraper);
            process();
            saveToCache();
        }
    }

    /**
     * Constructs a LinguaKWIC object using a search topic and language, extracting and processing Wikipedia content.
     *
     * @param topic    The search topic or article title to extract and process from Wikipedia.
     * @param language The language of the Wikipedia article (e.g., "en" for English, "de" for German).
     * @throws IOException If there is an error in accessing or processing the Wikipedia content.
     */
    public LinguaKWIC(String topic, String language) throws IOException {
        this.searchTopic = topic;
        this.lang = language;
        WikipediaScraper scraper = new WikipediaScraper(this.searchTopic, this.lang);
        getTextFromUrl(scraper);
        process();
    }

    /**
     * Constructs a LinguaKWIC object from a local file, reading and processing its content.
     *
     * @param fileName The local file containing text content to be processed.
     */
    public LinguaKWIC(File fileName) {
        this.fileName = fileName;
        readFile(fileName.getAbsolutePath());
        process();
    }

    /**
     * Generates a cache file name based on the source (URL, file name, or search topic).
     *
     * @return The cache file name.
     */
    private String generateCacheFileName() {
        String source = url != null ? url.toString() : (fileName != null ? fileName.getAbsolutePath() : searchTopic + "-" + lang);
        return CACHE_DIR + source.hashCode() + ".ser";
    }

    /**
     * Saves the current state of the LinguaKWIC object to a cache file.
     */
    private void saveToCache() {
        String cacheFileName = generateCacheFileName();
        File cacheDir = new File(CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cacheFileName))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Error saving to cache: " + e.getMessage());
        }
    }

    /**
     * Loads the state of a LinguaKWIC object from a cache file if available.
     *
     * @return true if the cache file was successfully loaded, false otherwise.
     */
    private boolean loadFromCache() {
        String cacheFileName = generateCacheFileName();
        File cacheFile = new File(cacheFileName);
        if (!cacheFile.exists()) {
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cacheFile))) {
            LinguaKWIC cachedData = (LinguaKWIC) ois.readObject();
            this.text = cachedData.text;
            this.lang = cachedData.lang;
            this.sentences = cachedData.sentences;
            this.tokens = cachedData.tokens;
            this.posTags = cachedData.posTags;
            this.lemmas = cachedData.lemmas;
            this.textSearch = cachedData.textSearch;
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from cache: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts text from a URL using a Wikipedia scraper.
     *
     * @param scraper The Wikipedia scraper instance.
     */
    private void getTextFromUrl(WikipediaScraper scraper) {
        List<String> paragraphs = scraper.extractTextByTag("p");
        this.text = String.join(" ", paragraphs);
    }

    /**
     * Returns the language of the text.
     *
     * @return The language code (e.g., "en", "de").
     */
    public String getLang() {
        return this.lang != null && this.lang.length() >= 2 ? this.lang.substring(0, 2) : "en"; // Default to "en" if lang is not properly set
    }

    /**
     * Detects the language of the input text.
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
     * Loads NLP models and processes the text to extract sentences, tokens, POS tags, and lemmas.
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
     * Creates a TextSearch object based on the processed text data.
     */
    private void createTextSearchObject() {
        this.textSearch = new TextSearch(getTokens(), getPosTags(), getLemmas());
    }

    /**
     * Reads a file and sets its content to the text field.
     *
     * @param filePath The path of the file to read.
     */
    public void readFile(String filePath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            this.text = new String(bytes);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Processes the text content to extract sentences, tokens, POS tags, and lemmas.
     */
    private void process() {
        detectLanguage();
        if (lang == null) {
            System.err.println("Language detection failed.");
            return;
        }

        // Validate the language. Our program for now is only working with English and German
        if (!getLang().equals("en") && !getLang().equals("de")) {
            System.err.println("Unsupported language detected. Please provide a text in English or German.");
            return;
        }

        loadModelsAndProcessText();
        createTextSearchObject();
    }

    // Testing the class functionalities (feel free to remove this in production)
    public static void main(String[] args) throws IOException {
        File file = new File("aRandomText.txt");
        LinguaKWIC linguaKWIC = new LinguaKWIC(file);
<<<<<<< HEAD
        System.out.println(linguaKWIC.getSentences());
=======
//        System.out.println(linguaKWIC.getSentences());
>>>>>>> b16b7cb76ccf0077590fc1d78dae4c235c42881f
//        System.out.println(linguaKWIC.getTokens());
//        System.out.println(linguaKWIC.getPosTags());
//        System.out.println(linguaKWIC.getLemmas());
//        System.out.println(linguaKWIC.getLang());
        List<TextSearch.Pair> results = linguaKWIC.getTextSearch().searchByToken("Sonne");
        System.out.println("Search results for 'Sonne': " + results);

        long startTime = System.currentTimeMillis();
        LinguaKWIC linguaKWIC2 = new LinguaKWIC("https://en.wikipedia.org/wiki/Computer");
        System.out.println(linguaKWIC2.getSentences());
        System.out.println(linguaKWIC2.getTokens());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Execution time: " + duration + " milliseconds");

        long startTime2 = System.currentTimeMillis();
        LinguaKWIC linguaKWIC3 = new LinguaKWIC("https://en.wikipedia.org/wiki/Computer");
        System.out.println(linguaKWIC3.getSentences());
        System.out.println(linguaKWIC3.getTokens());
        long endTime2 = System.currentTimeMillis();
        long duration2 = endTime2 - startTime2;
        System.out.println("Execution time: " + duration2 + " milliseconds");
    }
}
