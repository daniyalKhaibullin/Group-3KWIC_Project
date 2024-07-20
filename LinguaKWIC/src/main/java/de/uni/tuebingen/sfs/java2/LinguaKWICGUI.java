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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class LinguaKWICGUI extends JFrame {

    // Define colors and fonts

    private static final Color LIGHT_PRIMARY_COLOR = new Color(195, 202, 198); // A lighter gray
    private static final Color LIGHT_SECONDARY_COLOR = new Color(127, 160, 130); // Softer green
    private static final Color LIGHT_TEXT_FIELD_BACKGROUND = new Color(255, 255, 255); // White
    private static final Color LIGHT_TEXT_AREA_BACKGROUND = new Color(255, 255, 255); // Light gray
    private static final Color LIGHT_BUTTON_BACKGROUND = new Color(109, 109, 126); // Light purple
    private static final Color LIGHT_FONT_COLOR = new Color(105,8,19);
    private static final Color LIGHT_TEXT_FONT_COLOR = new Color(37, 37, 37);// Dark gray
    private static final Color LIGHT_HIGHLIGHTER = new Color(255, 255, 153);


    // Define colors and fonts for dark mode
    private static final Color DARK_PRIMARY_COLOR = new Color(64, 60, 71); // Darker purple
    private static final Color DARK_SECONDARY_COLOR = new Color(56, 78, 58); // Darker green
    private static final Color DARK_TEXT_FIELD_BACKGROUND = new Color(40, 40, 40); // Dark gray
    private static final Color DARK_TEXT_AREA_BACKGROUND = new Color(60, 60, 60); // Medium dark gray
    private static final Color DARK_BUTTON_BACKGROUND = new Color(80, 80, 120); // Dark purple
    private static final Color DARK_FONT_COLOR = new Color(220, 220, 220); // Light gray
    private static final Color DARK_HIGHLIGHTER = new Color(40, 40, 40);

    private static final Font FONT = new Font("Rockwell", Font.BOLD, 15);
    private static final Font INPUT_FONT = new Font("Rockwell", Font.PLAIN, 15);

    private static Color highlighterColor;

    private static final int MAX_RECENT_ENTRIES = 25;

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


    static JButton saveButton;
    static JTextArea resultTextArea;

    static File selectedFile;
    static LinguaKWIC linguaKWIC;

    //
    static DefaultListModel<String> recentListModel;
    static JList<String> recentList;

    private static Queue<History> recentHistory = new ArrayDeque<>();

    //Panels
    public JPanel rootPanel = new JPanel(new BorderLayout());
    public JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    public JPanel leftPanel = new JPanel(new BorderLayout());
    public JPanel recentPanel = new JPanel(new BorderLayout());
    public JPanel optionsPanel = new JPanel(new GridBagLayout());
    public JPanel centerPanel = new JPanel(new BorderLayout());
    public JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    // Theme toggle button
    private JButton themeToggleButton;
    private static boolean isDarkMode = false;

    //JLables :
    public JLabel target = new JLabel("Search in:");
    public JLabel recent = new JLabel("History of Search (click for see more details):");
    public JLabel rn = new JLabel("Right Neighbor:");
    public JLabel ln = new JLabel("Left Neighbor:");
    public JLabel result = new JLabel("Result:");

    public LinguaKWICGUI() {
        initializeComponents();
        setTheme();
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
        // Theme toggle button setup
        themeToggleButton = new JButton("Toggle Theme");
        themeToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDarkMode = !isDarkMode;
                setTheme();
            }
        });

        // West components
        exactWordCheckBox = new JCheckBox("EXACT WORD");
        wordLemmaCheckBox = new JCheckBox("WORD'S LEMMA");
        wordPOSTagCheckBox = new JCheckBox("POS-TAG");
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

        resultTextArea.setEditable(false);

        //
        recentListModel = new DefaultListModel<>();
        recentList = new JList<>(recentListModel);
        recentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recentList.setLayoutOrientation(JList.VERTICAL);
        recentList.setVisibleRowCount(-1);
        recentList.setVisibleRowCount(15);
        JScrollPane scrollPane = new JScrollPane(recentList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        recentList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    handleRecentListClick(evt);
                }
            }
        });

    }

    private void setTheme() {
        Color primaryColor = isDarkMode ? DARK_PRIMARY_COLOR : LIGHT_PRIMARY_COLOR;
        Color textFieldBackground = isDarkMode ? DARK_TEXT_FIELD_BACKGROUND : LIGHT_TEXT_FIELD_BACKGROUND;
        Color textAreaBackground = isDarkMode ? DARK_TEXT_AREA_BACKGROUND : LIGHT_TEXT_AREA_BACKGROUND;
        Color buttonBackground = isDarkMode ? DARK_BUTTON_BACKGROUND : LIGHT_BUTTON_BACKGROUND;
        Color fontColor = isDarkMode ? DARK_FONT_COLOR : LIGHT_FONT_COLOR;
        Color textfontColor = isDarkMode ? DARK_FONT_COLOR : LIGHT_TEXT_FONT_COLOR;
        highlighterColor = isDarkMode ? DARK_HIGHLIGHTER : LIGHT_HIGHLIGHTER;


        // Apply colors to panels
        getContentPane().setBackground(primaryColor);
        topPanel.setBackground(primaryColor);
        leftPanel.setBackground(primaryColor);
        bottomPanel.setBackground(primaryColor);
        centerPanel.setBackground(primaryColor);
        optionsPanel.setBackground(primaryColor);
        recentPanel.setBackground(primaryColor);
        rootPanel.setBackground(primaryColor);

        // Apply colors to text fields and buttons
        searchField.setBackground(textFieldBackground);
        searchField.setForeground(fontColor);
        browseFileButton.setBackground(buttonBackground);
        browseFileButton.setForeground(fontColor);
        advanceSearchButton.setBackground(buttonBackground);
        advanceSearchButton.setForeground(fontColor);
        exactWordCheckBox.setForeground(fontColor);
        wordLemmaCheckBox.setForeground(fontColor);
        wordPOSTagCheckBox.setForeground(fontColor);
        caseSensitiveCheckBox.setForeground(fontColor);
        exactWordField.setBackground(textFieldBackground);
        exactWordField.setForeground(fontColor);
        wordLemmaField.setBackground(textFieldBackground);
        wordLemmaField.setForeground(fontColor);
        wordPOSTagField.setBackground(textFieldBackground);
        wordPOSTagField.setForeground(fontColor);
        wholeSentenceRadioButton.setForeground(fontColor);
        neighborRadioButton.setForeground(fontColor);
        neighborRightSpinner.setForeground(fontColor);
        neighborLeftSpinner.setForeground(fontColor);
        themeToggleButton.setBackground(buttonBackground);
        themeToggleButton.setForeground(fontColor);
        searchButton.setBackground(buttonBackground);
        searchButton.setForeground(fontColor);
        saveButton.setBackground(buttonBackground);
        saveButton.setForeground(fontColor);

        // Apply colors and fonts to text area and list
        resultTextArea.setBackground(textAreaBackground);
        resultTextArea.setForeground(fontColor);
        resultTextArea.setFont(INPUT_FONT);
        resultTextArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        recentList.setBackground(textAreaBackground);
        recentList.setForeground(fontColor);
        recentList.setFont(INPUT_FONT);
        recentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recentList.setLayoutOrientation(JList.VERTICAL);
        recentList.setVisibleRowCount(15);
        recentList.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        recentList.setOpaque(true);

        // Apply fonts to specific fields
        searchField.setFont(INPUT_FONT);
        exactWordField.setFont(INPUT_FONT);
        neighborRightSpinner.setFont(FONT);
        neighborLeftSpinner.setFont(FONT);

        target.setForeground(fontColor);
        target.setFont(FONT);

        recent.setForeground(fontColor);
        recent.setFont(FONT);

        rn.setForeground(fontColor);
        rn.setFont(FONT);

        ln.setForeground(fontColor);
        ln.setFont(FONT);

        result.setForeground(fontColor);
        result.setFont(FONT);

    }


    private void createLayout() {
        setTitle("LinguaKWIC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 1000);
        setLocationRelativeTo(null);

        // Main panel using BorderLayout
        setContentPane(rootPanel);

        // Top Panel (North)
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(target);
        topPanel.add(searchField);
        topPanel.add(browseFileButton);
        topPanel.add(advanceSearchButton);
        topPanel.add(themeToggleButton, BorderLayout.EAST);
        rootPanel.add(topPanel, BorderLayout.NORTH);
        recentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        recentPanel.add(recent, BorderLayout.NORTH);
        JScrollPane recentScrollPane = new JScrollPane(recentList);
        recentScrollPane.setPreferredSize(new Dimension(200, 200));
        recentPanel.add(recentScrollPane, BorderLayout.CENTER);


        leftPanel.add(recentPanel, BorderLayout.CENTER);

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
        optionsPanel.add(rn, gbc);

        gbc.gridx = 1;
        optionsPanel.add(neighborRightSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        optionsPanel.add(ln, gbc);

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

        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setPreferredSize(new Dimension(800, 800)); // Adjust size
        centerPanel.add(result, BorderLayout.NORTH);
        centerPanel.add(resultScrollPane, BorderLayout.CENTER);

        rootPanel.add(centerPanel, BorderLayout.CENTER);

        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bottomPanel.add(saveButton);

        rootPanel.add(bottomPanel, BorderLayout.SOUTH);

        exactWordCheckBox.addActionListener(e -> exactWordField.setEnabled(exactWordCheckBox.isSelected()));
        wordLemmaCheckBox.addActionListener(e -> wordLemmaField.setEnabled(wordLemmaCheckBox.isSelected()));
        wordPOSTagCheckBox.addActionListener(e -> wordPOSTagField.setEnabled(wordPOSTagCheckBox.isSelected()));


        setVisible(true);
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

            // Center the dialog on the screen
            advancedDialog.setLocationRelativeTo(LinguaKWICGUI.this);

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel infoLabel = new JLabel("<html><p style='text-align:center;'>In Advanced search, you will provide us a topic in the language that you want and we will search in Wikipedia for your topic :)</p></html>");
            infoLabel.setFont(FONT.deriveFont(Font.BOLD));
            infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(infoLabel);
            contentPanel.add(Box.createVerticalStrut(20));

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(2, 2, 10, 10));

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
            if (isDarkMode) {
                contentPanel.setBackground(DARK_PRIMARY_COLOR);
                formPanel.setBackground(DARK_PRIMARY_COLOR);
                advancedDialog.setBackground(DARK_PRIMARY_COLOR);
                okButton.setBackground(DARK_BUTTON_BACKGROUND);
            } else {
                contentPanel.setBackground(LIGHT_PRIMARY_COLOR);
                formPanel.setBackground(LIGHT_PRIMARY_COLOR);
                advancedDialog.setBackground(LIGHT_PRIMARY_COLOR);
                okButton.setBackground(LIGHT_BUTTON_BACKGROUND);
            }
            advancedDialog.setVisible(true);
        }
    }

    private static class SaveButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
//                java.util.List<UploadedFile> uploadedFiles = new ArrayList<>();
//                java.util.List<ProcessedFile> processedFiles = new ArrayList<>();
//                java.util.List<WikipediaArticle> articles = new ArrayList<>();
//
//                if (selectedFile.exists()) {
//                    uploadedFiles.add(new UploadedFile(selectedFile.getName(), linguaKWIC.getText()));
//                    processedFiles.add(new ProcessedFile(selectedFile.getName(), linguaKWIC.getTokens().stream()
//                            .flatMap(java.util.List::stream)
//                            .collect(Collectors.toList()), linguaKWIC.getLemmas().stream()
//                            .flatMap(java.util.List::stream)
//                            .collect(Collectors.toList()), linguaKWIC.getPosTags().stream()
//                            .flatMap(List::stream)
//                            .collect(Collectors.toList())));
//                }
//                if (isValidURL(searchField.getText())) {
//                    articles.add(new WikipediaArticle(searchField.getText(), linguaKWIC.getText()));
//                }
//
//                JFileChooser fileChooser = new JFileChooser();
//                fileChooser.setDialogTitle("Specify a file to save");
//                FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
//                fileChooser.setFileFilter(filter);
//                fileChooser.setAcceptAllFileFilterUsed(false);
//
//                int userSelection = fileChooser.showSaveDialog(null);
//                if (userSelection == JFileChooser.APPROVE_OPTION) {
//                    File fileToSave = fileChooser.getSelectedFile();
//                    XMLWriter xmlWriter = new XMLWriter();
//                    xmlWriter.writeXML(fileToSave.getAbsolutePath(), uploadedFiles, processedFiles, articles);
//                }
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
                    History detail = new History(token + " " + lemma + " " + posTag, searchField.getText(), NLPResults.size());
                    addHistory(detail);
                    addRecentEntry(token + " " + lemma + " " + posTag + "(in " + searchField.getText() + ")");
                } else if (exactWordCheckBox.isSelected() && wordLemmaCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTokenAndLemm(token, lemma, caseSensitive);
                    addRecentEntry(token + " " + lemma + "(in " + searchField.getText() + ")");
                    History detail = new History(token + " " + lemma, searchField.getText(), NLPResults.size());
                    addHistory(detail);
                } else if (exactWordCheckBox.isSelected() && wordPOSTagCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTokenAndTag(token, posTag, caseSensitive);
                    addRecentEntry(token + " " + posTag + "(in " + searchField.getText() + ")");
                    History detail = new History(token + " " + posTag, searchField.getText(), NLPResults.size());
                    addHistory(detail);
                } else if (wordLemmaCheckBox.isSelected() && wordPOSTagCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTagAndLemm(posTag, lemma, caseSensitive);
                    addRecentEntry(lemma + " " + posTag + "(in " + searchField.getText() + ")");
                    History detail = new History(lemma + " " + posTag, searchField.getText(), NLPResults.size());
                    addHistory(detail);
                } else if (exactWordCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByToken(token, caseSensitive);
                    addRecentEntry(token + "(in " + searchField.getText() + ")");
                    History detail = new History(token, searchField.getText(), NLPResults.size());
                    addHistory(detail);
                } else if (wordLemmaCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByLemm(lemma, caseSensitive);
                    addRecentEntry(lemma + "(in " + searchField.getText() + ")");
                    History detail = new History(lemma, searchField.getText(), NLPResults.size());
                    addHistory(detail);
                } else if (wordPOSTagCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTag(posTag, caseSensitive);
                    addRecentEntry(posTag + "(in " + searchField.getText() + ")");
                    History detail = new History(posTag, searchField.getText(), NLPResults.size());
                    addHistory(detail);
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
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(highlighterColor);

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

    private void handleRecentListClick(MouseEvent evt) {
        int index = recentList.locationToIndex(evt.getPoint());

        if (index >= 0) {
            History selectedHistory = getHistoryByIndex(recentListModel.size() - (index + 1));

            // Create a new JDialog
            JDialog advancedDialog = new JDialog(this, "Search History", true);
            advancedDialog.setSize(500, 500);
            advancedDialog.setLayout(new BorderLayout());

            // Center the dialog on the screen
            advancedDialog.setLocationRelativeTo(this);
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel infoLabel = new JLabel(selectedHistory.toString());
            infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(infoLabel);
            contentPanel.add(Box.createVerticalStrut(20));

            JButton okButton = new JButton("OK");
            okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            okButton.addActionListener(event -> {
                advancedDialog.dispose();
            });
            contentPanel.add(okButton);
            advancedDialog.add(contentPanel, BorderLayout.CENTER);
            if (isDarkMode) {
                contentPanel.setBackground(DARK_PRIMARY_COLOR);
                infoLabel.setBackground(DARK_PRIMARY_COLOR);
                advancedDialog.setBackground(DARK_PRIMARY_COLOR);
                okButton.setBackground(DARK_BUTTON_BACKGROUND);
            } else {
                contentPanel.setBackground(LIGHT_PRIMARY_COLOR);
                infoLabel.setBackground(LIGHT_PRIMARY_COLOR);
                advancedDialog.setBackground(LIGHT_PRIMARY_COLOR);
                okButton.setBackground(LIGHT_BUTTON_BACKGROUND);
            }
            advancedDialog.setVisible(true);
        }
    }

    private static void addRecentEntry(String entry) {
        recentListModel.add(0, entry);

        // Remove the oldest entry if the list exceeds the maximum capacity
        if (recentListModel.size() > MAX_RECENT_ENTRIES) {
            recentListModel.removeElementAt(MAX_RECENT_ENTRIES);
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

    public static void addHistory(History history) {
        if (recentHistory.size() >= MAX_RECENT_ENTRIES) {
            recentHistory.poll();
        }
        recentHistory.offer(history);
    }

    private History getHistoryByIndex(int index) {
        List<History> historyList = new ArrayList<>(recentHistory);
        return historyList.get(index);
    }


    public static void main(String[] args) {
        //this try part helps windows and linux user get the same result as mac users!
        try {
            // Set Nimbus look and feel
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the default cross-platform L&F
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(LinguaKWICGUI::new);
    }
}
