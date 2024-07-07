//it failed to load any pics or font....AAAAAAAAAAAA
//the good news here is that the layouts can really move freely now
package de.uni.tuebingen.sfs.java2;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class LinguaKWICGUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Font customFont;
    JCheckBox exactWordCheckBox;
    JCheckBox wordLemmaCheckBox;
    JCheckBox wordPOSTagCheckBox;
    JCheckBox caseSensitiveCheckBox;
    JTextField fileField;
    JTextArea searchWord;
    static JTextArea result;
    File file = null;
    List<TextSearch.Pair> results;

    public LinguaKWICGUI() {
        setTitle("Lingua KWIC");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        loadCustomFont();
        getContentPane().setBackground(new Color(153, 153, 189));

        initializeComponents();

        setVisible(true);
    }

    private void loadCustomFont() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/LanaPixel.ttf")).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.PLAIN, 14); // Fallback to Serif if custom font loading fails (as always
        }
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel fileLabel = new JLabel("File:");
        fileLabel.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(fileLabel, gbc);

        fileField = new JTextField(40);
        fileField.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        add(fileField, gbc);

        JButton browseButton = new JButton("Browse");
        browseButton.setFont(customFont);
        gbc.gridx = 4;
        gbc.gridwidth = 1;
        try {
            Image img1 = ImageIO.read(getClass().getResource("/resources/IMG_0190.PNG"));
            browseButton.setIcon(new ImageIcon(img1));
        } catch (Exception e) {
            System.out.println(e);
        }
        add(browseButton, gbc);
        browseButton.addActionListener(new LinguaKWICGUI.browseButtonHandler());

        ButtonGroup gt = new ButtonGroup();
        exactWordCheckBox = new JCheckBox("Exact word");
        exactWordCheckBox.setSelected(true);
        exactWordCheckBox.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(exactWordCheckBox, gbc);
        gt.add(exactWordCheckBox);
        exactWordCheckBox.addItemListener(new LinguaKWICGUI.exactWordCheckBoxHandler());

        wordLemmaCheckBox = new JCheckBox("Word lemma");
        wordLemmaCheckBox.setFont(customFont);
        gbc.gridx = 1;
        add(wordLemmaCheckBox, gbc);
        gt.add(wordLemmaCheckBox);
        wordLemmaCheckBox.addItemListener(new LinguaKWICGUI.wordLemmaCheckBoxHandler());

        wordPOSTagCheckBox = new JCheckBox("Word POS Tag");
        wordPOSTagCheckBox.setFont(customFont);
        gbc.gridx = 2;
        add(wordPOSTagCheckBox, gbc);
        gt.add(wordPOSTagCheckBox);
        wordPOSTagCheckBox.addItemListener(new LinguaKWICGUI.wordPOSTagCheckBoxHandler());

        JButton searchButton = new JButton("Search");
        searchButton.setFont(customFont);
        gbc.gridx = 4;
        try {
            Image img2 = ImageIO.read(getClass().getResource("/resources/IMG_0192.PNG"));
            searchButton.setIcon(new ImageIcon(img2));
        } catch (Exception e) {
            System.out.println(e);
        }
        add(searchButton, gbc);
        searchButton.addActionListener(new LinguaKWICGUI.searchButtonHandler());

        searchWord = new JTextArea();
        searchWord.setFont(customFont);
        JScrollPane scrollPane = new JScrollPane(searchWord);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;

        ButtonGroup group = new ButtonGroup();
        JRadioButton wholeSentenceRadioButton = new JRadioButton("Whole Sentence");
        wholeSentenceRadioButton.setSelected(true);
        wholeSentenceRadioButton.setFont(customFont);
        group.add(wholeSentenceRadioButton);
        add(wholeSentenceRadioButton, gbc);

        JRadioButton neighborRadioButton = new JRadioButton("Neighbor");
        neighborRadioButton.setFont(customFont);
        gbc.gridy = 6;
        group.add(neighborRadioButton);
        add(neighborRadioButton, gbc);

        JTextField neighborsField1 = new JTextField(2);
        neighborsField1.setFont(customFont);
        gbc.gridx = 1;
        add(neighborsField1, gbc);

        Label toLabel = new Label("to");
        toLabel.setFont(customFont);
        gbc.gridx = 2;
        add(toLabel, gbc);

        JTextField neighborsField2 = new JTextField(2);
        neighborsField2.setFont(customFont);
        gbc.gridx = 3;
        add(neighborsField2, gbc);

        caseSensitiveCheckBox = new JCheckBox("Case Sensitive");
        caseSensitiveCheckBox.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        add(caseSensitiveCheckBox, gbc);
        caseSensitiveCheckBox.addItemListener(new LinguaKWICGUI.caseSensitiveCheckBoxHandler());

        result = new JTextArea();
        result.setEditable(false);
        result.setFont(customFont);
        JScrollPane recentScrollPane = new JScrollPane(result);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(recentScrollPane, gbc);

        JTextArea textStatistics = new JTextArea();
        textStatistics.setFont(customFont);
        JScrollPane statsScrollPane = new JScrollPane(textStatistics);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        add(statsScrollPane, gbc);

        JButton saveToXMLButton = new JButton("Save to XML");
        saveToXMLButton.setFont(customFont);
        gbc.gridx = 4;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        try {
            Image img3 = ImageIO.read(getClass().getResource("/resources/IMG_0195.PNG"));
            saveToXMLButton.setIcon(new ImageIcon(img3));
        } catch (Exception e) {
            System.out.println(e);
        }
        add(saveToXMLButton, gbc);

        // Add the image to the top right corner
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        try {
            Image img4 = ImageIO.read(getClass().getResource("/resources/IMG_0190.PNG"));
            JLabel imgLabel = new JLabel(new ImageIcon(img4));
            add(imgLabel, gbc);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private class exactWordCheckBoxHandler implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            // Check if the checkbox is selected
            if (e.getStateChange() == ItemEvent.SELECTED) {
                System.out.println("Checkbox is selected");
            } else {
                System.out.println("Checkbox is deselected");
            }
        }
    }

    private class wordLemmaCheckBoxHandler implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            JCheckBox wordLemmaCheckBox = (JCheckBox) e.getSource();
            if (e.getStateChange() == ItemEvent.SELECTED) {
                System.out.println("Checkbox is selected");
            } else {
                System.out.println("Checkbox is deselected");
            }
        }
    }

    private class wordPOSTagCheckBoxHandler implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            // Check if the checkbox is selected
            if (e.getStateChange() == ItemEvent.SELECTED) {
                System.out.println("Checkbox is selected");
            } else {
                System.out.println("Checkbox is deselected");
            }
        }
    }

    private class caseSensitiveCheckBoxHandler implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                System.out.println("Checkbox is selected");
            }
        }
    }

    private class browseButtonHandler implements ActionListener {

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
                fileField.setText(file.getName());
            }
        }
    }
    private class searchButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            LinguaKWIC linguaKWIC = null;
            String filePathOrLink = fileField.getText();
            if (isValidURL(filePathOrLink)) {
                try {
                    linguaKWIC = new LinguaKWIC(filePathOrLink);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } else if (filePathOrLink != null && new File(filePathOrLink).exists()) {
                try {
                    linguaKWIC = new LinguaKWIC(new File(filePathOrLink));
                } catch (Exception t) {
                    t.printStackTrace();
                }
            }

            if (linguaKWIC == null) {
                JOptionPane.showMessageDialog(null, "Invalid file or URL", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            java.util.List<java.util.List<String>> Sentences = linguaKWIC.getTokens();
            java.util.List<java.util.List<String>> lemmas = linguaKWIC.getLemmas();
            java.util.List<java.util.List<String>> posTags = linguaKWIC.getPosTags();

            if (wordLemmaCheckBox.isSelected()&&caseSensitiveCheckBox.isSelected()) {
                results = linguaKWIC.getTextSearch().searchByLemm(searchWord.getText());
                showAlignedLists(Sentences, lemmas, posTags, results);
            }
            if (wordPOSTagCheckBox.isSelected()&&caseSensitiveCheckBox.isSelected()) {
                results = linguaKWIC.getTextSearch().searchByTag(searchWord.getText());
                showAlignedLists(Sentences, lemmas, posTags, results);
            }
            if (exactWordCheckBox.isSelected()&&caseSensitiveCheckBox.isSelected()) {
                results = linguaKWIC.getTextSearch().searchByToken(searchWord.getText());
                showAlignedLists(Sentences, lemmas, posTags, results);
            }

            if (wordLemmaCheckBox.isSelected()&&!caseSensitiveCheckBox.isSelected()) {
                results = linguaKWIC.getTextSearch().searchByLemm(searchWord.getText());
                showAlignedListsIgnoreCase(Sentences, lemmas, posTags, results);
            }
            if (wordPOSTagCheckBox.isSelected()&&!caseSensitiveCheckBox.isSelected()) {
                results = linguaKWIC.getTextSearch().searchByTag(searchWord.getText());
                showAlignedListsIgnoreCase(Sentences, lemmas, posTags, results);
            }
            if (exactWordCheckBox.isSelected()&&!caseSensitiveCheckBox.isSelected()) {
                results = linguaKWIC.getTextSearch().searchByToken(searchWord.getText());
                showAlignedListsIgnoreCase(Sentences, lemmas, posTags, results);
            }


        }
    }


    //KAI new change
    public static boolean isValidURL(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    //KAI new change
    public static void showAlignedLists(java.util.List<java.util.List<String>> l1, java.util.List<java.util.List<String>> l2, java.util.List<java.util.List<String>> l3, List<TextSearch.Pair> indices) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < l1.size(); i++) {

            String element1 = String.join(" ", l1.get(i));
            String element2 = String.join(" ", l2.get(i));
            String element3 = String.join(" ", l3.get(i));

            // Append elements with line breaks after each set
            text.append(element1).append("\n");
            text.append(element2).append("\n");
            text.append(element3).append("\n\n");
        }

        result.setText(text.toString());

        // Highlight the specific elements at indices i1 and i2
        for (TextSearch.Pair indexPair : indices) {
            int i2 = indexPair.getTokenIndex();
            int i1 = indexPair.getSentenceIndex();

            if (i1 < l1.size() && i2 < l1.get(i1).size()) {
                String word1 = l1.get(i1).get(i2);
                String word2 = l2.get(i1).get(i2);
                String word3 = l3.get(i1).get(i2);
                highlightWord(word1, Color.pink);
                highlightWord(word2, Color.pink);
                highlightWord(word3, Color.pink);
            }
        }
    }

    public static void showAlignedListsIgnoreCase(java.util.List<java.util.List<String>> l1, java.util.List<java.util.List<String>> l2, java.util.List<java.util.List<String>> l3, List<TextSearch.Pair> indices) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < l1.size(); i++) {

            String element1 = String.join(" ", l1.get(i));
            String element2 = String.join(" ", l2.get(i));
            String element3 = String.join(" ", l3.get(i));

            // Append elements with line breaks after each set
            text.append(element1).append("\n");
            text.append(element2).append("\n");
            text.append(element3).append("\n\n");
        }

        result.setText(text.toString());

        // Highlight the specific elements at indices i1 and i2
        for (TextSearch.Pair indexPair : indices) {
            int i2 = indexPair.getTokenIndex();
            int i1 = indexPair.getSentenceIndex();

            if (i1 < l1.size() && i2 < l1.get(i1).size()) {
                String word1 = l1.get(i1).get(i2);
                String word2 = l2.get(i1).get(i2);
                String word3 = l3.get(i1).get(i2);
                highlightWord(word1, Color.pink);
                highlightWord(word2, Color.pink);
                highlightWord(word3, Color.pink);

                highlightWord(word1.toUpperCase(), Color.pink);
                highlightWord(word2.toUpperCase(), Color.pink);
                highlightWord(word3.toUpperCase(), Color.pink);

                highlightWord(word1.toLowerCase(), Color.pink);
                highlightWord(word2.toLowerCase(), Color.pink);
                highlightWord(word3.toLowerCase(), Color.pink);

                highlightWord(word1.substring(0,1).toUpperCase() + word1.substring(1), Color.pink);
                highlightWord(word2.substring(0,1).toUpperCase() + word2.substring(1), Color.pink);
                highlightWord(word3.substring(0,1).toUpperCase() + word3.substring(1), Color.pink);
            }
        }
    }


    //KAI new change
    private static void highlightWord(String word, Color color) {
        String text = result.getText();
        int index = text.indexOf(word);

        while (index >= 0) {
            try {
                Highlighter highlighter = result.getHighlighter();
                highlighter.addHighlight(index, index + word.length(), new DefaultHighlighter.DefaultHighlightPainter(color));
                index = text.indexOf(word, index + word.length());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LinguaKWICGUI();
            }
        });
    }
}
