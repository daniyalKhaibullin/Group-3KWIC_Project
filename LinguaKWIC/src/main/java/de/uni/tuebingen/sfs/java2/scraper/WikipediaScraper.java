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
 * Version: V1.1
 */

package de.uni.tuebingen.sfs.java2.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WikipediaScraper {
    // Instances of the class: where the document, url, title, language, and sections are stored
    private Document document;
    private String url;
    private String mainTitle;
    private String language;
    private List<Section> sections;

    public WikipediaScraper() {
        sections = new ArrayList<>();
    }

    public WikipediaScraper(String url) throws IOException {
        this();
        connectToWikipedia(url);
    }

    /**
     * Setters and Getters
     */

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    /**
     * Connects to the specified Wikipedia URL and initiates scraping.
     *
     * @param url The URL of the Wikipedia page to scrape.
     * @throws IOException If an I/O error occurs while connecting to the URL.
     */
    public void connectToWikipedia(String url) throws IOException {
        this.url = url;
        this.document = Jsoup.connect(url).get();
        scrapeWikipedia();
    }

    /**
     * Scrapes the Wikipedia page for the main title, language, and sections.
     */
    private void scrapeWikipedia() {
        if (document != null) {
            this.mainTitle = document.title();
            this.language = document.selectFirst("html").attr("lang");

            Element bodyContent = document.selectFirst("#bodyContent");
            Element topContent = bodyContent.selectFirst("#mw-content-text");

            Section topSection = new Section("Introduction");
            if (topContent != null) {
                topSection.addParagraph(topContent.text());
            }
            sections.add(topSection);

            Elements sectionElements = bodyContent.select("h2, h3, p");

            Section currentSection = new Section("Introduction");
            for (Element element : sectionElements) {
                if (element.tagName().equals("h2") || element.tagName().equals("h3")) {
                    if (!currentSection.getTitle().equals("Introduction")) {
                        sections.add(currentSection);
                    }
                    currentSection = new Section(element.text());
                } else {
                    currentSection.addParagraph(element.text());
                }
            }
            sections.add(currentSection);
        }
    }

    /**
     * Extracts text from the document based on the specified tag.
     *
     * @param tag The HTML tag to extract text from.
     * @return A list of text extracted from the specified tag.
     */
    public List<String> extractTextByTag(String tag) {
        List<String> textList = new ArrayList<>();
        Elements elements = document.select(tag);
        for (Element element : elements) {
            textList.add(element.text());
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
        Elements elements = document.getElementsByClass(className);
        for (Element element : elements) {
            textList.add(element.text());
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
    public static class Section {
        private String title;
        private List<String> paragraphs;

        public Section(String title) {
            this.title = title;
            this.paragraphs = new ArrayList<>();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getParagraphs() {
            return paragraphs;
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
