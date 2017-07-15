package com.example.uottawa.cali;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class AssignmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        SeekBar completeBar = (SeekBar)findViewById(R.id.completeSeekBarAssignment);
        completeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView completeText = (TextView)findViewById(R.id.completeTextViewAssignment);
                completeText.setText(String.format("%s %s%%",getString(R.string.complete_header_assignment),progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
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
        int priority = 3;
        int[] buttonIDs = new int[]{R.id.priorityButton1Assignment, R.id.priorityButton2Assignment, R.id.priorityButton3Assignment, R.id.priorityButton4Assignment, R.id.priorityButton5Assignment};
        for (int i = 0; i < buttonIDs.length; i++) {
            ImageButton priorityButton = (ImageButton)findViewById(buttonIDs[i]);
            if (v.getId() == buttonIDs[i]) {
                priority = i;
                priorityButton.setColorFilter(ContextCompat.getColor(AssignmentActivity.this, R.color.colorAccent));
            } else {
                priorityButton.setColorFilter(ContextCompat.getColor(AssignmentActivity.this, R.color.colorGray));
            }
        }
    }

    public void changeType(View v) {
        DialogFragment newFragment = new TypeSelectorDialog();
        newFragment.show(getSupportFragmentManager(), "missiles");
    }

    public void enableEditText(View v) {
        EditText et = (EditText)v;
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }
}
