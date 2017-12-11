package com.example.heatherlogan.songle;

import java.util.Comparator;
/**
 * Word objects
 */

public class WordInfo {

    private String word;
    private int lineNo;
    private int posNo;

    public WordInfo(){

    }

    public WordInfo (String word, int lineNo, int posNo){
        this.word = word;
        this.lineNo = lineNo;
        this.posNo = posNo;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getLine() {
        return lineNo;
    }

    public void setLine(int lineNo) {
        this.lineNo = lineNo;
    }

    public int getPos() {
        return posNo;
    }

    public void setPos(int posNo) {
        this.posNo = posNo;
    }


    public static Comparator<WordInfo> WordComparator = new Comparator<WordInfo>(){

        public int compare(WordInfo w1, WordInfo w2){
            int lineNo1 = w1.getLine();
            int lineNo2 = w2.getLine();

            int posNo1 = w1.getPos();
            int posNo2 = w2.getPos();

            if (lineNo1 == lineNo2){
                return posNo1-posNo2;
            } else {
                return lineNo1 - lineNo2;
            }
        }
    };
}
