package de.uni.tuebingen.sfs.java2;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class LeaNewNewNewGUI {
    private JButton serchButton;
    private JButton saveButton;
    private JTextArea youGonnaSeeTextArea;
    private JPanel rootPanel;
    private JTextField serchField;
    private JComboBox<String> comboBox1;
    private JRadioButton exactWordRadioButton;
    private JRadioButton wordLemmaRadioButton;
    private JRadioButton wordPOSTagRadioButton;
    private JTextField exactWordField;
    private JTextField wordLemmaField;
    private JTextField wordPOSTagField;
    private JTextArea bottomTextArea;
    private JTextArea leftTextArea;
    private JRadioButton wholeSentenceRadioButton;
    private JRadioButton neighborRadioButton;
    private JRadioButton caseSensitiveRadioButton;
    private JSlider neighborSlider;

    public LeaNewNewNewGUI() {
        // Initialize components
        serchButton = new JButton("SEARCH");
        saveButton = new JButton("SAVE TO XML");
        youGonnaSeeTextArea = new JTextArea();
        serchField = new JTextField();
        comboBox1 = new JComboBox<>(new String[]{"URL", "File"});
        exactWordRadioButton = new JRadioButton("EXACT WORD");
        wordLemmaRadioButton = new JRadioButton("WORD LEMMA");
        wordPOSTagRadioButton = new JRadioButton("WORD POS TAG");
        exactWordField = new JTextField(3);
        wordLemmaField = new JTextField(3);
        wordPOSTagField = new JTextField(3);
        bottomTextArea = new JTextArea();
        leftTextArea = new JTextArea();
        wholeSentenceRadioButton = new JRadioButton("WHOLE SENTENCE");
        neighborRadioButton = new JRadioButton("NEIGHBOR");
        caseSensitiveRadioButton = new JRadioButton("CASE SENSITIVE");
        neighborSlider = new JSlider();

        // Set fonts and colors
        Font font = new Font("Phosphate", Font.PLAIN, 15);
        Color fontColor = new Color(72, 78, 87);
        Color whiteColor = Color.WHITE;

        serchButton.setFont(font);
        serchButton.setForeground(fontColor);
        serchButton.setBackground(new Color(153, 153, 189));

        saveButton.setFont(font);
        saveButton.setForeground(fontColor);
        saveButton.setBackground(new Color(165, 109, 152));

        youGonnaSeeTextArea.setFont(font);
        youGonnaSeeTextArea.setForeground(whiteColor);
        youGonnaSeeTextArea.setBackground(new Color(84, 133, 32));
        youGonnaSeeTextArea.setEditable(false);

        serchField.setFont(font);
        serchField.setForeground(fontColor);
        serchField.setBackground(new Color(170, 178, 141));

        comboBox1.setFont(font);
        comboBox1.setForeground(fontColor);
        comboBox1.setBackground(new Color(87, 71, 97));

        exactWordRadioButton.setFont(font);
        exactWordRadioButton.setForeground(whiteColor);
        exactWordRadioButton.setBackground(new Color(84, 133, 32));

        wordLemmaRadioButton.setFont(font);
        wordLemmaRadioButton.setForeground(whiteColor);
        wordLemmaRadioButton.setBackground(new Color(84, 133, 32));

        wordPOSTagRadioButton.setFont(font);
        wordPOSTagRadioButton.setForeground(whiteColor);
        wordPOSTagRadioButton.setBackground(new Color(84, 133, 32));

        exactWordField.setFont(font);
        exactWordField.setForeground(whiteColor);
        exactWordField.setBackground(new Color(72, 78, 87));

        wordLemmaField.setFont(font);
        wordLemmaField.setForeground(whiteColor);
        wordLemmaField.setBackground(new Color(72, 78, 87));

        wordPOSTagField.setFont(font);
        wordPOSTagField.setForeground(whiteColor);
        wordPOSTagField.setBackground(new Color(72, 78, 87));

        bottomTextArea.setFont(font);
        bottomTextArea.setForeground(whiteColor);
        bottomTextArea.setBackground(new Color(89,123,94));

        leftTextArea.setFont(font);
        leftTextArea.setForeground(whiteColor);
        leftTextArea.setBackground(new Color(83,74,87));
        Border border = BorderFactory.createLineBorder(Color.WHITE); // Add a white border
        leftTextArea.setBorder(border);

        wholeSentenceRadioButton.setFont(font);
        wholeSentenceRadioButton.setForeground(whiteColor);
        wholeSentenceRadioButton.setBackground(new Color(84, 133, 32));

        neighborRadioButton.setFont(font);
        neighborRadioButton.setForeground(whiteColor);
        neighborRadioButton.setBackground(new Color(84, 133, 32));

        caseSensitiveRadioButton.setFont(font);
        caseSensitiveRadioButton.setForeground(whiteColor);
        caseSensitiveRadioButton.setBackground(new Color(84, 133, 32));

        neighborSlider.setBackground(new Color(153, 153, 189));

        // Set layout manager for rootPanel
        rootPanel = new JPanel();
        rootPanel.setLayout(null);  // Absolute positioning
        rootPanel.setBackground(new Color(153, 153, 189));

        // Set bounds and add components
        serchField.setBounds(10, 10, 200, 30);
        comboBox1.setBounds(220, 10, 100, 30);
        serchButton.setBounds(330, 10, 100, 30);
        saveButton.setBounds(900, 750, 200, 30);
        exactWordRadioButton.setBounds(10, 50, 200, 30);
        exactWordField.setBounds(200, 50, 50, 30);
        wordLemmaRadioButton.setBounds(10, 90, 200, 30);
        wordLemmaField.setBounds(200, 90, 50, 30);
        wordPOSTagRadioButton.setBounds(10, 130, 200, 30);
        wordPOSTagField.setBounds(200, 130, 50, 30);
        youGonnaSeeTextArea.setBounds(260, 50, 920, 500);

        bottomTextArea.setBounds(10, 170, 235, 400);
        leftTextArea.setBounds(300, 50, 800, 680);
        wholeSentenceRadioButton.setBounds(10, 580, 200, 30);
        neighborRadioButton.setBounds(10, 620, 200, 30);
        neighborSlider.setBounds(10, 660, 200, 30);
        caseSensitiveRadioButton.setBounds(10, 700, 200, 30);

        rootPanel.add(serchField);
        rootPanel.add(comboBox1);
        rootPanel.add(serchButton);
        rootPanel.add(saveButton);
        rootPanel.add(exactWordRadioButton);
        rootPanel.add(exactWordField);
        rootPanel.add(wordLemmaRadioButton);
        rootPanel.add(wordLemmaField);
        rootPanel.add(wordPOSTagRadioButton);
        rootPanel.add(wordPOSTagField);
        //rootPanel.add(new JScrollPane(youGonnaSeeTextArea));
        rootPanel.add(bottomTextArea);
        rootPanel.add(leftTextArea);
        rootPanel.add(wholeSentenceRadioButton);
        rootPanel.add(neighborRadioButton);
        rootPanel.add(neighborSlider);
        rootPanel.add(caseSensitiveRadioButton);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Search whatever you want");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new LeaNewNewNewGUI().rootPanel);
        frame.setSize(1200, 1000);
        frame.setVisible(true);
    }
}
