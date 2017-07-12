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

    public Assignment(String title, int complete, Date dueDate) {//Convenience constructor should be removed post-testing
        this.title = title;
        this.complete = complete;
        this.dueDate = dueDate;
    }

    public int getComplete() {
        return complete;
    }

   public String getTitle() {
       return title;
   }

   public String getDateString() {
       return dateFormat.format(dueDate);
   }
}
