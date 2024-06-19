/**
 * Wikipedia Scraper - A class that enables users to easily scrape Wikipedia pages using JSoup.
 * This class abstracts away the complexity of interacting with JSoup documents, elements, etc.
 *
 * Features:
 * - Connect to a Wikipedia page and scrape its contents.
 * - Extract main title and language of the Wikipedia page.
 * - Extract sections of the page including the main content.
 * - Extract text based on specific tags or classes.
 *
 * Version: V1.3
 * Changes:
 * - Added a new constructor that takes a topic and language to generate the Wikipedia URL.
 * - Updated manual and usage instructions.
 * - Improved error handling for the new constructor.
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

    // Constructor initializing the sections list
    public WikipediaScraper() {
        sections = new ArrayList<>();
    }

    // Constructor connecting to a Wikipedia page by URL
    public WikipediaScraper(String url) throws IOException {
        this();
        connectToWikipedia(url);
    }

    // New constructor generating URL from topic and language
    public WikipediaScraper(String topic, String language) {
        this();
        this.url = generateWikipediaURL(topic, language);
        connectToWikipedia(this.url);
    }

    /**
     * Generates a Wikipedia URL from a given topic and language.
     *
     * @param topic The topic of the Wikipedia page.
     * @param language The language code (e.g., "en" for English).
     * @return The generated Wikipedia URL.
     */
    private String generateWikipediaURL(String topic, String language) {
        return "https://" + language + ".wikipedia.org/wiki/" + topic.replace(" ", "_");
    }

    /**
     * Connects to the specified Wikipedia URL and initiates scraping.
     * Checks cache first to avoid redundant network calls.
     *
     * @param url The URL of the Wikipedia page to scrape.
     */
    public void connectToWikipedia(String url) {
        this.url = url;
        try {
            // Check if the URL is already cached and the cache is not expired
            if (cache.containsKey(url) && !cache.get(url).isExpired()) {
                // Load from cache
                CachedContent cachedContent = cache.get(url);
                this.document = cachedContent.getDocument();
                this.mainTitle = cachedContent.getMainTitle();
                this.language = cachedContent.getLanguage();
                this.sections = cachedContent.getSections();
            } else {
                // Connect and scrape the page if not cached
                this.document = Jsoup.connect(url).get();
                scrapeWikipedia();
                // Store in cache
                cache.put(url, new CachedContent(document, mainTitle, language, sections));
            }
        } catch (IOException e) {
            System.err.println("Error connecting to Wikipedia: " + e.getMessage());
        }
    }

    /**
     * Scrapes the Wikipedia page for the main title, language, and sections.
     * Extracts content from the 'mw-content-text' section and further divides it into sub-sections.
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
                switch (element.tagName()) {
                    case "h2":
                        currentSection = new Section(element.text(), 2);
                        sections.add(currentSection);
                        currentLevel = 2;
                        break;
                    case "h3":
                        currentSection = new Section(element.text(), 3);
                        sections.add(currentSection);
                        currentLevel = 3;
                        break;
                    default:
                        if (currentLevel == 1) {
                            topSection.addParagraph(formatText(element.text()));
                        } else {
                            currentSection.addParagraph(formatText(element.text()));
                        }
                        break;
                }
            }
        }
    }

    /**
     * Formats text by replacing multiple consecutive spaces with new lines for readability.
     *
     * @param text The text to format.
     * @return The formatted text.
     */
    private String formatText(String text) {
        return text.replaceAll("(?<!\\n)\\s{2,}", "\n");
    }

    /**
     * Extracts text from the document based on the specified HTML tag.
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
     * Extracts text from the document based on the specified HTML class.
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

    /**
     * Inner class to represent a section of the Wikipedia page.
     */
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

    /**
     * Inner class to represent cached content of a Wikipedia page.
     */
    @Getter @Setter
    private static class CachedContent {
        private final Document document;
        private final String mainTitle;
        private final String language;
        private final List<Section> sections;
        private final long timestamp;

        public CachedContent(Document document, String mainTitle, String language, List<Section> sections) {
            this.document = document;
            this.mainTitle = mainTitle;
            this.language = language;
            this.sections = sections;
            this.timestamp = System.currentTimeMillis();
        }

        /**
         * Checks if the cached content is expired (older than one hour).
         *
         * @return true if the content is expired, false otherwise.
         */
        public boolean isExpired() {
            final long ONE_HOUR = 60 * 60 * 1000;
            return (System.currentTimeMillis() - timestamp) > ONE_HOUR;
        }
    }

    /**
     * Main method for running the WikipediaScraper.
     * This provides a simple interface to interact with the scraper via console input.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean stop = false;
        while (!stop) {
            System.out.println("Welcome to Wikipedia Scraper:");
            try {
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
            System.out.println("Do you want to continue: (y/n)");
            String response = scanner.nextLine();
            if (response.equals("n")) {
                stop = true;
            }
        }
    }
}

/**
 * Usage Manual:
 *
 * This manual provides a step-by-step guide to using the WikipediaScraper class effectively.
 *
 * 1. **Initialization**:
 *    - Create an instance of the WikipediaScraper class.
 *      Example: `WikipediaScraper scraper = new WikipediaScraper();`
 *
 * 2. **Connecting to a Wikipedia Page**:
 *    - Use the `connectToWikipedia(String url)` method to connect to a Wikipedia page.
 *      Example: `scraper.connectToWikipedia("https://en.wikipedia.org/wiki/OpenAI");`
 *
 * 3. **Using the New Constructor**:
 *    - Use the new constructor that takes a topic and language to generate the URL automatically.
 *      Example: `WikipediaScraper scraper = new WikipediaScraper("OpenAI", "en");`
 *
 * 4. **Caching**:
 *    - The scraper uses a cache to store previously accessed Wikipedia pages to improve efficiency.
 *    - To make use of the cache, ensure that you keep the WikipediaScraper instance running and reuse it for multiple requests.
 *    - If the same URL is accessed within an hour, the cached content will be used instead of making a new network request.
 *
 * 5. **Extracting Main Title and Language**:
 *    - Access the `mainTitle` and `language` properties to get the main title and language of the Wikipedia page.
 *      Example: `String title = scraper.getMainTitle();`
 *
 * 6. **Extracting Sections**:
 *    - The `sections` property contains a list of Section objects representing different sections of the Wikipedia page.
 *    - Each Section object has a title and a list of paragraphs.
 *      Example:
 *      ```
 *      for (Section section : scraper.getSections()) {
 *          System.out.println(section.getTitle());
 *          for (String paragraph : section.getParagraphs()) {
 *              System.out.println(paragraph);
 *          }
 *      }
 *      ```
 *
 * 7. **Extracting Text by Tag**:
 *    - Use the `extractTextByTag(String tag)` method to extract text based on HTML tags.
 *      Example: `List<String> paragraphs = scraper.extractTextByTag("p");`
 *
 * 8. **Extracting Text by Class**:
 *    - Use the `extractTextByClass(String className)` method to extract text based on HTML classes.
 *      Example: `List<String> elementsByClass = scraper.extractTextByClass("mw-headline");`
 *
 * 9. **Full Text Representation**:
 *    - Use the `toStringBuilder()` method to get a StringBuilder representation of the scraped content.
 *      Example: `StringBuilder content = scraper.toStringBuilder();`
 *
 * 10. **Error Handling**:
 *    - The scraper catches IOExceptions and prints error messages to the console.
 *    - Ensure you handle any potential exceptions in your implementation.
 *
 * By following these steps, you can effectively use the WikipediaScraper class to scrape and extract content from Wikipedia pages.
 */
