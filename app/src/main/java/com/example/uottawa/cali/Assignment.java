package com.example.uottawa.cali;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class Assignment implements Serializable, Comparable<Assignment> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d", Locale.getDefault());
    private Course course = Course.getUnsetCourse();
    private String name = "";
    private int complete = 0;
    private Date dueDate = new Date();
    private int priority = 3;
    private AssignmentTypes type = AssignmentTypes.UNSET;
    private String description = "";
    private LinkedList<NamedUri> attachmentList = new LinkedList<>();
    private LinkedList<NamedLink> namedLinkList = new LinkedList<>();

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
        this.attachmentList = new LinkedList<>(original.getAttachmentList());
        this.namedLinkList = new LinkedList<>(original.getNamedLinkList());
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

    public LinkedList<NamedUri> getAttachmentList() {
        return this.attachmentList;
    }

    public LinkedList<NamedLink> getNamedLinkList() {
        return this.namedLinkList;
    }

   public boolean equals(Assignment other) {
       return
               this.course == other.getCourse() &&
               this.name.equals(other.getName()) &&
               this.complete == other.getComplete() &&
               this.dueDate.equals(other.dueDate) &&
               this.priority == other.getPriority() &&
               this.type == other.getType() &&
               this.description.equals(other.getDescription()) &&
               this.attachmentList.equals(other.getAttachmentList()) &&
               this.namedLinkList.equals(other.getNamedLinkList());
   }

    @Override
    public int compareTo(@NonNull Assignment o) {
        return Double.valueOf(rank).compareTo(o.getRank());
    }

    public String getNotificationString() {
        Calendar tCalendar = Calendar.getInstance();
        tCalendar.setTime(this.dueDate);
        String month = "January";
        switch (tCalendar.get(Calendar.MONTH)) {
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name+" ");
        stringBuilder.append("Due on: "+month);
        stringBuilder.append(" "+tCalendar.get(Calendar.DAY_OF_MONTH));
        stringBuilder.append(" at "+tCalendar.get(Calendar.HOUR));
        stringBuilder.append(":"+tCalendar.get(Calendar.MINUTE));
        return stringBuilder.toString();
    }
}
