package com.example.uottawa.cali;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox priorityCheckBox;
    private CheckBox dueCheckBox;
    private CheckBox completeCheckBox;
    private Switch dailyNotificationSwitch;
    private Switch upcomingNotificationSwitch;
    private Switch completedAssignmentsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        priorityCheckBox= (CheckBox)findViewById(R.id.priorityCheckBoxSettings);
        dueCheckBox = (CheckBox)findViewById(R.id.dueCheckBoxSettings);
        completeCheckBox = (CheckBox)findViewById(R.id.completeCheckBoxSettings);
        dailyNotificationSwitch = (Switch)findViewById(R.id.dailyNotificationSwitchSettings);
        upcomingNotificationSwitch = (Switch)findViewById(R.id.upcomingNotificationSwitchSettings);
        completedAssignmentsSwitch= (Switch)findViewById(R.id.completedAssignmentsSwitchSettings);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        priorityCheckBox.setChecked(sharedPref.getBoolean(getString(R.string.priority_sort_preferences), true));
        dueCheckBox.setChecked(sharedPref.getBoolean(getString(R.string.due_sort_preferences), true));
        completeCheckBox.setChecked(sharedPref.getBoolean(getString(R.string.complete_sort_preferences), true));
        dailyNotificationSwitch.setChecked(sharedPref.getBoolean(getString(R.string.daily_notification_preferences), false));
        upcomingNotificationSwitch.setChecked(sharedPref.getBoolean(getString(R.string.upcoming_notification_preferences), false));
        completedAssignmentsSwitch.setChecked(sharedPref.getBoolean(getString(R.string.completed_assignments_preferences), false));
    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
        editor.putBoolean(getString(R.string.complete_sort_preferences), completeCheckBox.isChecked());
        editor.putBoolean(getString(R.string.due_sort_preferences), dueCheckBox.isChecked());
        editor.putBoolean(getString(R.string.priority_sort_preferences), priorityCheckBox.isChecked());
        editor.putBoolean(getString(R.string.upcoming_notification_preferences), upcomingNotificationSwitch.isChecked());
        editor.putBoolean(getString(R.string.daily_notification_preferences), dailyNotificationSwitch.isChecked());
        editor.putBoolean(getString(R.string.completed_assignments_preferences), completedAssignmentsSwitch.isChecked());
        editor.commit();
        super.onBackPressed();
    }
}
