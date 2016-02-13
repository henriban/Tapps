package com.tapps.henrikanderson.tapps;

import android.widget.Button;

/**
 * Created by henrik on 08.02.2016.
 */
public class ButtonClass {

    private int index;
    private String color;
    private Button button;
    private boolean correct;

    public ButtonClass(int index, Button button) {
        this.index = index;
        //this.color = color;
        this.button = button;
    }

    public int getIndex() {
        return index;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
