package com.example.heatherlogan.songle;

/**
 * Created by heatherlogan on 03/11/2017.
 */

public class WordInfo {

    private String word;
    private int lineNo;
    private int posNo;

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

}
