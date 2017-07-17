package com.example.uottawa.cali;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Iterator;

public class CourseActivity extends AppCompatActivity implements EditNameDialogListener{

    private boolean isNew, isEditing, isTheSame;
    private Course course, oldCourse;
    private int colorId, indexOfOldCourse=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        //Add a shadow to the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        toolbar.setElevation(10);

        Intent intent = getIntent();
        isNew = intent.getExtras().getBoolean("isNewCourse");
        if(isNew) {
            colorId = R.color.colorPrimaryDark;
            isTheSame = false;
            switchToEdit();

        } else {
            populateFields();
        }
    }

    public void switchToEdit() {
        createColorOptions();
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        switcher.showNext(); //or switcher.showPrevious();
        ViewSwitcher courseToolbarSwitcher = (ViewSwitcher) findViewById(R.id.course_toolbar_switcher);
        courseToolbarSwitcher.showNext();
        ImageButton saveButton = (ImageButton) findViewById(R.id.saveCourseBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText courseName = (EditText) findViewById(R.id.editCourseTitle);
                if(courseName.getText().length() == 0) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(CourseActivity.this);
                    alert.setMessage("Course name cannot be empty")
                            .setPositiveButton(R.string.ok_filter_dialog, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).show();
                }
                else {
                    course = new Course(courseName.getText().toString(), colorId, colorId);
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.intent_course_operation), FileOperations.MODIFY);
                    intent.putExtra(getString(R.string.intent_course_data_receive), course);
                    if (isEditing) {
                        indexOfOldCourse = getIntent().getExtras().getInt("oldCourseIndex");
                        intent.putExtra("oldCourseIndex", indexOfOldCourse);
                        intent.putExtra("oldCourse", oldCourse);
                        intent.putExtra("shouldEdit", true);
                    } else {
                        ArrayList<Course> courses = (ArrayList<Course>) getIntent().getExtras().getSerializable("courseArrayList");
                        Iterator<Course> iter = courses.iterator();
                        while(iter.hasNext()) {
                            Course currentCourse = iter.next();
                            if((currentCourse.getName().equals(course.getName())) && (currentCourse.getColorIndex() == course.getColorIndex())) {
                                isTheSame = true;
                            }
                        }
                        if(isTheSame) {
                            final AlertDialog.Builder alert = new AlertDialog.Builder(CourseActivity.this);
                            alert.setMessage("Already a course with the same name\n" + "(Course name has to be unique)")
                                    .setPositiveButton(R.string.ok_filter_dialog, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    }).show();
                        } else {
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
            }
        });
    }

    public void populateFields() {
        TextView courseTitleText = (TextView) findViewById(R.id.courseTitle);
        CardView colorCircle = (CardView) findViewById(R.id.colorCircle);
        course = (Course) getIntent().getExtras().getSerializable("course");
        colorCircle.setBackgroundTintList(ResourcesCompat.getColorStateList(this.getResources(), course.getColorIndex(), null));
        colorId = course.getColorIndex();
        courseTitleText.setText(course.getName());
        oldCourse = course;
        ImageButton editButton = (ImageButton) findViewById(R.id.editCourseBtn);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditing = true;
                switchToEdit();
            }
        });
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
        colorCircle.setBackgroundTintList(ResourcesCompat.getColorStateList(this.getResources(), colorId, null));
    }

    @Override
    public void onBackPressed() {

        if(isNew || isEditing) {
            EditText currentName = (EditText) findViewById(R.id.editCourseTitle);
            if(currentName.getText().length() > 0) {
                AlertDialog.Builder confirmation = new AlertDialog.Builder(CourseActivity.this);
                confirmation.setMessage("Are you sure you want to leave unsaved changes?")
                        .setPositiveButton(R.string.ok_filter_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.negative_delete_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        }else {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.intent_course_operation), FileOperations.UNCHANGED);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
