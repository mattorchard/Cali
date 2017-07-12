package com.example.uottawa.cali;

import java.io.Serializable;

public class Course implements Serializable{

    private String name;
    private int colorIndex;
    private int colorInverseIndex;

    public Course (String name, int colorIndex, int colorInverseIndex) {
        this.name = name;
        this.colorIndex = colorIndex;
        this.colorInverseIndex = colorInverseIndex;
    }

    public String getName() {
        return name;
    }

    public int getColorIndex() {
        return colorIndex;
    }
    public int getColorInverseIndex() {
        return colorInverseIndex;
    }
}
