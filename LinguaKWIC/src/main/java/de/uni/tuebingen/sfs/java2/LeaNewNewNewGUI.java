package de.uni.tuebingen.sfs.java2;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.geom.RoundRectangle2D;
import java.awt.*;

public class LeaNewNewNewGUI {
    private JButton searchButton;
    private JButton saveButton;
    private JPanel rootPanel;
    private RoundedTextField searchField;
    private JComboBox<String> comboBox1;
    private JRadioButton exactWordRadioButton;
    private JRadioButton wordLemmaRadioButton;
    private JRadioButton wordPOSTagRadioButton;
    private RoundedTextField exactWordField;
    private RoundedTextField wordLemmaField;
    private RoundedTextField wordPOSTagField;
    private JTextArea bottomTextArea;
    private JTextArea leftTextArea;
    private JRadioButton wholeSentenceRadioButton;
    private JRadioButton neighborRadioButton;
    private JRadioButton caseSensitiveRadioButton;
    private JSlider neighborSlider;
    JScrollPane bottomScrollPane = new JScrollPane(bottomTextArea);
    JScrollPane leftScrollPane = new JScrollPane(leftTextArea);


    public LeaNewNewNewGUI() {
        // Initialize components
        searchButton = new JButton("SEARCH");
        saveButton = new JButton("SAVE TO XML");
        searchField = new RoundedTextField(20);
        comboBox1 = new JComboBox<>(new String[]{"URL", "File"});
        exactWordRadioButton = new JRadioButton("EXACT WORD");
        wordLemmaRadioButton = new JRadioButton("WORD LEMMA");
        wordPOSTagRadioButton = new JRadioButton("WORD POS TAG");
        exactWordField = new RoundedTextField(3);
        wordLemmaField = new RoundedTextField(3);
        wordPOSTagField = new RoundedTextField(3);
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

        Font inputfont = new Font("Papyrus", Font.BOLD, 15);
        Color fontColor2 = new Color(72, 78, 87);

        searchButton.setFont(font);
        searchButton.setForeground(fontColor);
        searchButton.setBackground(new Color(153, 153, 189));

        saveButton.setFont(font);
        saveButton.setForeground(new Color(165, 109, 152));
        saveButton.setBackground(new Color(165, 109, 152));

        searchField.setFont(font);
        searchField.setForeground(fontColor);
        searchField.setBackground(new Color(170, 178, 141));

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

        exactWordField.setFont(inputfont);
        exactWordField.setForeground(whiteColor);
        exactWordField.setBackground(new Color(192,174,182));

        wordLemmaField.setFont(inputfont);
        wordLemmaField.setForeground(whiteColor);
        wordLemmaField.setBackground(new Color(192,174,182));

        wordPOSTagField.setFont(inputfont);
        wordPOSTagField.setForeground(whiteColor);
        wordPOSTagField.setBackground(new Color(192,174,182));

        bottomTextArea.setFont(inputfont);
        bottomTextArea.setForeground(new Color(153, 153, 189));
        bottomTextArea.setBackground(new Color(89,123,94));

        leftTextArea.setFont(inputfont);
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

        JScrollPane bottomScrollPane = new JScrollPane(bottomTextArea);
        JScrollPane leftScrollPane = new JScrollPane(leftTextArea);
        bottomScrollPane.getVerticalScrollBar().setBackground(new Color(153, 153, 189));
        bottomScrollPane.getVerticalScrollBar().setForeground(new Color(165, 109, 152));
        bottomScrollPane.getHorizontalScrollBar().setBackground(new Color(153, 153, 189));
        bottomScrollPane.getHorizontalScrollBar().setForeground(new Color(165, 109, 152));


        // Set bounds and add components
        searchField.setBounds(20, 10, 200, 30);
        comboBox1.setBounds(230, 10, 100, 30);
        searchButton.setBounds(340, 10, 100, 30);
        saveButton.setBounds(900, 770, 200, 30);
        exactWordRadioButton.setBounds(30, 50, 200, 30);
        exactWordField.setBounds(190, 50, 80, 30);
        wordLemmaRadioButton.setBounds(30, 90, 200, 30);
        wordLemmaField.setBounds(190, 90, 80, 30);
        wordPOSTagRadioButton.setBounds(30, 130, 200, 30);
        wordPOSTagField.setBounds(190, 130, 80, 30);

        bottomTextArea.setBounds(30, 170, 235, 400);
        leftTextArea.setBounds(300, 50, 800, 680);
        wholeSentenceRadioButton.setBounds(30, 580, 200, 30);
        neighborRadioButton.setBounds(30, 620, 200, 30);
        neighborSlider.setBounds(30, 660, 200, 30);
        caseSensitiveRadioButton.setBounds(30, 700, 200, 30);

        bottomScrollPane.setBounds(30, 170, 235, 400);
        leftScrollPane.setBounds(300, 50, 800, 680);

        rootPanel.add(searchField);
        rootPanel.add(comboBox1);
        rootPanel.add(searchButton);
        rootPanel.add(saveButton);
        rootPanel.add(exactWordRadioButton);
        rootPanel.add(exactWordField);
        rootPanel.add(wordLemmaRadioButton);
        rootPanel.add(wordLemmaField);
        rootPanel.add(wordPOSTagRadioButton);
        rootPanel.add(wordPOSTagField);
        rootPanel.add(bottomTextArea);
        rootPanel.add(leftTextArea);
        rootPanel.add(wholeSentenceRadioButton);
        rootPanel.add(neighborRadioButton);
        rootPanel.add(neighborSlider);
        rootPanel.add(caseSensitiveRadioButton);
        rootPanel.add(bottomScrollPane);
        rootPanel.add(leftScrollPane);
    }
    class RoundedTextField extends JTextField {
        private int arcWidth = 15;
        private int arcHeight = 15;

        public RoundedTextField(int columns) {
            super(columns);
            setOpaque(false); // Make the text field non-opaque to paint background
        }
        @Override
        protected void paintComponent(Graphics g) {
            if (!isOpaque() && getBackground() != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
                g2.dispose();
            }
            super.paintComponent(g);
        }
        @Override
        protected void paintBorder(Graphics g) {
            if (!isOpaque() && getBackground() != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getForeground());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
                g2.dispose();
            }
        }
        public void setArcWidth(int arcWidth) {
            this.arcWidth = arcWidth;
            repaint();
        }

        public void setArcHeight(int arcHeight) {
            this.arcHeight = arcHeight;
            repaint();
        }
    }

    /*
    public class RoundedJTextArea extends JTextArea {
        private int arcWidth;
        private int arcHeight;

        public RoundedJTextArea(int arcWidth, int arcHeight) {
            super();
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Adjust padding if needed
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arcWidth, arcHeight));
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight));
            g2.dispose();
        }
    }*/



    public static void main(String[] args) {
        JFrame frame = new JFrame("Search whatever you want:)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new LeaNewNewNewGUI().rootPanel);
        frame.setSize(1200, 1000);
        frame.setVisible(true);
    }
}
