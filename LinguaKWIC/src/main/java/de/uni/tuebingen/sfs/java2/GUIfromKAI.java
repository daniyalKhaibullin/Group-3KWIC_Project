package de.uni.tuebingen.sfs.java2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

public class GUIfromKAI extends JFrame {
    //temporal fileSaver
    File file = null;
    JTextField fileField;
    JTextField neighborsField1;
    JTextField neighborsField2;
    JTextArea textStatistics;
    JTextArea searchWord;
    JTextArea result;
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
        initializeComponents();
        Font customFont = new Font("Serif", Font.PLAIN, 14);
        ;
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

        result = new JTextArea();
        result.setFont(customFont);
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

    private class exactWordCheckBoxHandler implements ItemListener{
        public void itemStateChanged(ItemEvent e) {
            // Check if the checkbox is selected
            if (e.getStateChange() == ItemEvent.SELECTED) {
                System.out.println("Checkbox is selected");
            } else {
                System.out.println("Checkbox is deselected");
            }
        }
    }

    private class wordLemmaCheckBoxHandler implements ItemListener{
        public void itemStateChanged(ItemEvent e) {
            // Check if the checkbox is selected
            if (e.getStateChange() == ItemEvent.SELECTED) {
                System.out.println("Checkbox is selected");
            } else {
                System.out.println("Checkbox is deselected");
            }
        }
    }

    private class wordPOSTagCheckBoxHandler implements ItemListener{
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
            //get file according to the fileField
            String filePath = fileField.getText();
            if (filePath != null && !file.exists()) {
                try {
                    file = new File(filePath);
                } catch (Exception t) {
                    t.getStackTrace();
                }

                //if exactWordCheckBox is selected, run the following search
                if(exactWordCheckBox.isSelected()){
                  //stab
                }
                //if wordLemmaCheckBox is selected, run the following search
                if(wordLemmaCheckBox.isSelected()){
                    //stab
                }
                //if wordPOSTagCheckBox is selected, run the following search
                if(wordPOSTagCheckBox.isSelected()){
                    //stab
                }

            }

            //run the search from the LingualKWIC
            LinguaKWIC linguaKWIC = new LinguaKWIC(file);
            neighborsField1.setText("hi I'm neighborsField1");
            neighborsField2.setText("hi I'm neighborsField2");
            searchWord.setText("hi I'm searchWord");
            result.setText("Hi, I'm the result");



        }
    }

    private void initializeComponents() {


    }


    // Main method to create and display the GUI
    public static void main(String[] args) {
        GUIfromKAI gui = new GUIfromKAI();
        gui.setVisible(true);
    }
}