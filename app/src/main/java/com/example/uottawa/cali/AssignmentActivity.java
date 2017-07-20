package com.example.uottawa.cali;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;


public class AssignmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private final int[] priorityButtons = new int[]{R.id.priorityButton1Assignment, R.id.priorityButton2Assignment, R.id.priorityButton3Assignment, R.id.priorityButton4Assignment, R.id.priorityButton5Assignment};
    private ArrayList<Course> courseList;
    private boolean fresh;
    private Assignment assignment;
    private Assignment assignmentOriginal;
    private TextView courseTextView;
    private LinearLayout courseBackground;
    private EditText nameEditText;
    private TextView dueTextView;
    private TextView typeTextView;
    private EditText descriptionEditText;


    private LinearLayout attachmentLayout;
    private LinearLayout linkLayout;
    private static LinkedList<Uri> uriList;
    private static LinkedList<String> urlList;

    private Calendar currentDate;
    private Calendar alarmTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        //Add a shadow to the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        toolbar.setElevation(10);

        attachmentLayout = (LinearLayout) findViewById(R.id.attachmentLayout);
        linkLayout = (LinearLayout) findViewById(R.id.linkLayout);

        Intent intent = getIntent();
        courseList = (ArrayList<Course>) intent.getSerializableExtra(getString(R.string.intent_course_list));
        fresh = intent.getBooleanExtra(getString(R.string.intent_assignment_fresh), false);
        assignment = (Assignment)intent.getSerializableExtra(getString(R.string.intent_assignment_data_send));
        if (null == assignment) {
            Toast.makeText(getBaseContext(), getString(R.string.error_assignment_parse), Toast.LENGTH_SHORT).show();
            finish();
        }
        assignmentOriginal = new Assignment(assignment);

        //Course bar
        courseTextView = (TextView)findViewById(R.id.courseTextViewAssignment);
        courseBackground = (LinearLayout)findViewById(R.id.courseBackgroundAssignment);
        courseTextView.setText(assignment.getCourse().getName());
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

        //Update the current date calendar
        currentDate = Calendar.getInstance();
        alarmTime = Calendar.getInstance();
        currentDate.setTime(assignment.getDueDate());

        //Repopulate the attachments and links if they exist
        if (!assignment.getAttachmentList().isEmpty()) {
            for (NamedUri namedUri : assignment.getAttachmentList()) {
                Uri selectedFile = Uri.parse(namedUri.getUri());

                TextView uriView = createTextViewFromUri(selectedFile, namedUri.getName());

                appendNewTextView(attachmentLayout, uriView, namedUri, null);
            }
        }

        if (!assignment.getNamedLinkList().isEmpty()) {
            for (NamedLink namedLink : assignment.getNamedLinkList()) {

                TextView urlView = createTextViewFromNamedLink(namedLink);

                appendNewTextView(linkLayout, urlView, null, namedLink);
            }
        }
    }

    public void clickBack(View v) {
        onBackPressed();
    }

    public void clickComplete(View v) {
        assignment.setName(nameEditText.getText().toString());
        assignment.setDescription(descriptionEditText.getText().toString());
        Intent intent = new Intent();
        if (fresh) {
            intent.putExtra(getString(R.string.intent_assignment_operation), FileOperations.MODIFY);
            intent.putExtra(getString(R.string.intent_assignment_data_receive), assignment);
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            int count = sharedPref.getInt("assignmentCount", 0); //0 is default value.
            count++;
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.putInt("assignmentCount", count);
            edit.commit();
        } else if (!assignment.equals(assignmentOriginal)) {
            intent.putExtra(getString(R.string.intent_assignment_operation), FileOperations.MODIFY);
            intent.putExtra(getString(R.string.intent_assignment_data_receive), assignment);
        }  else {
            intent.putExtra(getString(R.string.intent_assignment_operation), FileOperations.UNCHANGED);
       }
        setResult(RESULT_OK, intent);
        finish();
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
        //Toast.makeText(getBaseContext(), "Course change unimplemented", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_course_selector, null);
        builder.setView(view);
        ListView listView = (ListView)view.findViewById(R.id.courseDialogListView);
        listView.setAdapter(new CourseSelectorAdapter(getBaseContext(), courseList.toArray(new Course[courseList.size()])));
        final AlertDialog dialog = builder.create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                assignment.setCourse(courseList.get(position));
                dialog.dismiss();
                courseTextView.setText(assignment.getCourse().getName());
                courseBackground.setBackgroundResource(assignment.getCourse().getColorIndex());
            }

        });
        dialog.show();
    }


    public void addLink(View v){
        //Create a new dialog to take the URL info from the user
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.link_dialog_layout, null);
        final EditText urlEditText = (EditText)dialogView.findViewById(R.id.urlEditText);
        final EditText nameEditText = (EditText)dialogView.findViewById(R.id.nameEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a new link");
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        //When OK is pressed, create a new TextView and place it in the layout
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = appendUrlPrefix(urlEditText.getText().toString());
                String name = nameEditText.getText().toString().replace(" " , "");

                if (name.equals("")) {
                    name = url;
                }

                NamedLink namedLink = new NamedLink(name, url);
                TextView urlView = createTextViewFromNamedLink(namedLink);
                assignment.getNamedLinkList().add(namedLink);

                appendNewTextView(linkLayout, urlView, null, namedLink);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void addFile(View v){

        if (Build.VERSION.SDK_INT < 19) {
            Intent intentAddFile = new Intent();
            intentAddFile.setType("*/*");
            intentAddFile.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentAddFile, 420);
        } else {
            Intent intentAddFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intentAddFile.addCategory(Intent.CATEGORY_OPENABLE);
            intentAddFile.setType("*/*");
            intentAddFile.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentAddFile, 420);
        }


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
        //DialogFragment newFragment = new TypeSelectorDialog();
        //newFragment.show(getSupportFragmentManager(), "Change Type");
        String[] typeStrings = new String[AssignmentTypes.values().length];
        for (int i = 0; i < AssignmentTypes.values().length; i ++) {
            typeStrings[i] = getString(AssignmentTypes.values()[i].getNameID());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentActivity.this);
        builder.setTitle(R.string.title_type_dialog)
                .setItems(typeStrings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        assignment.setType(AssignmentTypes.values()[which]);
                        typeTextView.setText(getString(assignment.getType().getNameID()));
                    }
                });
        builder.create().show();
    }

    public void enableEditText(View v) {
        EditText et = (EditText)v;
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    public void deleteAssignment(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_delete_dialog));
        builder.setMessage(getString(R.string.message_delete_dialog) + " " + nameEditText.getText().toString() + "?");
        builder.setPositiveButton(getString(R.string.positive_delete_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.intent_assignment_operation), FileOperations.DELETE);
                setResult(RESULT_OK, intent);
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.negative_delete_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Allows the system to get the URI of the file that the user chose
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If the user successfully chose a file, add it to the list
        if(requestCode == 420 && resultCode == RESULT_OK) {
            Uri selectedFile = data.getData();
            //Must convert the URI into a string, or the assignment will not be able to serialize
            NamedUri namedUri = new NamedUri(getFileName(selectedFile), selectedFile.toString());
            assignment.getAttachmentList().add(namedUri);

            TextView uriView = createTextViewFromUri(selectedFile, getFileName(selectedFile));
            appendNewTextView(attachmentLayout, uriView, namedUri, null);
        }
    }

    public void changeDate(View v){

        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                AssignmentActivity.this,
                currentDate.get(currentDate.YEAR),
                currentDate.get(currentDate.MONTH),
                currentDate.get(currentDate.DAY_OF_MONTH)
        );

        datePicker.show(getFragmentManager(), "Due date");
    }

    private TextView createTextViewFromUri(final Uri uri, String name) {

        TextView uriView = new TextView(this);
        uriView.setText(name);

        uriView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                //openFileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                openFileIntent.setData(uri);

                try {
                    startActivity(openFileIntent);
                } catch (ActivityNotFoundException e) {

                }
            }
        });

        return uriView;
    }

    private TextView createTextViewFromNamedLink(final NamedLink namedLink) {

        TextView urlView = new TextView(this);
        SpannableString nameText = new SpannableString(namedLink.getName());

        //Make the text underlined
        nameText.setSpan(new UnderlineSpan(), 0, nameText.length(), 0);
        urlView.setText(nameText);

        //Add an onClickListener so the hyperlink can navigate to the link when clicked
        urlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(namedLink.getUrl()));
                startActivity(browserIntent);
            }
        });

        return urlView;
    }

    /**
     * Method used to add a new TextView to the Attachment or Link layout.
     * @param parentLayout
     * @param newTextView
     */
    private void appendNewTextView(LinearLayout parentLayout, TextView newTextView, final NamedUri namedUri, final NamedLink namedLink) {

        int marginTopDp = 20;
        int marginLeftDp = 32;

        float density = getBaseContext().getResources().getDisplayMetrics().density;
        //Convert dp margins to px
        int marginTop = (int)(marginTopDp * density);
        int marginLeft = (int)(marginLeftDp * density);

        newTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLeft, marginTop, 0, 0);
        newTextView.setLayoutParams(layoutParams);

        parentLayout.addView(newTextView);
        newTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(AssignmentActivity.this, view);
                popupMenu.inflate(R.menu.attachment_link_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (view.getParent().equals(attachmentLayout)) {
                            attachmentLayout.removeView(view);
                            findAndRemoveAttachment(namedUri);
                        } else {
                            linkLayout.removeView(view);
                            findAndRemoveLink(namedLink);
                        }

                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    private void findAndRemoveAttachment(NamedUri namedUri) {
        for (NamedUri uri : assignment.getAttachmentList()) {
            if (uri.equals(namedUri)) {
                assignment.getAttachmentList().remove(uri);
            }
        }
    }

    private void findAndRemoveLink(NamedLink namedLink) {
        for (NamedLink link : assignment.getNamedLinkList()) {
            if (link.equals(namedLink)) {
                assignment.getNamedLinkList().remove(link);
            }
        }
    }

    /**
     * Checks if a URL starts with "http", if it does not, prepend it.
     * This is because the URL must start with http, or the browser activity
     * will not be able to process it.
     * @param link
     * @return
     */
    private String appendUrlPrefix(String link) {
        if (!link.startsWith("http")) {
            link = "http://" + link;
        }
        return link;
    }

    //Method used to get a file name from a URI
    //Via Stefan Haustein on StackOverflow:
    //https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month = "";

        switch (monthOfYear) {
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

        String date = month + " " + dayOfMonth;

        //Update the currentDate object
        currentDate.setTimeInMillis(System.currentTimeMillis());
        currentDate.set(Calendar.YEAR, year);
        currentDate.set(Calendar.MONTH, monthOfYear);
        currentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //Update the saved due date and the TextView
        assignment.setDueDate(currentDate.getTime());
        dueTextView.setText(date);

        //Setting the notification to send 2 days before the due date
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int count = sharedPref.getInt("assignmentCount", 0); //0 is default value.
        boolean isUpcomingNotificationOn = sharedPref.getBoolean(getString(R.string.upcoming_notification_preferences), true);
        boolean isDailyNotificationOn = sharedPref.getBoolean(getString(R.string.daily_notification_preferences), true);

        Intent intent = new Intent(AssignmentActivity.this, MyReceiver.class);
        intent.putExtra("Assignment", assignment.getNotificationString());
        intent.putExtra("isUpcomingNotificationOn", isUpcomingNotificationOn);
        intent.putExtra("isDailyNotificationOn", isDailyNotificationOn);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AssignmentActivity.this, count, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if(isUpcomingNotificationOn) {
            alarmTime.setTimeInMillis(currentDate.getTimeInMillis() - 48 * 60 * 60 * 1000);
            alarmTime.set(Calendar.HOUR_OF_DAY, 9);
            alarmTime.set(Calendar.MINUTE, 0);
            alarmTime.set(Calendar.SECOND, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis() , pendingIntent);
        } else if (isDailyNotificationOn) {
            alarmTime.setTimeInMillis(currentDate.getTimeInMillis() + 24 * 60 * 60 * 1000);
            alarmTime.set(Calendar.HOUR_OF_DAY, 9);
            alarmTime.set(Calendar.MINUTE, 0);
            alarmTime.set(Calendar.SECOND, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        /*Toast.makeText(this, String.valueOf(alarmTime.getTime().toString()), Toast.LENGTH_LONG).show();*/
    }
}
