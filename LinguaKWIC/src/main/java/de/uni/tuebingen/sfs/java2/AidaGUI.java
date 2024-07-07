package de.uni.tuebingen.sfs.java2;

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
    final static  Dimension buttonSize = new Dimension(150, 30);


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AidaGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("AidaGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);
        frame.setLayout(new BorderLayout());
        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

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

        JButton BrowseButton = new JButton("Browse File");
        BrowseButton.setBackground(buttonColor);
        BrowseButton.setForeground(Color.WHITE);
        BrowseButton.setFont(font);
        BrowseButton.setMaximumSize(buttonSize);
        BrowseButton.addActionListener(new browseButtonHandler());

        targetPanel.add(targetLabel);
        targetPanel.add(targetField);
        targetPanel.add(BrowseButton);


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



        JButton searchButton = new JButton("Search");
        searchButton.setBackground(buttonColor);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(font);
        searchButton.setMaximumSize(buttonSize);
        searchButton.addActionListener(new searchButtonHandler());

        JButton advanceButton = new JButton("Advance Search");
        advanceButton.setBackground(buttonColor);
        advanceButton.setForeground(Color.WHITE);
        advanceButton.setFont(font);
        advanceButton.setMaximumSize(buttonSize);


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
            advancedDialog.setSize(500, 300);
            advancedDialog.setLayout(new BorderLayout());
            advancedDialog.setBackground(panelColor);

            // Center the dialog on the screen
            advancedDialog.setLocationRelativeTo(frame);

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(panelColor);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel infoLabel = new JLabel("<html><p style='text-align:center;'>In Advanced search, you will provide us a topic in the language that you want and we will search in Wikipedia for your topic :)</p></html>");
            infoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(infoLabel);
            contentPanel.add(Box.createVerticalStrut(20));

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(2, 2, 10, 10));
            formPanel.setBackground(panelColor);

            JLabel topicLabel = new JLabel("Topic:");
            topicLabel.setFont(new Font("Arial", Font.BOLD, 16));
            formPanel.add(topicLabel);

            JTextField topicField = new JTextField();
            topicField.setFont(font);
            topicField.setBackground(Color.WHITE);
            formPanel.add(topicField);

            JLabel languageLabel = new JLabel("Language:");
            languageLabel.setFont(new Font("Arial", Font.BOLD, 16));
            formPanel.add(languageLabel);

            String[] languages = {"English", "German"};
            JComboBox<String> languageComboBox = new JComboBox<>(languages);
            languageComboBox.setFont(font);
            languageComboBox.setBackground(Color.WHITE);
            formPanel.add(languageComboBox);

            contentPanel.add(formPanel);
            contentPanel.add(Box.createVerticalStrut(20));

            JButton okButton = new JButton("OK");
            okButton.setBackground(buttonColor);
            okButton.setForeground(Color.WHITE);
            okButton.setFont(font);
            okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            okButton.addActionListener(event -> advancedDialog.dispose());
            contentPanel.add(okButton);

            advancedDialog.add(contentPanel, BorderLayout.CENTER);
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
                NLPResults = linguaKWIC.getTextSearch().searchByLemm(searchText);
            } else if (wordPOSTagCheckBox.isSelected() && caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.getTextSearch().searchByTag(searchText);
            } else if (exactWordCheckBox.isSelected() && caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.getTextSearch().searchByToken(searchText);
            } else if (wordLemmaCheckBox.isSelected() && !caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.getTextSearch().searchByLemm(searchText.toLowerCase());
            } else if (wordPOSTagCheckBox.isSelected() && !caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.getTextSearch().searchByTag(searchText.toLowerCase());
            } else if (exactWordCheckBox.isSelected() && !caseSensitiveCheckBox.isSelected()) {
                NLPResults = linguaKWIC.getTextSearch().searchByToken(searchText.toLowerCase());
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

