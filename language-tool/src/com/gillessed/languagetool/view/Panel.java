package com.gillessed.languagetool.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import com.gillessed.languagetool.model.Word;
import com.gillessed.languagetool.model.WordList;

public class Panel extends JPanel {
    private static enum State {
        SHOWING_WORD,
        SHOWING_ANSWER;
    }
    private static final long serialVersionUID = 1L;

    private static final Font SMALL = new Font("helvetica", Font.PLAIN, 16);
    private static final Font MEDIUM = new Font("helvetica", Font.PLAIN, 20);
    private static final Font LARGE = new Font("helvetica", Font.PLAIN, 48);

    private static int ANSWER_DELAY = 4;
    private static int SHOW_DELAY = 7;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final WordList wordList;
    private final Random random;
    private Word currentWord;
    private State state;
    private boolean empty;

    public Panel(WordList wordList) {
        this.wordList = wordList;
        state = State.SHOWING_WORD;
        random = new Random();
        empty = false;
    }

    @Override
    protected void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        if(empty) {
            g.setFont(MEDIUM);
            drawStringInMiddle(g, "Word list is empty. Please create a list.", getWidth() / 2, getHeight() / 2);
        } else if(!wordList.isReady() || currentWord == null) {
            g.setFont(MEDIUM);
            drawStringInMiddle(g, "Word List is loading...", getWidth() / 2, getHeight() / 2);
        } else {
            g.setFont(LARGE);
            drawStringInMiddle(g, currentWord.getText(), getWidth() / 2, getHeight() / 2 - 60);
            g.setFont(SMALL);
            g.drawString(currentWord.getLanguage(), 2, 18);
            g.setFont(MEDIUM);
            if(state == State.SHOWING_ANSWER) {
                if(currentWord.getReading().isPresent()) {
                    drawStringInMiddle(g, currentWord.getReading().get(), getWidth() / 2, getHeight() / 2 - 20);
                }
                drawParagraph(g, currentWord.getDefinition(), 20, getHeight() / 2, getWidth() - 40);
            }
        }
    }

    private void drawStringInMiddle(Graphics g, String text, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g.drawString(text, x - width / 2, y - height / 2);
    }

    private void drawParagraph(Graphics g, String text, int left, int top, int width) {
        FontMetrics fm = g.getFontMetrics();
        List<String> lines = new ArrayList<>();
        Stack<String> words = new Stack<>();
        String[] wordArray = text.split(" ");
        for(int i = wordArray.length - 1; i >= 0 ; i--) {
            if(fm.stringWidth(wordArray[i]) > width) {
                throw new IllegalStateException("Need to increase width to draw: " + wordArray[i]);
            }
            words.add(wordArray[i]);
        }
        while(!words.isEmpty()) {
            String line = "";
            while(!words.isEmpty() && fm.stringWidth(line + " " + words.peek()) < width) {
                line += " " + words.pop();
            }
            lines.add(line);
        }
        int height = top;
        for(String line : lines) {
            g.drawString(line, left, height);
            height += fm.getHeight();
        }
    }

    public void transitionState() {
        if(state == State.SHOWING_WORD) {
            state = State.SHOWING_ANSWER;
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    transitionState();
                }
            }, ANSWER_DELAY, TimeUnit.SECONDS);
        } else {
            state = State.SHOWING_WORD;
            currentWord = wordList.getWordList().get(random.nextInt(wordList.getWordList().size()));
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    transitionState();
                }
            }, SHOW_DELAY, TimeUnit.SECONDS);
        }
        repaint();
    }

    public void startSlides() {
        if(wordList.getWordList().isEmpty()) {
            empty = true;
        } else {
            currentWord = wordList.getWordList().get(random.nextInt(wordList.getWordList().size()));
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    transitionState();
                }
            }, SHOW_DELAY, TimeUnit.SECONDS);
        }
    }
}
