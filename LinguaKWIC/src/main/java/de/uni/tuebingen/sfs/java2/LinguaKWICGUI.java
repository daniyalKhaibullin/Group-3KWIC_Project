package de.uni.tuebingen.sfs.java2;

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
import java.util.regex.*;
import javax.xml.stream.XMLStreamException;

public class LinguaKWICGUI extends JFrame {

    // Define colors and fonts
    private static final Color LIGHT_PRIMARY_COLOR = new Color(195, 202, 198); // A lighter gray
    private static final Color LIGHT_HIGHLIGHTER_COLOR = new Color(127, 160, 130); // Softer green
    private static final Color LIGHT_TEXT_FIELD_BACKGROUND = new Color(255, 255, 255); // White
    private static final Color LIGHT_TEXT_AREA_BACKGROUND = new Color(255, 255, 255); // Light gray
    private static final Color LIGHT_BUTTON_BACKGROUND = new Color(109, 109, 126); // Light purple
    private static final Color LIGHT_FONT_COLOR = new Color(105, 8, 19);
    private static final Color LIGHT_TEXT_FONT_COLOR = new Color(37, 37, 37);// Dark gray

    // Define colors and fonts for dark mode
    private static final Color DARK_PRIMARY_COLOR = new Color(64, 60, 71); // Darker purple
    private static final Color DARK_HIGHLIGHTER_COLOR = new Color(56, 78, 58); // Darker green
    private static final Color DARK_TEXT_FIELD_BACKGROUND = new Color(40, 40, 40); // Dark gray
    private static final Color DARK_TEXT_AREA_BACKGROUND = new Color(60, 60, 60); // Medium dark gray
    private static final Color DARK_BUTTON_BACKGROUND = new Color(80, 80, 120); // Dark purple
    private static final Color DARK_FONT_COLOR = new Color(220, 220, 220); // Light gray
    //Define Fonts
    private static final Font FONT = new Font("Phosphate", Font.BOLD, 16);
    private static final Font INPUT_FONT = new Font("Rockwell", Font.PLAIN, 15);
    private static final Font BUTTON_FONT = new Font("Rockwell", Font.BOLD, 15);

    private static Color highlighterColor;

    private static final int MAX_RECENT_ENTRIES = 15;

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
    static JButton saveAll;


    static JButton saveButton;
    static JTextArea resultTextArea;

    static File selectedFile;
    static LinguaKWIC linguaKWIC;

    //
    static DefaultListModel<String> recentListModel;
    static JList<String> recentList;

    private static final Queue<History> recentHistory = new ArrayDeque<>();
    private static final Queue<RecordKeeper> recordKeeper = new ArrayDeque<>();

    //Panels
    public JPanel rootPanel = new JPanel(new BorderLayout());
    public JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    public JPanel leftPanel = new JPanel(new BorderLayout());
    public JPanel recentPanel = new JPanel(new BorderLayout());
    public JPanel optionsPanel = new JPanel(new GridBagLayout());
    public JPanel centerPanel = new JPanel(new BorderLayout());
    public JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    public JPanel leftPanelForTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

    // Theme toggle button
    private JButton themeToggleButton;
    private static boolean isDarkMode = false;

    //JLables :
    public JLabel target = new JLabel("Search in:");
    public JLabel recent = new JLabel("History of Search (click for see more details):");
    public JLabel rn = new JLabel("Right Neighbor:");
    public JLabel ln = new JLabel("Left Neighbor:");
    public JLabel result = new JLabel("Result:");

    ///
    private static int indexOfRecord;


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
        saveButton = new JButton("SAVE THIS SEARCH TO XML");
        saveButton.addActionListener(new SaveButtonHandler());

        saveAll = new JButton("SAVE ALL HISTORY TO XML");
        saveAll.addActionListener(new SaveAllButtonHandler());

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
        highlighterColor = isDarkMode ? DARK_HIGHLIGHTER_COLOR : LIGHT_HIGHLIGHTER_COLOR;


        // Apply colors to panels
        getContentPane().setBackground(primaryColor);
        topPanel.setBackground(primaryColor);
        leftPanelForTop.setBackground(primaryColor);
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
        browseFileButton.setFont(BUTTON_FONT);
        advanceSearchButton.setBackground(buttonBackground);
        advanceSearchButton.setForeground(fontColor);
        advanceSearchButton.setFont(BUTTON_FONT);
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
        themeToggleButton.setFont(BUTTON_FONT);
        searchButton.setBackground(buttonBackground);
        searchButton.setForeground(fontColor);
        searchButton.setFont(BUTTON_FONT);
        saveButton.setBackground(buttonBackground);
        saveButton.setForeground(fontColor);
        saveButton.setFont(BUTTON_FONT);


        saveAll.setBackground(buttonBackground);
        saveAll.setForeground(fontColor);
        saveAll.setFont(BUTTON_FONT);

        // Apply colors and fonts to text area and list
        resultTextArea.setBackground(textAreaBackground);
        resultTextArea.setForeground(textfontColor);
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
        topPanel.setLayout(new BorderLayout());  // Change layout manager to BorderLayout
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a panel to hold left-aligned components
        leftPanelForTop.add(target);
        leftPanelForTop.add(searchField);
        leftPanelForTop.add(browseFileButton);
        leftPanelForTop.add(advanceSearchButton);

        // Add the left-aligned panel to the CENTER of the topPanel
        topPanel.add(leftPanelForTop, BorderLayout.CENTER);

        // Add the themeToggleButton to the EAST of the topPanel
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

        bottomPanel.add(saveAll);
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
            try {
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
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error loading target: this file is not valid or doesnt have a content to analyse.", "Error", JOptionPane.ERROR_MESSAGE);

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
                    String lang = Objects.requireNonNull(languageComboBox.getSelectedItem()).toString();
                    if (lang.equals("English")) {
                        lang = "en";
                    } else {
                        lang = "de";
                    }
                    String url = "https://" + lang + ".wikipedia.org/wiki/" + searchTopic.replace(" ", "_");
                    if (!isValidURL(url)) {
                        JOptionPane.showMessageDialog(null, "Error performing advanced search: couldn't find the topic", "Error", JOptionPane.ERROR_MESSAGE);
                    }
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
                okButton.setForeground(DARK_FONT_COLOR);
                okButton.setFont(BUTTON_FONT);
            } else {
                contentPanel.setBackground(LIGHT_PRIMARY_COLOR);
                formPanel.setBackground(LIGHT_PRIMARY_COLOR);
                advancedDialog.setBackground(LIGHT_PRIMARY_COLOR);
                okButton.setBackground(LIGHT_BUTTON_BACKGROUND);
                okButton.setForeground(LIGHT_FONT_COLOR);
                okButton.setFont(BUTTON_FONT);
            }
            advancedDialog.setVisible(true);
        }
    }

    private static class SaveButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<RecordKeeper> records = new ArrayList<>(recordKeeper);
            RecordKeeper record = records.get(indexOfRecord);
            List<RecordKeeper> sendData = new ArrayList<>();
            sendData.add(record);

            // Create a file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save as XML");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // Add a file filter to only show XML files
            FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("XML files", "xml");
            fileChooser.setFileFilter(xmlFilter);

            // Show save dialog and get user selection
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                // Ensure the file has a .xml extension
                if (!filePath.toLowerCase().endsWith(".xml")) {
                    filePath += ".xml";
                }

                // Call the writeXML method and handle exceptions
                try {
                    XMLWriter writer = new XMLWriter();
                    writer.writeXML(filePath, sendData);
                    JOptionPane.showMessageDialog(null, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (XMLStreamException | IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static class SaveAllButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<RecordKeeper> records = new ArrayList<>(recordKeeper);


            // Create a file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save as XML");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // Add a file filter to only show XML files
            FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("XML files", "xml");
            fileChooser.setFileFilter(xmlFilter);

            // Show save dialog and get user selection
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                // Ensure the file has a .xml extension
                if (!filePath.toLowerCase().endsWith(".xml")) {
                    filePath += ".xml";
                }

                // Call the writeXML method and handle exceptions
                try {
                    XMLWriter writer = new XMLWriter();
                    writer.writeXML(filePath,records);
                    JOptionPane.showMessageDialog(null, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (XMLStreamException | IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
                        System.out.println("Error: " + ioe.getMessage());
                        JOptionPane.showMessageDialog(null, "Error loading from URL : this URL is not valid", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else if (filePathOrLink != null) {
                    try {
                        linguaKWIC = new LinguaKWIC(selectedFile);
                    } catch (Exception t) {
                        t.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error loading from file. \n Please load the file with Brows button.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    RecordKeeper record = new RecordKeeper(NLPResults, tokens, lemmas, posTags, searchField.getText(), token + ", " + lemma + ", " + posTag, "word, pos, lemma");
                    addHistory(detail, record);
                    addRecentEntry(token + " " + lemma + " " + posTag + "(in " + searchField.getText() + ")");
                } else if (exactWordCheckBox.isSelected() && wordLemmaCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTokenAndLemm(token, lemma, caseSensitive);
                    addRecentEntry(token + " " + lemma + "(in " + searchField.getText() + ")");
                    History detail = new History(token + " " + lemma, searchField.getText(), NLPResults.size());
                    RecordKeeper record = new RecordKeeper(NLPResults, tokens, lemmas, posTags, searchField.getText(), token + ", " + lemma, "word, lemma");
                    addHistory(detail, record);
                } else if (exactWordCheckBox.isSelected() && wordPOSTagCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTokenAndTag(token, posTag, caseSensitive);
                    addRecentEntry(token + " " + posTag + "(in " + searchField.getText() + ")");
                    History detail = new History(token + " " + posTag, searchField.getText(), NLPResults.size());
                    RecordKeeper record = new RecordKeeper(NLPResults, tokens, lemmas, posTags, searchField.getText(), token + ", " + posTag, "word, pos");
                    addHistory(detail, record);
                } else if (wordLemmaCheckBox.isSelected() && wordPOSTagCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTagAndLemm(posTag, lemma, caseSensitive);
                    addRecentEntry(lemma + " " + posTag + "(in " + searchField.getText() + ")");
                    History detail = new History(lemma + " " + posTag, searchField.getText(), NLPResults.size());
                    RecordKeeper record = new RecordKeeper(NLPResults, tokens, lemmas, posTags, searchField.getText(), lemma + ", " + posTag, "lemma, posTag");
                    addHistory(detail, record);
                } else if (exactWordCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByToken(token, caseSensitive);
                    addRecentEntry(token + "(in " + searchField.getText() + ")");
                    History detail = new History(token, searchField.getText(), NLPResults.size());
                    RecordKeeper record = new RecordKeeper(NLPResults, tokens, lemmas, posTags, searchField.getText(), token, "word");
                    addHistory(detail, record);
                } else if (wordLemmaCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByLemm(lemma, caseSensitive);
                    addRecentEntry(lemma + "(in " + searchField.getText() + ")");
                    History detail = new History(lemma, searchField.getText(), NLPResults.size());
                    RecordKeeper record = new RecordKeeper(NLPResults, tokens, lemmas, posTags, searchField.getText(), lemma, "lemma");
                    addHistory(detail, record);
                } else if (wordPOSTagCheckBox.isSelected()) {
                    NLPResults = linguaKWIC.getTextSearch().searchByTag(posTag, caseSensitive);
                    addRecentEntry(posTag + "(in " + searchField.getText() + ")");
                    History detail = new History(posTag, searchField.getText(), NLPResults.size());
                    RecordKeeper record = new RecordKeeper(NLPResults, tokens, lemmas, posTags, searchField.getText(), posTag, "pos");
                    addHistory(detail, record);
                }


                // Display search results in the GUI
                displaySearchResults(NLPResults, tokens, lemmas, posTags);
                indexOfRecord = recentHistory.size() - 1;
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error loading data: this URL is not valid or doesnt have a content to analyse.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


        private static void displaySearchResults(List<TextSearch.Pair> results, List<List<String>> tokens,
                                                 List<List<String>> lemmas, List<List<String>> posTags) throws Exception {
            if (results == null || results.isEmpty()) {
                resultTextArea.append("No results found.");
                return;
            }

            Highlighter highlighter = resultTextArea.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(highlighterColor);

            resultTextArea.setText("");
            String matchInfo = results.size() + " match(es) found:\n\n";
            resultTextArea.append(matchInfo);

            if (neighborRadioButton.isSelected()) {
                highlightWithNeighbors(results, tokens, lemmas, posTags, highlighter, painter);
            } else {
                highlightWithoutNeighbors(results, tokens, lemmas, posTags, highlighter, painter);
            }

            resultTextArea.setCaretPosition(0);
        }

        private static void highlightWithNeighbors(List<TextSearch.Pair> results, List<List<String>> tokens,
                                                   List<List<String>> lemmas, List<List<String>> posTags,
                                                   Highlighter highlighter, Highlighter.HighlightPainter painter) throws BadLocationException {
            for (TextSearch.Pair result : results) {
                int sentenceIndex = result.getSentenceIndex();
                int tokenIndex = result.getTokenIndex();
                int start;
                int end;
                int leftN = (int) neighborLeftSpinner.getValue();
                int rightN = (int) neighborRightSpinner.getValue();
                start = Math.max(0, tokenIndex - rightN);
                end = Math.min(tokens.get(sentenceIndex).size(), tokenIndex + leftN + 1);

                if (sentenceIndex < tokens.size() && tokenIndex < tokens.get(sentenceIndex).size()) {
                    String token = tokens.get(sentenceIndex).get(tokenIndex);
                    String lemma = lemmas.get(sentenceIndex).get(tokenIndex);
                    String posTag = posTags.get(sentenceIndex).get(tokenIndex);

                    StringBuilder sentenceText = new StringBuilder("Sentence " + (sentenceIndex + 1) + ": ");
                    StringBuilder lemmaText = new StringBuilder("Lemmas: ");
                    StringBuilder posTagText = new StringBuilder("POS Tags: ");

                    for (int i = start; i < end; i++) {
                        if (i == tokenIndex) {
                            sentenceText.append("[[");
                            lemmaText.append("[[");
                            posTagText.append("[[");
                        }
                        sentenceText.append(tokens.get(sentenceIndex).get(i)).append(" ");
                        lemmaText.append(lemmas.get(sentenceIndex).get(i)).append(" ");
                        posTagText.append(posTags.get(sentenceIndex).get(i)).append(" ");
                        if (i == tokenIndex) {
                            sentenceText.append("]]");
                            lemmaText.append("]]");
                            posTagText.append("]]");
                        }
                    }
                    sentenceText.append("\n");
                    lemmaText.append("\n");
                    posTagText.append("\n\n");
                    resultTextArea.append(sentenceText.toString());
                    resultTextArea.append(lemmaText.toString());
                    resultTextArea.append(posTagText.toString());

                    highlightWords(resultTextArea);
                }
            }
        }

        private static void highlightWithoutNeighbors(List<TextSearch.Pair> results, List<List<String>> tokens,
                                                      List<List<String>> lemmas, List<List<String>> posTags,
                                                      Highlighter highlighter, Highlighter.HighlightPainter painter) throws Exception {
            // Sort results by sentence index and then by token index
            results.sort(Comparator.comparingInt(TextSearch.Pair::getSentenceIndex)
                    .thenComparingInt(TextSearch.Pair::getTokenIndex));

            // Group results by sentence index
            Map<Integer, List<Integer>> sentenceToTokensMap = new LinkedHashMap<>();
            for (TextSearch.Pair result : results) {
                sentenceToTokensMap.computeIfAbsent(result.getSentenceIndex(), k -> new ArrayList<>()).add(result.getTokenIndex());
            }

            // Process each sentence with matches
            for (Map.Entry<Integer, List<Integer>> entry : sentenceToTokensMap.entrySet()) {
                int sentenceIndex = entry.getKey();
                List<Integer> tokenIndices = entry.getValue();

                if (sentenceIndex < tokens.size()) {
                    StringBuilder sentenceText = new StringBuilder("Sentence " + (sentenceIndex + 1) + ": ");
                    StringBuilder lemmaText = new StringBuilder("Lemmas: ");
                    StringBuilder posTagText = new StringBuilder("POS Tags: ");

                    for (int i = 0; i < tokens.get(sentenceIndex).size(); i++) {
                        if (tokenIndices.contains(i)) {
                            sentenceText.append("[[");
                            lemmaText.append("[[");
                            posTagText.append("[[");
                        }

                        sentenceText.append(tokens.get(sentenceIndex).get(i)).append(" ");
                        lemmaText.append(lemmas.get(sentenceIndex).get(i)).append(" ");
                        posTagText.append(posTags.get(sentenceIndex).get(i)).append(" ");

                        if (tokenIndices.contains(i)) {
                            sentenceText.append("]]");
                            lemmaText.append("]]");
                            posTagText.append("]]");
                        }
                    }

                    sentenceText.append("\n");
                    lemmaText.append("\n");
                    posTagText.append("\n\n");


                    resultTextArea.append(sentenceText.toString());
                    resultTextArea.append(lemmaText.toString());
                    resultTextArea.append(posTagText.toString());

                    highlightWords(resultTextArea);

                }
            }
        }

        private static void highlightWords(JTextArea textArea) throws BadLocationException {
            // Get original content
            String content = textArea.getText();

            // Pattern to match [[text]]
            Pattern pattern = Pattern.compile("\\[\\[(.*?)\\]\\]");
            Matcher matcher = pattern.matcher(content);

            // Highlighter setup
            Highlighter highlighter = textArea.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(highlighterColor);

            // To avoid text manipulation, we will directly highlight matching segments
            while (matcher.find()) {
                // Highlight the text between [[ and ]]
                int start = matcher.start(1);
                int end = matcher.end(1);

                // Use the original content's offsets
                highlighter.addHighlight(start, end, painter);
            }
        }
    }

    private void handleRecentListClick(MouseEvent evt) {
        try {
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

                JButton showAgainButton = new JButton("Show The Result Again");
                showAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                showAgainButton.addActionListener(event -> {
                    indexOfRecord = recordKeeper.size() - 1 - index;
                    List<RecordKeeper> records = new ArrayList<>(recordKeeper);
                    RecordKeeper record = records.get(indexOfRecord);
                    try {
                        SearchButtonHandler.displaySearchResults(record.getResults(), record.getTokens(), record.getLemmas(), record.getPosTags());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    advancedDialog.dispose();
                });
                contentPanel.add(showAgainButton);

                advancedDialog.add(contentPanel, BorderLayout.CENTER);
                if (isDarkMode) {
                    contentPanel.setBackground(DARK_PRIMARY_COLOR);
                    infoLabel.setBackground(DARK_PRIMARY_COLOR);
                    advancedDialog.setBackground(DARK_PRIMARY_COLOR);
                    showAgainButton.setBackground(DARK_BUTTON_BACKGROUND);
                    showAgainButton.setForeground(DARK_FONT_COLOR);
                    showAgainButton.setFont(BUTTON_FONT);
                } else {
                    contentPanel.setBackground(LIGHT_PRIMARY_COLOR);
                    infoLabel.setBackground(LIGHT_PRIMARY_COLOR);
                    advancedDialog.setBackground(LIGHT_PRIMARY_COLOR);
                    showAgainButton.setBackground(LIGHT_BUTTON_BACKGROUND);
                    showAgainButton.setForeground(LIGHT_FONT_COLOR);
                    showAgainButton.setFont(BUTTON_FONT);
                }
                advancedDialog.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: something went wrong :( ", "Error", JOptionPane.ERROR_MESSAGE);
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

    public static void addHistory(History history, RecordKeeper record) {
        if (recentHistory.size() >= MAX_RECENT_ENTRIES) {
            recentHistory.poll();
            recordKeeper.poll();
        }
        recentHistory.offer(history);
        recordKeeper.offer(record);
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
