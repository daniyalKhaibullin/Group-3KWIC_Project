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
import java.awt.*;

public class AidaGUI {

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
        JTextField targetField = new JTextField(30);
        targetField.setFont(font);

        String[] urlFileOptions = {"URL", "File"};
        JComboBox<String> urlFileComboBox = new JComboBox<>(urlFileOptions);
        urlFileComboBox.setFont(font);

        targetPanel.add(targetLabel);
        targetPanel.add(targetField);
        targetPanel.add(urlFileComboBox);

        // Second line for checkboxes and fields
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.setBackground(panelColor);

        JCheckBox exactWordCheckBox = new JCheckBox("Exact word");
        JCheckBox wordLemmaCheckBox = new JCheckBox("Word lemma");
        JCheckBox wordPOSTagCheckBox = new JCheckBox("Word POS Tag");

        JTextField exactWordField = new JTextField(15);
        JTextField wordLemmaField = new JTextField(15);
        JTextField wordPOSTagField = new JTextField(15);

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

        JRadioButton wholeSentenceRadioButton = new JRadioButton("Whole Sentence");
        JRadioButton neighborRadioButton = new JRadioButton("Neighbor");

        ButtonGroup sentenceGroup = new ButtonGroup();
        sentenceGroup.add(wholeSentenceRadioButton);
        sentenceGroup.add(neighborRadioButton);

        JSpinner neighborXSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JSpinner neighborYSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

        neighborXSpinner.setEnabled(false);
        neighborYSpinner.setEnabled(false);

        neighborRadioButton.addActionListener(e -> {
            boolean selected = neighborRadioButton.isSelected();
            neighborXSpinner.setEnabled(selected);
            neighborYSpinner.setEnabled(selected);
        });

        JCheckBox caseSensitiveCheckBox = new JCheckBox("Case Sensitive");

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
        JTextArea searchResultsArea = new JTextArea();
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

        JButton advanceButton = new JButton("Advance");
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
}

