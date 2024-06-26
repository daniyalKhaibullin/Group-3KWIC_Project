package de.uni.tuebingen.sfs.java2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LinguaKWICGUI extends JFrame {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 1000;
    private Font customFont;

    public LinguaKWICGUI() {
        setTitle("Lingua KWIC ");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        de.uni.tuebingen.sfs.java2.LinguaKWICGUI.BackgroundPanel backgroundPanel = new de.uni.tuebingen.sfs.java2.LinguaKWICGUI.BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        Font customFont = new Font("Serif", Font.PLAIN, 14);

        getContentPane().setBackground(new Color(230, 230, 250));

        initializeComponents();

        setVisible(true);
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

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
        try {
            Image img1 = ImageIO.read(getClass().getResource("resources/IMG_0190.PNG"));
            browseButton.setIcon(new ImageIcon(img1));
        } catch (Exception e) {
            System.out.println(e);
        }

        /*browseButton.setIcon(new ImageIcon("resources/IMG_0190.PNG"));
        add(browseButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
         */

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

        Label toLabel = new Label("to");
        toLabel.setFont(customFont);
        gbc.gridx = 2;
        add(toLabel, gbc);

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

    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(getClass().getResource("/resources/IMG_0143.PNG"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }


    // Main method to create and display the GUI
    public static void main(String[] args) {
        de.uni.tuebingen.sfs.java2.LinguaKWICGUI gui = new de.uni.tuebingen.sfs.java2.LinguaKWICGUI();
        gui.setVisible(true);
    }
}
