/**
 * Wikipedia Scraper - class that enables the users to easily
 * interact with JSoup without touching documents, elements, and other
 * complicated tools in JSoup.
 * Created to facilitate scraping Wikipedia pages.
 * OOP based, trivial and relatively easy for experienced Java developers
 *
 * Features:
 * - Connect to a Wikipedia page and scrape its contents.
 * - Extract main title and language of the Wikipedia page.
 * - Extract sections of the page including the top section 'mw-content-text'.
 * - Extract text based on specific tags or classes.
 *
 * Changes with V1.2:
 * - Enable Cache Content that can store cache - wikipedia links that previously have been accessed
 * - Clearer section division enabled
 * - Catch excpetions and errors.
 * - Get rid of all the space for unnecessary getters and setters, can be done with lombok, pom.xml changed
 *
 * Version: V1.2.
 */

package de.uni.tuebingen.sfs.java2;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class WikipediaScraper {
    @Getter @Setter
    private Document document;
    @Getter @Setter
    private String url;
    @Getter @Setter
    private String mainTitle;
    @Getter @Setter
    private String language;
    @Getter @Setter
    private List<Section> sections;
    private static final HashMap<String, CachedContent> cache = new HashMap<>();

    public WikipediaScraper() {
        sections = new ArrayList<>();
    }

    public WikipediaScraper(String url) throws IOException {
        this();
        connectToWikipedia(url);
    }

    /**
     * Connects to the specified Wikipedia URL and initiates scraping.
     * V1.2 - added cacheContent - a hash table of wikipedia links, and its info
     *
     * @param url The URL of the Wikipedia page to scrape.
     * @throws IOException If an I/O error occurs while connecting to the URL.
     */
    public void connectToWikipedia(String url) {
        this.url = url;
        try {
            if (cache.containsKey(url) && !cache.get(url).isExpired()) {
                CachedContent cachedContent = cache.get(url);
                this.document = cachedContent.getDocument();
                this.mainTitle = cachedContent.getMainTitle();
                this.language = cachedContent.getLanguage();
                this.sections = cachedContent.getSections();
            } else {
                this.document = Jsoup.connect(url).get();
                scrapeWikipedia();
                cache.put(url, new CachedContent(document, mainTitle, language, sections));
            }
        } catch (IOException e) {
            System.err.println("Error connecting to Wikipedia: " + e.getMessage());
        }
    }

    /**
     * Scrapes the Wikipedia page for the main title, language, and sections.
     * V1.2 - added the level of sections to not get confused with the formatting
     */
    private void scrapeWikipedia() {
        if (document != null) {
            this.mainTitle = document.title();
            this.language = document.selectFirst("html").attr("lang");

            Element bodyContent = document.selectFirst("#bodyContent");
            Element topContent = bodyContent.selectFirst("#mw-content-text");

            Section topSection = new Section("Introduction", 1);
            if (topContent != null) {
                topSection.addParagraph(formatText(topContent.text()));
            }
            sections.add(topSection);

            Elements sectionElements = bodyContent.select("h2, h3, p");

            Section currentSection = topSection;
            int currentLevel = 1;

            for (Element element : sectionElements) {
                if (element.tagName().equals("h2")) {
                    currentSection = new Section(element.text(), 2);
                    sections.add(currentSection);
                    currentLevel = 2;
                } else if (element.tagName().equals("h3")) {
                    currentSection = new Section(element.text(), 3);
                    sections.add(currentSection);
                    currentLevel = 3;
                } else {
                    if (currentLevel == 1) {
                        topSection.addParagraph(formatText(element.text()));
                    } else {
                        currentSection.addParagraph(formatText(element.text()));
                    }
                }
            }
        }
    }

    private String formatText(String text) {
        // Example of formatting: splitting into multiple lines for readability
        return text.replaceAll("(?<!\\n)\\s{2,}", "\n");
    }

    /**
     * Extracts text from the document based on the specified tag.
     * V1.2 modified extract by tag - works when the document exists to prevent null errors
     *
     * @param tag The HTML tag to extract text from.
     * @return A list of text extracted from the specified tag.
     */
    public List<String> extractTextByTag(String tag) {
        List<String> textList = new ArrayList<>();
        if (document != null) {
            Elements elements = document.select(tag);
            for (Element element : elements) {
                textList.add(element.text());
            }
        }
        return textList;
    }

    /**
     * Extracts text from the document based on the specified class.
     *
     * @param className The HTML class to extract text from.
     * @return A list of text extracted from the specified class.
     */
    public List<String> extractTextByClass(String className) {
        List<String> textList = new ArrayList<>();
        if (document != null) {
            Elements elements = document.getElementsByClass(className);
            for (Element element : elements) {
                textList.add(element.text());
            }
        }
        return textList;
    }

    /**
     * Converts the scraped content to a StringBuilder for easy representation.
     * Works with the toString method.
     *
     * @return A StringBuilder representation of the scraped content.
     */
    public StringBuilder toStringBuilder() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(mainTitle).append("\n");
        sb.append("Language: ").append(language).append("\n");
        for (Section section : sections) {
            sb.append(section.toString());
        }
        return sb;
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }

    // Inner class to represent a section
    @Getter @Setter
    public static class Section {
        private String title;
        private List<String> paragraphs;
        private int level;

        public Section(String title, int level) {
            this.title = title;
            this.level = level;
            this.paragraphs = new ArrayList<>();
        }

        public void addParagraph(String paragraph) {
            this.paragraphs.add(paragraph);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Section: ").append(title).append("\n");
            for (String paragraph : paragraphs) {
                sb.append(paragraph).append("\n");
            }
            return sb.toString();
        }
    }

    @Getter @Setter
    private static class CachedContent {
        private final Document document;
        private final String mainTitle;
        private final String language;
        private final List<Section> sections;
        private final long timestamp;

        public CachedContent(Document document, String mainTitle, String language, List<Section> sections){
            this.document = document;
            this.mainTitle = mainTitle;
            this.language = language;
            this.sections = sections;
            this.timestamp = System.currentTimeMillis();
        }

        //for timestamps, and to not clog the cache, so that it cleans itself
        public boolean isExpired(){
            final long ONE_HOUR = 60 * 60 * 1000;
            return (System.currentTimeMillis() - timestamp) > ONE_HOUR;
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the title of the Wikipedia page:");
            String mainTitle = scanner.nextLine();
            System.out.println("Enter the language of the Wikipedia page (e.g., en for English):");
            String language = scanner.nextLine();
            String url = "https://" + language + ".wikipedia.org/wiki/" + mainTitle.replace(" ", "_");

            WikipediaScraper scraper = new WikipediaScraper(url);
            System.out.println(scraper);

            // Example of extracting text by tag
            System.out.println("Extracted paragraphs:");
            List<String> paragraphs = scraper.extractTextByTag("p");
            for (String paragraph : paragraphs) {
                System.out.println(paragraph);
            }

            // Example of extracting text by class
            System.out.println("Extracted elements by class 'mw-headline':");
            List<String> elementsByClass = scraper.extractTextByClass("mw-headline");
            for (String text : elementsByClass) {
                System.out.println(text);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

