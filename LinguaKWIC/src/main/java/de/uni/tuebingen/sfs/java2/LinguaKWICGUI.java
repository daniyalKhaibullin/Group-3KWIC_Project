package de.uni.tuebingen.sfs.java2;

import lombok.Setter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LinguaKWICGUI extends JFrame {

    // Define colors and fonts
    private static final Color PRIMARY_COLOR = new Color(153, 153, 189);
    private static final Color SECONDARY_COLOR = new Color(236, 242, 156);
    private static final Color TEXT_FIELD_BACKGROUND = new Color(236, 242, 156);
    private static final Color TEXT_AREA_BACKGROUND = new Color(200, 180, 220);
    private static final Color TEXT_AREA_BG_SECOND = new Color(170, 140, 185);
    private static final Color BUTTON_BACKGROUND = new Color(234, 221, 231);
    private static final Font FONT = new Font("Phosphate", Font.PLAIN, 15);
    private static final Font INPUT_FONT = new Font("Papyrus", Font.BOLD, 15);

    // Components
    static RoundedTextField searchField;
    static JButton browseFileButton;
    static JButton advanceSearchButton;

    static JCheckBox exactWordCheckBox;
    static JCheckBox wordLemmaCheckBox;
    static JCheckBox wordPOSTagCheckBox;
    static RoundedTextField exactWordField;
    static RoundedTextField wordLemmaField;
    static RoundedTextField wordPOSTagField;
    static JRadioButton wholeSentenceRadioButton;
    static JRadioButton neighborRadioButton;
    static JSpinner neighborRightSpinner;
    static JSpinner neighborLeftSpinner;
    static JCheckBox caseSensitiveCheckBox;
    static JButton searchButton;

    static JTextArea recentTextArea;
    static JButton saveButton;
    static JTextArea resultTextArea;

    static File selectedFile;
    static LinguaKWIC linguaKWIC;


    public LinguaKWICGUI() {
        initializeComponents();
        setStyles();
        createLayout();
    }

    private void initializeComponents() {
        // Initialize components
        // Top components
        searchField = new RoundedTextField(30);
        advanceSearchButton = new JButton("SEARCH WITH TOPIC");
        advanceSearchButton.addActionListener(new AdvanceSearchButtonListener());
        browseFileButton = new JButton("BROWSE FILE");
        browseFileButton.addActionListener(new BrowseButtonHandler());

        // West components
        exactWordCheckBox = new JCheckBox("EXACT WORD");
        wordLemmaCheckBox = new JCheckBox("WORD LEMMA");
        wordPOSTagCheckBox = new JCheckBox("WORD POS-TAG");
        exactWordField = new RoundedTextField(15);
        wordLemmaField = new RoundedTextField(15);
        wordPOSTagField = new RoundedTextField(15);

        wholeSentenceRadioButton = new JRadioButton("WHOLE SENTENCE");
        neighborRadioButton = new JRadioButton("NEIGHBOR");
        neighborRightSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        neighborLeftSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(wholeSentenceRadioButton);
        radioGroup.add(neighborRadioButton);

        caseSensitiveCheckBox = new JCheckBox("CASE SENSITIVE");

        searchButton = new JButton("SEARCH");
        searchButton.addActionListener(new SearchButtonHandler());

        recentTextArea = new JTextArea();
        JScrollPane recentScrollPane = new JScrollPane(recentTextArea);

        // South components
        saveButton = new JButton("SAVE TO XML");
        saveButton.addActionListener(new SaveButtonHandler());

        // Center component
        resultTextArea = new JTextArea();
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);

        // Add action listeners to radio buttons
        neighborRadioButton.addActionListener(e -> {
            boolean enabled = neighborRadioButton.isSelected();
            neighborRightSpinner.setEnabled(enabled);
            neighborLeftSpinner.setEnabled(enabled);
        });

        wholeSentenceRadioButton.addActionListener(e -> {
            boolean enabled = wholeSentenceRadioButton.isSelected();
            neighborRightSpinner.setEnabled(!enabled); // Disable neighbor fields
            neighborLeftSpinner.setEnabled(!enabled); // Disable neighbor fields
        });

        neighborRightSpinner.setEnabled(false);
        neighborLeftSpinner.setEnabled(false);
        exactWordField.setEnabled(false);
        wordLemmaField.setEnabled(false);
        wordPOSTagField.setEnabled(false);
        recentTextArea.setEditable(false);

        resultTextArea.setEditable(false);

    }

    private void setStyles() {
        // Set fonts and colors
        // Top
        advanceSearchButton.setBackground(BUTTON_BACKGROUND);
        searchField.setFont(INPUT_FONT);
        searchField.setBackground(TEXT_FIELD_BACKGROUND);
        browseFileButton.setBackground(BUTTON_BACKGROUND);

        // West
        exactWordCheckBox.setBackground(SECONDARY_COLOR);
        wordLemmaCheckBox.setBackground(SECONDARY_COLOR);
        wordPOSTagCheckBox.setBackground(SECONDARY_COLOR);

        exactWordField.setFont(INPUT_FONT);
        exactWordField.setBackground(new Color(191, 155, 166));
        wordLemmaField.setFont(INPUT_FONT);
        wordLemmaField.setBackground(new Color(186, 159, 178));
        wordPOSTagField.setFont(INPUT_FONT);
        wordPOSTagField.setBackground(new Color(192, 174, 182));

        caseSensitiveCheckBox.setBackground(SECONDARY_COLOR);
        wholeSentenceRadioButton.setBackground(SECONDARY_COLOR);
        neighborRadioButton.setBackground(SECONDARY_COLOR);

        neighborRightSpinner.setFont(FONT);
        neighborRightSpinner.setBackground(PRIMARY_COLOR);
        neighborLeftSpinner.setFont(FONT);
        neighborLeftSpinner.setBackground(PRIMARY_COLOR);

        searchButton.setBackground(BUTTON_BACKGROUND);

        recentTextArea.setFont(INPUT_FONT);
        recentTextArea.setBackground(TEXT_AREA_BG_SECOND);
        recentTextArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // South
        saveButton.setBackground(BUTTON_BACKGROUND);

        // Center
        resultTextArea.setFont(INPUT_FONT);
        resultTextArea.setBackground(TEXT_AREA_BACKGROUND);
        resultTextArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    }

    private void createLayout() {
        setTitle("LinguaKWIC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLocationRelativeTo(null);

        // Main panel using BorderLayout
        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        // Top Panel (North)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Search in:"));
        topPanel.add(searchField);
        topPanel.add(browseFileButton);
        topPanel.add(advanceSearchButton);

        rootPanel.add(topPanel, BorderLayout.NORTH);

        // Left Panel (Options and Recent Text)
        JPanel leftPanel = new JPanel(new BorderLayout());

        // Recent Text Panel
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBackground(PRIMARY_COLOR);
        recentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        recentPanel.add(new JLabel("Recent:"), BorderLayout.NORTH);
        JScrollPane recentScrollPane = new JScrollPane(recentTextArea);
        recentScrollPane.setPreferredSize(new Dimension(200, 200)); // Increase size
        recentPanel.add(recentScrollPane, BorderLayout.CENTER);


        leftPanel.add(recentPanel, BorderLayout.CENTER);

        // Options Panel
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBackground(PRIMARY_COLOR);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Row 1: EXACT WORD checkbox and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        optionsPanel.add(exactWordCheckBox, gbc);

        gbc.gridx = 1;
        optionsPanel.add(exactWordField, gbc);

        // Row 2: WORD LEMMA checkbox and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        optionsPanel.add(wordLemmaCheckBox, gbc);

        gbc.gridx = 1;
        optionsPanel.add(wordLemmaField, gbc);

        // Row 3: WORD POS-TAG checkbox and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        optionsPanel.add(wordPOSTagCheckBox, gbc);

        gbc.gridx = 1;
        optionsPanel.add(wordPOSTagField, gbc);

        // Row 4: WHOLE SENTENCE and NEIGHBOR radio buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        optionsPanel.add(wholeSentenceRadioButton, gbc);

        gbc.gridx = 1;
        optionsPanel.add(neighborRadioButton, gbc);

        // Row 5: Neighbor fields
        gbc.gridx = 0;
        gbc.gridy = 4;
        optionsPanel.add(new JLabel("Right Neighbor:"), gbc);

        gbc.gridx = 1;
        optionsPanel.add(neighborRightSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        optionsPanel.add(new JLabel("Left Neighbor:"), gbc);

        gbc.gridx = 1;
        optionsPanel.add(neighborLeftSpinner, gbc);

        // Row 6: CASE SENSITIVE checkbox
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        optionsPanel.add(caseSensitiveCheckBox, gbc);

        // Row 7: SEARCH BUTTON
        gbc.gridy = 7;
        optionsPanel.add(searchButton, gbc);

        leftPanel.add(optionsPanel, BorderLayout.NORTH);

        // Add leftPanel to rootPanel on the WEST
        rootPanel.add(leftPanel, BorderLayout.WEST);

        // Center Panel (Text Area)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(PRIMARY_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setPreferredSize(new Dimension(800, 800)); // Adjust size
        centerPanel.add(resultScrollPane, BorderLayout.CENTER);

        rootPanel.add(centerPanel, BorderLayout.CENTER);

        // South Panel (Save Button)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(PRIMARY_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bottomPanel.add(saveButton);

        rootPanel.add(bottomPanel, BorderLayout.SOUTH);


        exactWordCheckBox.addActionListener(e -> exactWordField.setEnabled(exactWordCheckBox.isSelected()));
        wordLemmaCheckBox.addActionListener(e -> wordLemmaField.setEnabled(wordLemmaCheckBox.isSelected()));
        wordPOSTagCheckBox.addActionListener(e -> wordPOSTagField.setEnabled(wordPOSTagCheckBox.isSelected()));


        setVisible(true);
    }

    @Setter
    private static class RoundedTextField extends JTextField {
        private final int arcWidth = 15;
        private final int arcHeight = 15;

        public RoundedTextField(int columns) {
            super(columns);
            setOpaque(false); // Make the text field non-opaque to paint background
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (!isOpaque() && getBackground() != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
                g2.dispose();
            }
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            if (!isOpaque() && getBackground() != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getForeground());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
                g2.dispose();
            }
        }
    }

    private class BrowseButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a new JFileChooser

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            fileChooser.setFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);
            // Show the open dialog and get the user's response
            int response = fileChooser.showOpenDialog(LinguaKWICGUI.this);
            if (response == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                searchField.setText(selectedFile.getName());
            }

        }
    }

    private class AdvanceSearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a new JDialog
            JDialog advancedDialog = new JDialog(LinguaKWICGUI.this, "Advanced Search", true); // Use LinguaKWICGUI.this as the parent frame
            advancedDialog.setSize(500, 300);
            advancedDialog.setLayout(new BorderLayout());
            advancedDialog.setBackground(PRIMARY_COLOR);

            // Center the dialog on the screen
            advancedDialog.setLocationRelativeTo(LinguaKWICGUI.this);

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(PRIMARY_COLOR);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel infoLabel = new JLabel("<html><p style='text-align:center;'>In Advanced search, you will provide us a topic in the language that you want and we will search in Wikipedia for your topic :)</p></html>");
            infoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(infoLabel);
            contentPanel.add(Box.createVerticalStrut(20));

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(2, 2, 10, 10));
            formPanel.setBackground(PRIMARY_COLOR);

            JLabel topicLabel = new JLabel("Topic:");
            topicLabel.setFont(new Font("Arial", Font.BOLD, 16));
            formPanel.add(topicLabel);

            JTextField topicField = new JTextField();
            topicField.setBackground(Color.WHITE);
            topicField.setFont(INPUT_FONT);
            formPanel.add(topicField);

            JLabel languageLabel = new JLabel("Language:");
            languageLabel.setFont(new Font("Arial", Font.BOLD, 16));
            formPanel.add(languageLabel);

            String[] languages = {"English", "German"};
            JComboBox<String> languageComboBox = new JComboBox<>(languages);
            languageComboBox.setBackground(Color.WHITE);
            formPanel.add(languageComboBox);

            contentPanel.add(formPanel);
            contentPanel.add(Box.createVerticalStrut(20));

            JButton okButton = new JButton("OK");
            okButton.setBackground(BUTTON_BACKGROUND);
            okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            okButton.addActionListener(event -> {
                try {
                    String searchTopic = topicField.getText();
                    String lang = languageComboBox.getSelectedItem().toString();
                    if (lang.equals("English")) {
                        lang = "en";
                    } else {
                        lang = "de";
                    }
                    String url = "https://" + lang + ".wikipedia.org/wiki/" + searchTopic.replace(" ", "_");
                    searchField.setText(url);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error performing advanced search: couldn't find the topic", "Error", JOptionPane.ERROR_MESSAGE);
                }
                advancedDialog.dispose();
            });
            contentPanel.add(okButton);

            advancedDialog.add(contentPanel, BorderLayout.CENTER);
            advancedDialog.setVisible(true);
        }
    }

    private static class SaveButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                java.util.List<UploadedFile> uploadedFiles = new ArrayList<>();
                java.util.List<ProcessedFile> processedFiles = new ArrayList<>();
                java.util.List<WikipediaArticle> articles = new ArrayList<>();

                if (selectedFile.exists()) {
                    uploadedFiles.add(new UploadedFile(selectedFile.getName(), linguaKWIC.getText()));
                    processedFiles.add(new ProcessedFile(selectedFile.getName(), linguaKWIC.getTokens().stream()
                            .flatMap(java.util.List::stream)
                            .collect(Collectors.toList()), linguaKWIC.getLemmas().stream()
                            .flatMap(java.util.List::stream)
                            .collect(Collectors.toList()), linguaKWIC.getPosTags().stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList())));
                }
                if (isValidURL(searchField.getText())) {
                    articles.add(new WikipediaArticle(searchField.getText(), linguaKWIC.getText()));
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
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving file: Something went wrong ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class SearchButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String filePathOrLink = searchField.getText();
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
                        linguaKWIC = new LinguaKWIC(selectedFile);
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
                resultTextArea.setText("");
                // Determine the search criteria
                String token = exactWordField.getText();
                String lemma = wordLemmaField.getText();
                String posTag = wordPOSTagField.getText();
                boolean caseSensitive = caseSensitiveCheckBox.isSelected();

                // Perform the search based on selected checkboxes
                if (exactWordCheckBox.isSelected() && wordLemmaCheckBox.isSelected() && wordPOSTagCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTokenAndLemmAndTag(token, lemma, posTag, caseSensitive);
                    recentTextArea.insert(token + " " + lemma + " " + posTag + "(in " + searchField.getText() + ")" + "\n", 0);
                } else if (exactWordCheckBox.isSelected() && wordLemmaCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTokenAndLemm(token, lemma, caseSensitive);
                    recentTextArea.insert(token + " " + lemma + "(in " + searchField.getText() + ")" + "\n", 0);
                } else if (exactWordCheckBox.isSelected() && wordPOSTagCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTokenAndTag(token, posTag, caseSensitive);
                    recentTextArea.insert(token + " " + posTag + "(in " + searchField.getText() + ")" + "\n", 0);
                } else if (wordLemmaCheckBox.isSelected() && wordPOSTagCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTagAndLemm(posTag, lemma, caseSensitive);
                    recentTextArea.insert(lemma + " " + posTag + "(in " + searchField.getText() + ")" + "\n", 0);
                } else if (exactWordCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByToken(token, caseSensitive);
                    recentTextArea.insert(token + "(in " + searchField.getText() + ")" + "\n", 0);
                } else if (wordLemmaCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByLemm(lemma, caseSensitive);
                    recentTextArea.insert(lemma + "(in " + searchField.getText() + ")" + "\n", 0);
                } else if (wordPOSTagCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTag(posTag, caseSensitive);
                    recentTextArea.insert(posTag + "(in " + searchField.getText() + ")" + "\n", 0);
                }


                // Display search results in the GUI
                displaySearchResults(NLPResults, tokens, lemmas, posTags);
            } catch (Exception ex) {
//                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error loading data: this URL is not valid or doesnt have a content to analyse.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


        private void displaySearchResults(List<TextSearch.Pair> results, List<List<String>> tokens,
                                          List<List<String>> lemmas, List<List<String>> posTags) {
            if (results == null || results.isEmpty()) {
                resultTextArea.append("No results found.");
                return;
            }


            Highlighter highlighter = resultTextArea.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

            resultTextArea.append(results.size() + " match(es) found:\n");


            for (TextSearch.Pair result : results) {
                int sentenceIndex = result.getSentenceIndex();
                int tokenIndex = result.getTokenIndex();
                int start = 0;
                int end = tokens.get(sentenceIndex).size();
                int leftN;
                int rightN;

                if (neighborRadioButton.isSelected()) {
                    leftN = (int) neighborLeftSpinner.getValue();
                    rightN = (int) neighborRightSpinner.getValue();
                    start = Math.max(0, tokenIndex - rightN);
                    end = Math.min(tokens.get(sentenceIndex).size(), tokenIndex + leftN + 1);
                }


                if (sentenceIndex < tokens.size() && tokenIndex < tokens.get(sentenceIndex).size()) {
                    String token = tokens.get(sentenceIndex).get(tokenIndex);
                    String lemma = lemmas.get(sentenceIndex).get(tokenIndex);
                    String posTag = posTags.get(sentenceIndex).get(tokenIndex);

                    StringBuilder sentenceText = new StringBuilder("Sentence " + (sentenceIndex + 1) + ": ");
                    StringBuilder lemmaText = new StringBuilder("Lemmas: ");
                    StringBuilder posTagText = new StringBuilder("POS Tags: ");


                    for (int i = start; i < end; i++) {
                        sentenceText.append(tokens.get(sentenceIndex).get(i)).append(" ");
                        lemmaText.append(lemmas.get(sentenceIndex).get(i)).append(" ");
                        posTagText.append(posTags.get(sentenceIndex).get(i)).append(" ");
                    }
                    sentenceText.append("\n");
                    lemmaText.append("\n");
                    posTagText.append("\n\n");

                    // Append the texts
                    int sentenceStartPos = resultTextArea.getText().length();
                    resultTextArea.append(sentenceText.toString());

                    int lemmaStartPos = resultTextArea.getText().length();
                    resultTextArea.append(lemmaText.toString());

                    int posTagStartPos = resultTextArea.getText().length();
                    resultTextArea.append(posTagText.toString());

                    resultTextArea.setCaretPosition(0);
                    try {
                        // Highlight the word in the sentence
                        int wordStart = sentenceStartPos + sentenceText.indexOf(token);
                        int wordEnd = wordStart + token.length();
                        highlighter.addHighlight(wordStart, wordEnd, painter);

                        // Highlight the corresponding lemma
                        int lemmaStart = lemmaStartPos + lemmaText.indexOf(lemma);
                        int lemmaEnd = lemmaStart + lemma.length();
                        highlighter.addHighlight(lemmaStart, lemmaEnd, painter);

                        // Highlight the corresponding POS tag
                        int posTagStart = posTagStartPos + posTagText.indexOf(posTag);
                        int posTagEnd = posTagStart + posTag.length();
                        highlighter.addHighlight(posTagStart, posTagEnd, painter);
                    } catch (BadLocationException e) {
                        JOptionPane.showMessageDialog(null, "Sorry, there was an issue displaying the search results \n due to a problem with the text position.", "Error", JOptionPane.ERROR_MESSAGE);

                    }
                }
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(LinguaKWICGUI::new);
    }
}
