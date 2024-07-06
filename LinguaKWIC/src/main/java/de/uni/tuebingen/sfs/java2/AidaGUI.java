package de.uni.tuebingen.sfs.java2;//package de.uni.tuebingen.sfs.java2;
//
//import javax.swing.*;
//import java.awt.*;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class AidaGUI {
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(AidaGUI::createAndShowGUI);
//    }
//
//    private static void createAndShowGUI() {
//        JFrame frame = new JFrame("AidaGUI");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1200, 1000);
//        frame.setLayout(new BorderLayout());
//
//        // Style settings
//        Font font = new Font("Arial", Font.PLAIN, 14);
//        Color backgroundColor = new Color(230, 230, 250);
//        Color buttonColor = new Color(153, 153, 189); // Light purple
//        Color panelColor = new Color(230, 230, 250); // Lavender
//
//        // Top Panel for File Path, URL/File option, and Search Bar
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        topPanel.setBackground(backgroundColor);
//
//        JLabel targetLabel = new JLabel();
//        targetLabel.setFont(font);
//        JTextField targetField = new JTextField(50);
//        targetField.setFont(font);
//
//        String[] urlFileOptions = {"URL  ", "File  "};
//        JComboBox<String> urlFileComboBox = new JComboBox<>(urlFileOptions);
//        urlFileComboBox.setFont(font);
//
//        topPanel.add(targetLabel);
//        topPanel.add(targetField);
//        topPanel.add(urlFileComboBox);
//
//        frame.add(topPanel, BorderLayout.NORTH);
//
//        // Center Panel for Search Options and Results
//        JPanel centerPanel = new JPanel(new BorderLayout());
//        centerPanel.setBackground(backgroundColor);
//
//        // Options Panel
//        JPanel optionsPanel = new JPanel();
//        optionsPanel.setBackground(panelColor);
//        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
//        optionsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
//
//        JCheckBox tokenCheckBox = new JCheckBox("Exact word");
//        JCheckBox lemmaCheckBox = new JCheckBox("Lemma");
//        JCheckBox posTagCheckBox = new JCheckBox("POS Tag");
//
//        JTextField tokenField = new JTextField(20);
//        JTextField lemmaField = new JTextField(20);
//        JTextField posTagField = new JTextField(20);
//
//        tokenField.setEnabled(false);
//        lemmaField.setEnabled(false);
//        posTagField.setEnabled(false);
//
//        tokenCheckBox.addActionListener(e -> tokenField.setEnabled(tokenCheckBox.isSelected()));
//        lemmaCheckBox.addActionListener(e -> lemmaField.setEnabled(lemmaCheckBox.isSelected()));
//        posTagCheckBox.addActionListener(e -> posTagField.setEnabled(posTagCheckBox.isSelected()));
//
//        JPanel tokenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        tokenPanel.add(tokenCheckBox);
//        tokenPanel.add(tokenField);
//        tokenPanel.setBackground(panelColor);
//
//        JPanel lemmaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        lemmaPanel.add(lemmaCheckBox);
//        lemmaPanel.add(lemmaField);
//        lemmaPanel.setBackground(panelColor);
//
//        JPanel posTagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        posTagPanel.add(posTagCheckBox);
//        posTagPanel.add(posTagField);
//        posTagPanel.setBackground(panelColor);
//
//        optionsPanel.add(tokenPanel);
//        optionsPanel.add(lemmaPanel);
//        optionsPanel.add(posTagPanel);
//
//        JCheckBox sentenceCheckBox = new JCheckBox("Sentence");
//        JCheckBox neighborCheckBox = new JCheckBox("Neighbor");
//        JCheckBox caseCheckBox = new JCheckBox("Case");
//
//        JTextField neighborXField = new JTextField(5);
//        JTextField neighborYField = new JTextField(5);
//
//        neighborXField.setEnabled(false);
//        neighborYField.setEnabled(false);
//
//        neighborCheckBox.addActionListener(e -> {
//            neighborXField.setEnabled(neighborCheckBox.isSelected());
//            neighborYField.setEnabled(neighborCheckBox.isSelected());
//        });
//
//        JPanel neighborPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        neighborPanel.add(neighborCheckBox);
//        neighborPanel.add(new JLabel("X:"));
//        neighborPanel.add(neighborXField);
//        neighborPanel.add(new JLabel("Y:"));
//        neighborPanel.add(neighborYField);
//        neighborPanel.setBackground(panelColor);
//
//        optionsPanel.add(sentenceCheckBox);
//        optionsPanel.add(neighborPanel);
//        optionsPanel.add(caseCheckBox);
//
//        centerPanel.add(optionsPanel, BorderLayout.WEST);
//
//        // Results Panel
//        JPanel resultsPanel = new JPanel(new BorderLayout());
//        resultsPanel.setBackground(backgroundColor);
//        JTextArea searchResultsArea = new JTextArea();
//        searchResultsArea.setEditable(false);
//        searchResultsArea.setFont(font);
//        JScrollPane scrollPane = new JScrollPane(searchResultsArea);
//        resultsPanel.add(scrollPane, BorderLayout.CENTER);
//
//        centerPanel.add(resultsPanel, BorderLayout.CENTER);
//        frame.add(centerPanel, BorderLayout.CENTER);
//
//        // Right Panel for Recent Searches and Save Button
//        JPanel rightPanel = new JPanel();
//        rightPanel.setBackground(panelColor);
//        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
//        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JButton searchButton = new JButton("Search");
//        searchButton.setBackground(buttonColor);
//        searchButton.setForeground(Color.WHITE);
//        searchButton.setFont(font);
//
//        JButton advanceButton = new JButton("Advance");
//        advanceButton.setBackground(buttonColor);
//        advanceButton.setForeground(Color.WHITE);
//        advanceButton.setFont(font);
//
//        JTextArea recentSearchesArea = new JTextArea(5, 15);
//        recentSearchesArea.setEditable(false);
//        recentSearchesArea.setFont(font);
//        JScrollPane recentScrollPane = new JScrollPane(recentSearchesArea);
//
//        JButton saveButton = new JButton("Save to XML");
//        saveButton.setBackground(buttonColor);
//        saveButton.setForeground(Color.WHITE);
//        saveButton.setFont(font);
//
//        rightPanel.add(searchButton);
//        rightPanel.add(Box.createVerticalStrut(10));
//        rightPanel.add(advanceButton);
//        rightPanel.add(Box.createVerticalStrut(10));
//        rightPanel.add(new JLabel("Recent"));
//        rightPanel.add(recentScrollPane);
//        rightPanel.add(Box.createVerticalStrut(10));
//        rightPanel.add(saveButton);
//
//        frame.add(rightPanel, BorderLayout.EAST);
//
//        // Action for Advanced Button
//        advanceButton.addActionListener(e -> {
//            JDialog advancedDialog = new JDialog(frame, "Advanced Search", true);
//            advancedDialog.setSize(400, 300);
//            advancedDialog.setLayout(new BorderLayout());
//            advancedDialog.setBackground(panelColor);
//
//            JLabel infoLabel = new JLabel("<html>In Advanced search, you will provide us a topic in the language that you want and we will search in Wikipedia for your topic :)</html>");
//            infoLabel.setFont(font);
//            advancedDialog.add(infoLabel, BorderLayout.NORTH);
//
//            JPanel advancedPanel = new JPanel(new GridLayout(2, 2));
//            advancedPanel.setBackground(panelColor);
//
//            advancedPanel.add(new JLabel("Topic:"));
//            JTextField topicField = new JTextField();
//            advancedPanel.add(topicField);
//            advancedPanel.add(new JLabel("Language:"));
//
//            String[] languages = {"English", "German"};
//            JComboBox<String> languageComboBox = new JComboBox<>(languages);
//            advancedPanel.add(languageComboBox);
//
//            advancedDialog.add(advancedPanel, BorderLayout.CENTER);
//            JButton okButton = new JButton("OK");
//            okButton.setBackground(buttonColor);
//            okButton.setForeground(Color.WHITE);
//            okButton.setFont(font);
//            okButton.addActionListener(event -> advancedDialog.dispose());
//            advancedDialog.add(okButton, BorderLayout.SOUTH);
//
//            advancedDialog.setVisible(true);
//        });
//
//        frame.setVisible(true);
//    }
//}
//

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AidaGUI {
    static JCheckBox exactWordCheckBox;
    static JCheckBox wordLemmaCheckBox;
    static JCheckBox wordPOSTagCheckBox;
    static JCheckBox caseSensitiveCheckBox;
    static JTextField exactWordField;
    static JTextField wordLemmaField;
    static JTextField wordPOSTagField;
    static File file;
    static JTextField targetField;
    static JComboBox<String> urlFileComboBox;
    static JRadioButton wholeSentenceRadioButton;
    static JRadioButton neighborRadioButton;
    static JTextArea searchResultsArea;
    static List<TextSearch.Pair> NLPResults;
    static JSpinner neighborXSpinner;
    static JSpinner neighborYSpinner;
    static LinguaKWIC linguaKWIC = null;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AidaGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("AidaGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        // Style settings
        Font font = new Font("Arial", Font.PLAIN, 14);
        Color backgroundColor = new Color(245, 245, 245);
        Color buttonColor = new Color(153, 153, 189); // Light purple
        Color panelColor = new Color(230, 230, 250); // Lavender

        // Top Panel for Target, URL/File option
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(panelColor);

        JPanel targetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        targetPanel.setBackground(panelColor);

        JLabel targetLabel = new JLabel("Target:");
        targetLabel.setFont(font);
        targetField = new JTextField(30);
        targetField.setFont(font);

        String[] urlFileOptions = {"URL", "File"};
        urlFileComboBox = new JComboBox<>(urlFileOptions);
        urlFileComboBox.setFont(font);

        targetPanel.add(targetLabel);
        targetPanel.add(targetField);
        targetPanel.add(urlFileComboBox);

        // Second line for checkboxes and fields
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.setBackground(panelColor);

        exactWordCheckBox = new JCheckBox("Exact word");
        wordLemmaCheckBox = new JCheckBox("Word lemma");
        wordPOSTagCheckBox = new JCheckBox("Word POS Tag");

        ButtonGroup gt = new ButtonGroup();
        exactWordCheckBox.setSelected(true);
        gt.add(exactWordCheckBox);
        gt.add(wordLemmaCheckBox);
        gt.add(wordPOSTagCheckBox);

        exactWordField = new JTextField(15);
        wordLemmaField = new JTextField(15);
        wordPOSTagField = new JTextField(15);

        exactWordField.setFont(font);
        wordLemmaField.setFont(font);
        wordPOSTagField.setFont(font);

        exactWordField.setEnabled(false);
        wordLemmaField.setEnabled(false);
        wordPOSTagField.setEnabled(false);

        exactWordCheckBox.addActionListener(e -> exactWordField.setEnabled(exactWordCheckBox.isSelected()));
        wordLemmaCheckBox.addActionListener(e -> wordLemmaField.setEnabled(wordLemmaCheckBox.isSelected()));
        wordPOSTagCheckBox.addActionListener(e -> wordPOSTagField.setEnabled(wordPOSTagCheckBox.isSelected()));

        optionsPanel.add(exactWordCheckBox);
        optionsPanel.add(exactWordField);
        optionsPanel.add(wordLemmaCheckBox);
        optionsPanel.add(wordLemmaField);
        optionsPanel.add(wordPOSTagCheckBox);
        optionsPanel.add(wordPOSTagField);

        // Third line for radio buttons and spinners
        JPanel sentencePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sentencePanel.setBackground(panelColor);

        wholeSentenceRadioButton = new JRadioButton("Whole Sentence");
        neighborRadioButton = new JRadioButton("Neighbor");
        ButtonGroup wn = new ButtonGroup();
        wholeSentenceRadioButton.setSelected(true);
        wn.add(wholeSentenceRadioButton);
        wn.add(neighborRadioButton);

        neighborXSpinner = new JSpinner(new SpinnerNumberModel(0, -5, 0, 1));
        neighborYSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));

        neighborXSpinner.setEnabled(false);
        neighborYSpinner.setEnabled(false);

        neighborRadioButton.addActionListener(e -> {
            boolean selected = neighborRadioButton.isSelected();
            neighborXSpinner.setEnabled(selected);
            neighborYSpinner.setEnabled(selected);
        });

        wholeSentenceRadioButton.addActionListener(e -> {
            if (wholeSentenceRadioButton.isSelected()) {
                neighborYSpinner.setEnabled(false);
                neighborXSpinner.setEnabled(false);
            }
        });

        caseSensitiveCheckBox = new JCheckBox("Case Sensitive");

        sentencePanel.add(wholeSentenceRadioButton);
        sentencePanel.add(neighborRadioButton);
        sentencePanel.add(neighborXSpinner);
        sentencePanel.add(neighborYSpinner);
        sentencePanel.add(caseSensitiveCheckBox);

        topPanel.add(targetPanel);
        topPanel.add(optionsPanel);
        topPanel.add(sentencePanel);

        frame.add(topPanel, BorderLayout.NORTH);

        // Center Panel for Search Options and Results
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);

        // Results Panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(backgroundColor);
        searchResultsArea = new JTextArea();
        searchResultsArea.setEditable(false);
        searchResultsArea.setFont(font);
        JScrollPane scrollPane = new JScrollPane(searchResultsArea);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(resultsPanel, BorderLayout.CENTER);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Right Panel for Recent Searches and Save Button
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(panelColor);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Dimension buttonSize = new Dimension(120, 30);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(buttonColor);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(font);
        searchButton.setMaximumSize(buttonSize);
        searchButton.addActionListener(new searchButtonHandler());

        JButton advanceButton = new JButton("Advance");
        advanceButton.setBackground(buttonColor);
        advanceButton.setForeground(Color.WHITE);
        advanceButton.setFont(font);
        advanceButton.setMaximumSize(buttonSize);

        JButton BrowseButton = new JButton("Browse");
        BrowseButton.setBackground(buttonColor);
        BrowseButton.setForeground(Color.WHITE);
        BrowseButton.setFont(font);
        BrowseButton.setMaximumSize(buttonSize);
        BrowseButton.addActionListener(new browseButtonHandler());

        JTextArea recentSearchesArea = new JTextArea(5, 15);
        recentSearchesArea.setEditable(false);
        recentSearchesArea.setFont(font);
        JScrollPane recentScrollPane = new JScrollPane(recentSearchesArea);

        JButton saveButton = new JButton("Save to XML");
        saveButton.setBackground(buttonColor);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(font);
        saveButton.setMaximumSize(buttonSize);
        saveButton.addActionListener(new saveButtonHandler());

        rightPanel.add(BrowseButton);
        rightPanel.add(Box.createVerticalStrut(150));
        rightPanel.add(searchButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(advanceButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(new JLabel("Recent"));
        rightPanel.add(recentScrollPane);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(saveButton);

        frame.add(rightPanel, BorderLayout.EAST);

        // Action for Advanced Button
        advanceButton.addActionListener(e -> {
            JDialog advancedDialog = new JDialog(frame, "Advanced Search", true);
            advancedDialog.setSize(400, 300);
            advancedDialog.setLayout(new BorderLayout());
            advancedDialog.setBackground(panelColor);

            JLabel infoLabel = new JLabel("<html>In Advanced search, you will provide us a topic in the language that you want and we will search in Wikipedia for your topic :)</html>");
            infoLabel.setFont(font);
            advancedDialog.add(infoLabel, BorderLayout.NORTH);

            JPanel advancedPanel = new JPanel(new GridLayout(2, 2));
            advancedPanel.setBackground(panelColor);

            advancedPanel.add(new JLabel("Topic:"));
            JTextField topicField = new JTextField();
            advancedPanel.add(topicField);
            advancedPanel.add(new JLabel("Language:"));

            String[] languages = {"English", "German"};
            JComboBox<String> languageComboBox = new JComboBox<>(languages);
            advancedPanel.add(languageComboBox);

            advancedDialog.add(advancedPanel, BorderLayout.CENTER);
            JButton okButton = new JButton("OK");
            okButton.setBackground(buttonColor);
            okButton.setForeground(Color.WHITE);
            okButton.setFont(font);
            okButton.addActionListener(event -> advancedDialog.dispose());
            advancedDialog.add(okButton, BorderLayout.SOUTH);

            advancedDialog.setVisible(true);
        });

        frame.setVisible(true);
    }


    private static class saveButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            List<UploadedFile> uploadedFiles = new ArrayList<>();
            List<ProcessedFile> processedFiles = new ArrayList<>();
            List<WikipediaArticle> articles = new ArrayList<>();

            if (file.exists()) {
                uploadedFiles.add(new UploadedFile(file.getName(), linguaKWIC.getText()));
                processedFiles.add(new ProcessedFile(file.getName(), linguaKWIC.getTokens().stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList()), linguaKWIC.getLemmas().stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList()), linguaKWIC.getPosTags().stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList())));
            }
            if (isValidURL(targetField.getText())) {
                articles.add(new WikipediaArticle(targetField.getText(), linguaKWIC.getText()));
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
            fileChooser.setFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                XMLWriter xmlWriter = new XMLWriter();
                xmlWriter.writeXML(fileToSave.getAbsolutePath(), uploadedFiles, processedFiles, articles);
            }
        }
    }

    private static class searchButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String filePathOrLink = targetField.getText();
            if (isValidURL(filePathOrLink)) {
                try {
                    linguaKWIC = new LinguaKWIC(filePathOrLink);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error loading from URL", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (filePathOrLink != null) {
                try {
                    linguaKWIC = new LinguaKWIC(file);
                } catch (Exception t) {
                    t.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error loading from file", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid file or URL", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<List<String>> tokens = linguaKWIC.getTokens();
            List<List<String>> lemmas = linguaKWIC.getLemmas();
            List<List<String>> posTags = linguaKWIC.getPosTags();

            List<TextSearch.Pair> NLPResults = null;
            String searchText = searchResultsArea.getText();

            if (wordLemmaCheckBox.isSelected() && caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.textSearch.searchByLemm(searchText);
            } else if (wordPOSTagCheckBox.isSelected() && caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.textSearch.searchByTag(searchText);
            } else if (exactWordCheckBox.isSelected() && caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.textSearch.searchByToken(searchText);
            } else if (wordLemmaCheckBox.isSelected() && !caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.textSearch.searchByLemm(searchText.toLowerCase());
            } else if (wordPOSTagCheckBox.isSelected() && !caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.textSearch.searchByTag(searchText.toLowerCase());
            } else if (exactWordCheckBox.isSelected() && !caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.textSearch.searchByToken(searchText.toLowerCase());
            }

            // Display search results in the GUI
            displaySearchResults(NLPResults, tokens, lemmas, posTags);
        }


        private void displaySearchResults(List<TextSearch.Pair> results, List<List<String>> tokens,
                                          List<List<String>> lemmas, List<List<String>> posTags) {
            searchResultsArea.setText("");
            if (results == null || results.isEmpty()) {
                searchResultsArea.append("No results found.");
                return;
            }

            for (TextSearch.Pair result : results) {
                int sentenceIndex = result.getSentenceIndex();
                int tokenIndex = result.getTokenIndex();

                if (sentenceIndex < tokens.size() && tokenIndex < tokens.get(sentenceIndex).size()) {
                    String token = tokens.get(sentenceIndex).get(tokenIndex);
                    String lemma = lemmas.get(sentenceIndex).get(tokenIndex);
                    String posTag = posTags.get(sentenceIndex).get(tokenIndex);

                    // Highlighting logic - Example: Wrap in HTML tags to highlight
                    String highlightedToken = "<span style='background-color: yellow;'>" + token + "</span>";
                    String highlightedLemma = "<span style='background-color: yellow;'>" + lemma + "</span>";
                    String highlightedPosTag = "<span style='background-color: yellow;'>" + posTag + "</span>";

                    // Append to the searchResultsArea with appropriate formatting
                    searchResultsArea.append("Sentence " + sentenceIndex + ": ");

                    // Append each token with its highlighting
                    searchResultsArea.append(highlightedToken + " ");

                    // Append new line for clarity
                    searchResultsArea.append("\n");
                }
            }
        }
    }

    private static class browseButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a new JFileChooser
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            fileChooser.setFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);
            // Show the open dialog and get the user's response
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                targetField.setText(file.getName());
                urlFileComboBox.setSelectedItem("File");
                urlFileComboBox.setEnabled(false);
            }
        }
    }

    public static boolean isValidURL(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}

