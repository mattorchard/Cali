package com.example.uottawa.cali;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Intent intent = getIntent();
        boolean isNew = intent.getExtras().getBoolean("isNewCourse");
        if(isNew) {
            switchToEdit();
        } else {
            populateFields();
        }
    }

    public void switchToEdit() {
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        switcher.showNext(); //or switcher.showPrevious();
    }

    public void populateFields() {
        TextView courseTitleText = (TextView) findViewById(R.id.courseTitle);
        Course course = (Course) getIntent().getExtras().getSerializable("course");
        courseTitleText.setText(course.getName());
    }
}
