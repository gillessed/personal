package com.gillessed.languagetool.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import com.gillessed.languagetool.model.WordList;

public class Window {

    private final JFrame frame;
    private final Panel panel;

    public Window(WordList wordList) {
        frame = new JFrame();
        frame.setLocation(0, 0);
        frame.setPreferredSize(new Dimension(500, 300));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container c = frame.getContentPane();
        c.setLayout(new GridLayout(1, 1));
        c.add((panel = new Panel(wordList)));
    }
    public void show() {
        frame.pack();
        frame.setVisible(true);
    }

    public Panel getPanel() {
        return panel;
    }
}
