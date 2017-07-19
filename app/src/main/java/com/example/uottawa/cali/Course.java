package com.example.uottawa.cali;

import java.io.Serializable;

public class Course implements Serializable{
    private static final Course unsetCourse = new Course("Unset", R.color.courseColor0, R.color.courseColor0a);
    private String name;
    private int colorIndex;
    private int colorInverseIndex;

    public Course (String name, int colorIndex, int colorInverseIndex) {
        this.name = name;
        this.colorIndex = colorIndex;
        this.colorInverseIndex = colorInverseIndex;
    }
    public static Course getUnsetCourse() {
        return unsetCourse;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public void setColorInverseIndex(int colorInverseIndex) {
        this.colorInverseIndex = colorInverseIndex;
    }
}
