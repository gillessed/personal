package com.gillessed.languagetool;

import java.io.File;

import javax.swing.SwingUtilities;

import com.gillessed.languagetool.io.SkritterCsvReader;
import com.gillessed.languagetool.model.WordList;
import com.gillessed.languagetool.view.Window;

public class LanguageToolMain {


    public static void main(String args[]) {
        WordList wordList = new WordList();
        final Window window = new Window(wordList);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                window.show();
            }
        });
        File resource = new File("resources");
        if(resource.exists()) {
            for(File child : resource.listFiles()) {
                wordList.addSource(new SkritterCsvReader(child, child.getName()));
            }
        }
        wordList.read();
        window.getPanel().startSlides();
    }
}
