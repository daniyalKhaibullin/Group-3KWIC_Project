package de.uni.tuebingen.sfs.java2;

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


public class GUIfromKAI extends JFrame {
    //temporal fileSaver
    File file = null;
    JTextField fileField;
    JTextField neighborsField1;
    JTextField neighborsField2;
    JTextArea textStatistics;
    JTextArea searchWord;
    static JTextArea result;
    JCheckBox exactWordCheckBox;
    JCheckBox wordLemmaCheckBox;
    JCheckBox wordPOSTagCheckBox;


    // Constructor
    public GUIfromKAI() {
        setTitle("Lingua KWIC ");
        //the size
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        getContentPane().setBackground(new Color(230, 230, 250));
        Font customFont = new Font("Serif", Font.PLAIN, 14);
        JLabel fileLabel = new JLabel("File:");
        fileLabel.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(fileLabel, gbc);

        fileField = new JTextField(30);
        fileField.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(fileField, gbc);


        JButton browseButton = new JButton("Browse");
        browseButton.setFont(customFont);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        browseButton.addActionListener(new browseButtonHandler());


        browseButton.setIcon(new ImageIcon("/Users/hooray/Desktop/Lab/K-word/IMG_0190.PNG"));
        add(browseButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        exactWordCheckBox = new JCheckBox("Exact word");
        exactWordCheckBox.setFont(customFont);
        add(exactWordCheckBox, gbc);
        exactWordCheckBox.addItemListener(new exactWordCheckBoxHandler());

        //KAI new change
        wordLemmaCheckBox = new JCheckBox("Word lemma");
        wordLemmaCheckBox.setFont(customFont);
        gbc.gridx = 1;
        add(wordLemmaCheckBox, gbc);
        wordLemmaCheckBox.addItemListener(new wordLemmaCheckBoxHandler());

        wordPOSTagCheckBox = new JCheckBox("Word POS Tag");
        wordPOSTagCheckBox.setFont(customFont);
        gbc.gridx = 2;
        add(wordPOSTagCheckBox, gbc);
        wordPOSTagCheckBox.addItemListener(new wordPOSTagCheckBoxHandler());

        JButton searchButton = new JButton("Search");
        searchButton.setFont(customFont);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        // 设置按钮图标
        searchButton.setIcon(new ImageIcon("/Users/hooray/Desktop/Lab/K-word/IMG_0192.PNG"));
        add(searchButton, gbc);
        searchButton.addActionListener(new searchButtonHandler());

        // 结果显示部分
        searchWord = new JTextArea();
        searchWord.setFont(customFont);
        JScrollPane scrollPane = new JScrollPane(searchWord);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;

        JRadioButton wholeSentenceRadioButton = new JRadioButton("Whole Sentence");
        wholeSentenceRadioButton.setFont(customFont);
        add(wholeSentenceRadioButton, gbc);

        JRadioButton neighborRadioButton = new JRadioButton("Neighbor");
        neighborRadioButton.setFont(customFont);
        gbc.gridy = 6;
        add(neighborRadioButton, gbc);

        neighborsField1 = new JTextField(2);
        neighborsField1.setFont(customFont);
        gbc.gridx = 1;
        add(neighborsField1, gbc);

        /*JLabel toLabel = new JLabel("to");
        toLabel.setFont(customFont);
        gbc.gridx = 2;
        add(toLabel, gbc);*/

        neighborsField2 = new JTextField(2);
        neighborsField2.setFont(customFont);
        gbc.gridx = 3;
        add(neighborsField2, gbc);

        JCheckBox caseSensitiveCheckBox = new JCheckBox("Case Sensitive");
        caseSensitiveCheckBox.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        add(caseSensitiveCheckBox, gbc);

        //KAI new change
        result = new JTextArea();
        result.setEditable(false);
//        result.setFont(customFont);
        result.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane recentScrollPane = new JScrollPane(result);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(recentScrollPane, gbc);

        textStatistics = new JTextArea();
        textStatistics.setFont(customFont);
        JScrollPane statsScrollPane = new JScrollPane(textStatistics);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        add(statsScrollPane, gbc);

        JButton saveToXMLButton = new JButton("Save to XML");
        saveToXMLButton.setFont(customFont);
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        saveToXMLButton.setIcon(new ImageIcon("/Users/hooray/Desktop/Lab/K-word/IMG_0195.PNG"));
        add(saveToXMLButton, gbc);

        setVisible(true);

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


            List<List<String>> Sentences = linguaKWIC.getTokens();
            List<List<String>> lemmas = linguaKWIC.getLemmas();
            List<List<String>> posTags = linguaKWIC.getPosTags();
//            showAlignedLists(Sentences, lemmas, posTags);
            
            if (wordLemmaCheckBox.isSelected()) {
                List<TextSearch.Pair> results = linguaKWIC.getTextSearch().searchByLemm(searchWord.getText());
                showAlignedLists(Sentences, lemmas, posTags, results);
            }
            if (wordPOSTagCheckBox.isSelected()) {
                List<TextSearch.Pair> results = linguaKWIC.getTextSearch().searchByTag(searchWord.getText());
                showAlignedLists(Sentences, lemmas, posTags, results);
            }
            if (exactWordCheckBox.isSelected()) {
                List<TextSearch.Pair> results = linguaKWIC.getTextSearch().searchByToken(searchWord.getText());
                showAlignedLists(Sentences, lemmas, posTags, results);
            }


            //run the search from the LingualKWIC
//            LinguaKWIC linguaKWIC = new LinguaKWIC(file);
            neighborsField1.setText("hi I'm neighborsField1");
            neighborsField2.setText("hi I'm neighborsField2");


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
    public static void showAlignedLists(List<List<String>> l1, List<List<String>> l2, List<List<String>> l3, List<TextSearch.Pair> indices) {
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


    // Main method to create and display the GUI
    public static void main(String[] args) {
        GUIfromKAI gui = new GUIfromKAI();
        gui.setVisible(true);
    }
}