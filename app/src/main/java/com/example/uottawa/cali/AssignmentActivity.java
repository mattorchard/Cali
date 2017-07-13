package com.example.uottawa.cali;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AssignmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
    }

    public void changeCourse(View v) {
        Toast.makeText(getBaseContext(), "Course change unimplimented", Toast.LENGTH_SHORT).show();
    }

    public void changeComplete(View v) {
        SeekBar completeBar = (SeekBar)findViewById(R.id.completeSeekBarAssignment);
        TextView completeText = (TextView)findViewById(R.id.completeTextViewAssignment);
        //completeText.setText(getString(R.string.complete_header_assignment) +);
        Toast.makeText(getBaseContext(), "Seekbar changed", Toast.LENGTH_SHORT).show();
    }

    public void addLink(View v){
        Toast.makeText(getBaseContext(), "Add link feature unimplimented", Toast.LENGTH_SHORT).show();
    }

    public void addFile(View v){
        Toast.makeText(getBaseContext(), "Add file feature unimplemented", Toast.LENGTH_SHORT).show();
    }
}
