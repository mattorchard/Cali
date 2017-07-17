package com.example.uottawa.cali;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Assignment implements Serializable, Comparable<Assignment> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d", Locale.getDefault());
    private static final Course unsetCourse = new Course("Unset", R.color.courseColor0, R.color.courseColor0a);
    private Course course = unsetCourse;
    private String name = "";
    private int complete = 0;
    private Date dueDate = new Date();
    private int priority = 3;
    private AssignmentTypes type = AssignmentTypes.UNSET;
    private String description = "";

    private boolean visible = true;
    private double rank;

    public Assignment(Course course, String name, int complete, Date dueDate, int priority, AssignmentTypes type, String description) {
        this.course = course;
        this.name = name;
        this.complete = complete;
        this.dueDate = dueDate;
        this.priority = priority;
        this.type = type;
        this.description = description;
    }
    public Assignment(Assignment original) {
        this.course = original.getCourse();
        this.name = original.getName();
        this.complete = original.getComplete();
        this.dueDate = original.getDueDate();
        this.priority = original.getPriority();
        this.type = original.getType();
        this.description = original.getDescription();
    }
    public Assignment() {}

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public AssignmentTypes getType() {
        return type;
    }

    public void setType(AssignmentTypes type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateString() {
       return dateFormat.format(dueDate);
   }

   public boolean equals(Assignment other) {
       return
               this.course == other.getCourse() &&
               this.name.equals(other.getName()) &&
               this.complete == other.getComplete() &&
               this.dueDate.equals(other.dueDate) &&
               this.priority == other.getPriority() &&
               this.type == other.getType() &&
               this.description.equals(other.getDescription());
   }

    @Override
    public int compareTo(@NonNull Assignment o) {
        return Double.valueOf(rank).compareTo(o.getRank());
    }
}
