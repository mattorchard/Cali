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
import java.util.HashMap;
import java.util.Iterator;

public class CourseActivity extends AppCompatActivity implements EditNameDialogListener{

    private boolean isNew, isEditing, isTheSame;
    private Course course, oldCourse;
    private int colorId, indexOfOldCourse=-1;
    private Integer secondaryColorId;
    private HashMap<Integer, Integer> colorIndexMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        createHashMap();

        //Add a shadow to the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        toolbar.setElevation(10);

        Intent intent = getIntent();
        isNew = intent.getExtras().getBoolean("isNewCourse");
        if(isNew) {
            colorId = R.color.courseColor8;
            secondaryColorId = colorIndexMap.get(colorId);
            isTheSame = false;
            switchToEdit();

        } else {
            populateFields();
        }
    }

    public void switchToEdit() {
        createColorOptions();
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
                    /*Integer test = colorIndexMap.get(colorId);
                    Toast.makeText(CourseActivity.this, Integer.toString(test), Toast.LENGTH_LONG).show();*/
                    course = new Course(courseName.getText().toString(), colorId, secondaryColorId);
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.intent_course_operation), FileOperations.MODIFY);
                    intent.putExtra(getString(R.string.intent_course_data_receive), course);
                    if (isEditing) {
                        indexOfOldCourse = getIntent().getExtras().getInt("oldCourseIndex");
                        intent.putExtra("oldCourseIndex", indexOfOldCourse);
                        intent.putExtra("shouldEdit", true);
                        setResult(RESULT_OK, intent);
                        finish();
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
        CardView colorCircle = (CardView) findViewById(R.id.colorCircle);
        course = (Course) getIntent().getExtras().getSerializable("course");
        colorCircle.setBackgroundTintList(ResourcesCompat.getColorStateList(this.getResources(), course.getColorIndex(), null));
        colorId = course.getColorIndex();
        secondaryColorId = course.getColorInverseIndex();
        oldCourse = course;
        isEditing = true;
        switchToEdit();
        EditText courseName = (EditText) findViewById(R.id.editCourseTitle);
        courseName.setText(course.getName());

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
        secondaryColorId = colorIndexMap.get(this.colorId);
        colorCircle.setBackgroundTintList(ResourcesCompat.getColorStateList(this.getResources(), colorId, null));
    }

    @Override
    public void onBackPressed() {
        isTheSame = false;
        EditText courseName = (EditText) findViewById(R.id.editCourseTitle);
        if (isEditing && (oldCourse.getName().equals(courseName.getText().toString())) && (oldCourse.getColorIndex() == colorId)) {
            finish();
        } else {
            EditText currentName = (EditText) findViewById(R.id.editCourseTitle);
            if (currentName.getText().length() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.title_save_dialog));
                builder.setMessage(getString(R.string.message_course_save_dialog));
                builder.setPositiveButton(getString(R.string.positive_save_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText courseName = (EditText) findViewById(R.id.editCourseTitle);
                        course = new Course(courseName.getText().toString(), colorId, secondaryColorId);
                        Intent intent = new Intent();
                        intent.putExtra(getString(R.string.intent_course_operation), FileOperations.MODIFY);
                        intent.putExtra(getString(R.string.intent_course_data_receive), course);
                        if (isEditing) {
                            indexOfOldCourse = getIntent().getExtras().getInt("oldCourseIndex");
                            intent.putExtra("oldCourseIndex", indexOfOldCourse);
                            intent.putExtra("oldCourse", oldCourse);
                            intent.putExtra("shouldEdit", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            ArrayList<Course> courses = (ArrayList<Course>) getIntent().getExtras().getSerializable("courseArrayList");
                            Iterator<Course> iter = courses.iterator();
                            while (iter.hasNext()) {
                                Course currentCourse = iter.next();
                                if ((currentCourse.getName().equals(course.getName())) && (currentCourse.getColorIndex() == course.getColorIndex())) {
                                    isTheSame = true;
                                }
                            }
                            if (isTheSame) {
                                final AlertDialog.Builder alert = new AlertDialog.Builder(CourseActivity.this);
                                alert.setMessage("A course with the same name already exists")
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
                });
                builder.setNeutralButton(getString(R.string.cancel_save_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(getString(R.string.negative_save_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(getString(R.string.intent_course_operation), FileOperations.UNCHANGED);
                        setResult(RESULT_OK, intent);
                        dialog.dismiss();
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.intent_assignment_operation), FileOperations.UNCHANGED);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
    public void createHashMap() {
        colorIndexMap = new HashMap<>();
        colorIndexMap.put(R.color.courseColor1, R.color.courseColor0a);
        colorIndexMap.put(R.color.courseColor5a, R.color.courseColor5);
        colorIndexMap.put(R.color.courseColor3, R.color.courseColor3a);
        colorIndexMap.put(R.color.courseColor7, R.color.courseColor7a);
        colorIndexMap.put(R.color.courseColor4, R.color.courseColor4a);
        colorIndexMap.put(R.color.courseColor2, R.color.courseColor2a);
        colorIndexMap.put(R.color.courseColor6, R.color.courseColor6a);
        colorIndexMap.put(R.color.courseColor3a, R.color.courseColor3);
        colorIndexMap.put(R.color.courseColor8, R.color.courseColor8a);
    }
}
