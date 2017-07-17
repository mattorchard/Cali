package com.example.uottawa.cali;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class CourseActivity extends AppCompatActivity implements EditNameDialogListener{

    private boolean isNew;
    private Course course;
    private int colorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Intent intent = getIntent();
        isNew = intent.getExtras().getBoolean("isNewCourse");
        createColorOptions();
        if(isNew) {
            switchToEdit();

        } else {
            populateFields();
        }
    }

    public void switchToEdit() {
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        switcher.showNext(); //or switcher.showPrevious();
        ViewSwitcher courseToolbarSwitcher = (ViewSwitcher) findViewById(R.id.course_toolbar_switcher);
        courseToolbarSwitcher.showNext();
        colorId = R.color.colorPrimaryDark;
    }

    public void populateFields() {
        TextView courseTitleText = (TextView) findViewById(R.id.courseTitle);
        CardView colorCircle = (CardView) findViewById(R.id.colorCircle);
        course = (Course) getIntent().getExtras().getSerializable("course");
        colorCircle.setBackgroundTintList(this.getResources().getColorStateList(course.getColorIndex()));
        colorId = course.getColorIndex();
        courseTitleText.setText(course.getName());
    }
    public void createColorOptions() {

        CardView colorCircle = (CardView) findViewById(R.id.colorCircle);
        colorCircle.setClickable(true);
        colorCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new ColorListDialog();
                newFragment.show(getSupportFragmentManager(), "Change Color");
            }
        });

    }

    @Override
    public void onFinishEditDialog(int colorId) {
        CardView colorCircle = (CardView) findViewById(R.id.colorCircle);
        this.colorId = colorId;
        colorCircle.setBackgroundTintList(this.getResources().getColorStateList(colorId));
    }

    @Override
    public void onBackPressed() {
        if(isNew) {
            EditText courseName = (EditText) findViewById(R.id.editCourseTitle);
            course = new Course(courseName.getText().toString(), colorId, colorId);
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.intent_course_operation), FileOperations.MODIFY);
            intent.putExtra(getString(R.string.intent_course_data_receive), course);
            setResult(RESULT_OK, intent);
            finish();
        } else if(!isNew){
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.intent_course_operation), FileOperations.UNCHANGED);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            finish();
        }
    }
}
