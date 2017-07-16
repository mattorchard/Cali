package com.example.uottawa.cali;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class IOActivity extends AppCompatActivity{

    protected ArrayList<Assignment> assignmentsFile;
    protected ArrayList<Course> coursesFile;

    protected boolean readData() {
        FileInputStream fis;
        ObjectInputStream ois;
        try {
            fis = openFileInput(getString(R.string.assignments_filename));
            ois = new ObjectInputStream(fis);
            assignmentsFile = (ArrayList<Assignment>)ois.readObject();
            ois.close();
            fis.close();
            fis = openFileInput(getString(R.string.unused_courses_filename));
            ois = new ObjectInputStream(fis);
            coursesFile = (ArrayList<Course>)ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        for (Assignment assignment : assignmentsFile) {
            if (!coursesFile.contains(assignment.getCourse())) {
                coursesFile.add(assignment.getCourse());
            }
        }
        return true;
    }
    protected boolean writeData() {
        ArrayList<Course> usedCourses = new ArrayList<>();
        for (Assignment assignment : assignmentsFile) {
            usedCourses.add(assignment.getCourse());
        }
        ArrayList<Course> unusedCourses = new ArrayList<>();
        for (Course course : coursesFile) {
            if (!usedCourses.contains(course)) {
                unusedCourses.add(course);
            }
        }
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = openFileOutput(getString(R.string.assignments_filename), Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(assignmentsFile);
            oos.close();
            fos.close();
            fos = openFileOutput(getString(R.string.unused_courses_filename), Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(unusedCourses);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
