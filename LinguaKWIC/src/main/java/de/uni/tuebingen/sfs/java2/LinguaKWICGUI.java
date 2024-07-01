//it failed to load any pics or font....AAAAAAAAAAAA
//the good news here is that the layouts can really move freely now
package de.uni.tuebingen.sfs.java2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LinguaKWICGUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Font customFont;

    public LinguaKWICGUI() {
        setTitle("Lingua KWIC ");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        loadCustomFont();
        getContentPane().setBackground(new Color(163, 163, 215));

        initializeComponents();

        setVisible(true);
    }

    private void loadCustomFont() {
        try {
            // Adjust the path to your custom font file
            customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/LanaPixel.ttf")).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.PLAIN, 14); // Fallback to Serif if custom font loading fails (like always
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

        JTextField fileField = new JTextField(40);
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

        JCheckBox exactWordCheckBox = new JCheckBox("Exact word");
        exactWordCheckBox.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
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
        gbc.gridx = 4;
        try {
            Image img2 = ImageIO.read(getClass().getResource("/resources/IMG_0192.PNG"));
            searchButton.setIcon(new ImageIcon(img2));
        } catch (Exception e) {
            System.out.println(e);
        }
        add(searchButton, gbc);

        JTextArea searchResults = new JTextArea();
        searchResults.setFont(customFont);
        JScrollPane scrollPane = new JScrollPane(searchResults);
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LinguaKWICGUI();
            }
        });
    }
}
