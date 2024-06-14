package de.uni.tuebingen.sfs.java2;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LinguaKWICGUI extends JFrame {
    // Constructor
    public LinguaKWICGUI() {
        setTitle("Lingua KWIC");
        //the size
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        getContentPane().setBackground(new Color(230, 230, 250));
        initializeComponents();
        Font customFont = new Font("Serif", Font.PLAIN, 14); ;
        JLabel fileLabel = new JLabel("File:");
        fileLabel.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(fileLabel, gbc);

        JTextField fileField = new JTextField(30);
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

        browseButton.setIcon(new ImageIcon("/Users/hooray/Desktop/Lab/K-word/IMG_0190.PNG"));
        add(browseButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        JCheckBox exactWordCheckBox = new JCheckBox("Exact word");
        exactWordCheckBox.setFont(customFont);
        add(exactWordCheckBox, gbc);

        JCheckBox wordLemmaCheckBox = new JCheckBox("Word lemma");
        wordLemmaCheckBox.setFont(customFont);
        gbc.gridx = 1;
        add(wordLemmaCheckBox, gbc);

        JCheckBox wordPOSTagCheckBox = new JCheckBox("Word POS Tag");
        wordPOSTagCheckBox.setFont(customFont);
        gbc.gridx = 2;
        add(wordPOSTagCheckBox, gbc);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(customFont);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        // 设置按钮图标
        searchButton.setIcon(new ImageIcon("/Users/hooray/Desktop/Lab/K-word/IMG_0192.PNG"));
        add(searchButton, gbc);

        // 结果显示部分
        JTextArea searchResults = new JTextArea();
        searchResults.setFont(customFont);
        JScrollPane scrollPane = new JScrollPane(searchResults);
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

        JTextField neighborsField1 = new JTextField(2);
        neighborsField1.setFont(customFont);
        gbc.gridx = 1;
        add(neighborsField1, gbc);

        /*JLabel toLabel = new JLabel("to");
        toLabel.setFont(customFont);
        gbc.gridx = 2;
        add(toLabel, gbc);*/

        JTextField neighborsField2 = new JTextField(2);
        neighborsField2.setFont(customFont);
        gbc.gridx = 3;
        add(neighborsField2, gbc);

        JCheckBox caseSensitiveCheckBox = new JCheckBox("Case Sensitive");
        caseSensitiveCheckBox.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        add(caseSensitiveCheckBox, gbc);

        JTextArea recentSearches = new JTextArea();
        recentSearches.setFont(customFont);
        JScrollPane recentScrollPane = new JScrollPane(recentSearches);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(recentScrollPane, gbc);

        JTextArea textStatistics = new JTextArea();
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

    private void initializeComponents() {


    }

    // Main method to create and display the GUI
    public static void main(String[] args) {
        LinguaKWICGUI gui = new LinguaKWICGUI();
        gui.setVisible(true);
    }
}