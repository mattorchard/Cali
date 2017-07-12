package com.example.uottawa.cali;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Assignment implements Serializable {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d", Locale.getDefault());
    private String title;
    private int complete;
    private Date dueDate;
    private Course course;

    public Assignment(Course course, String title, int complete, Date dueDate) {//Convenience constructor should be removed post-testing
        this.course = course;
        this.title = title;
        this.complete = complete;
        this.dueDate = dueDate;
    }

    public int getComplete() {
        return complete;
    }

    public String getName() {
       return title;
   }

    public String getDateString() {
       return dateFormat.format(dueDate);
   }

    public Course getCourse() {
       return course;
    }
}
