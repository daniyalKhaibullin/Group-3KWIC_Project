package de.uni.tuebingen.sfs.java2;

import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Setter
public class RoundedTextField extends JTextField {
    private final int arcWidth = 15;
    private final int arcHeight = 15;

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
}