package com.example.uottawa.cali;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class AssignmentActivity extends AppCompatActivity {
    private final int[] priorityButtons = new int[]{R.id.priorityButton1Assignment, R.id.priorityButton2Assignment, R.id.priorityButton3Assignment, R.id.priorityButton4Assignment, R.id.priorityButton5Assignment};
    private boolean fresh;
    private Assignment assignment;
    private Assignment assignmentOriginal;
    private TextView courseTextView;
    private LinearLayout courseBackground;
    private EditText nameEditText;
    private TextView dueTextView;
    private TextView typeTextView;
    private EditText descriptionEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Intent intent = getIntent();
        fresh = intent.getBooleanExtra(getString(R.string.intent_assignment_fresh), false);
        assignment = (Assignment)intent.getSerializableExtra(getString(R.string.intent_assignment_data_send));
        if (null == assignment) {
            Toast.makeText(getBaseContext(), getString(R.string.error_assignment_parse), Toast.LENGTH_SHORT).show();
            finish();
        }
        assignmentOriginal = new Assignment(assignment);
        //Course bar
        courseTextView = (TextView)findViewById(R.id.courseTextViewAssignment);
        courseTextView.setText(assignment.getCourse().getName());
        courseBackground = (LinearLayout)findViewById(R.id.courseBackgroundAssignment);
        courseBackground.setBackgroundResource(assignment.getCourse().getColorIndex());
        //Assignment name

        nameEditText = (EditText)findViewById(R.id.nameEditTextAssignment);
        nameEditText.setText(assignment.getName());
        //Complete
        SeekBar completeBar = (SeekBar)findViewById(R.id.completeSeekBarAssignment);
        completeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView completeText = (TextView)findViewById(R.id.completeTextViewAssignment);
                completeText.setText(String.format("%s %s%%",getString(R.string.complete_header_assignment), progress));
                assignment.setComplete(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        completeBar.setProgress(assignment.getComplete());
        TextView completeText = (TextView)findViewById(R.id.completeTextViewAssignment);
        completeText.setText(String.format("%s %s%%",getString(R.string.complete_header_assignment), assignment.getComplete()));
        //Due
        dueTextView = (TextView)findViewById(R.id.dueTextViewAssignment);
        dueTextView.setText(assignment.getDateString());
        //Priority
        ImageButton priorityButton = (ImageButton)findViewById(priorityButtons[assignment.getPriority() - 1]);
        priorityButton.setColorFilter(ContextCompat.getColor(AssignmentActivity.this, R.color.colorAccent));
        //Type
        typeTextView = (TextView)findViewById(R.id.typeTextViewAssignment);
        typeTextView.setText(getString(assignment.getType().getNameID()));
        //Description
        descriptionEditText = (EditText)findViewById(R.id.descriptionEditTextAssignment);
        descriptionEditText.setText(assignment.getDescription());
    }

    @Override
    public void onBackPressed() {
        assignment.setName(nameEditText.getText().toString());
        assignment.setDescription(descriptionEditText.getText().toString());
        if (fresh) {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.intent_assignment_operation), FileOperations.MODIFY);
            intent.putExtra(getString(R.string.intent_assignment_data_receive), assignment);
            setResult(RESULT_OK, intent);
            finish();
        } else if (!assignment.equals(assignmentOriginal)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.title_save_dialog));
            builder.setMessage(getString(R.string.message_save_dialog));
            builder.setPositiveButton(getString(R.string.positive_save_dialog), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.intent_assignment_operation), FileOperations.MODIFY);
                    intent.putExtra(getString(R.string.intent_assignment_data_receive), assignment);
                    setResult(RESULT_OK, intent);
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton(getString(R.string.negative_save_dialog), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.intent_assignment_operation), FileOperations.UNCHANGED);
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

    public void changeCourse(View v) {
        Toast.makeText(getBaseContext(), "Course change unimplimented", Toast.LENGTH_SHORT).show();
    }

    public void addLink(View v){
        Toast.makeText(getBaseContext(), "Add link feature unimplimented", Toast.LENGTH_SHORT).show();
    }

    public void addFile(View v){
        Toast.makeText(getBaseContext(), "Add file feature unimplemented", Toast.LENGTH_SHORT).show();
    }

    public void changePriority(View v) {
        for (int i = 0; i < priorityButtons.length; i++) {
            ImageButton priorityButton = (ImageButton)findViewById(priorityButtons[i]);
            if (v.getId() == priorityButtons[i]) {
                assignment.setPriority(i + 1);
                priorityButton.setColorFilter(ContextCompat.getColor(AssignmentActivity.this, R.color.colorAccent));
            } else {
                priorityButton.setColorFilter(ContextCompat.getColor(AssignmentActivity.this, R.color.colorGray));
            }
        }
    }

    public void changeType(View v) {
        DialogFragment newFragment = new TypeSelectorDialog();
        newFragment.show(getSupportFragmentManager(), "Change Type");
    }

    public void enableEditText(View v) {
        EditText et = (EditText)v;
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }
}
