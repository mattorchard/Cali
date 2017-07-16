package com.example.uottawa.cali;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends IOActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int ASSIGNMENT_REQUEST = 1;
    private int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assignment freshAssignment = new Assignment();
                freshAssignment.setName(getString(R.string.fresh_assignment_name));
                assignmentsFile.add(freshAssignment);
                selectedIndex = assignmentsFile.size() - 1;
                Intent intent = new Intent(getBaseContext(), AssignmentActivity.class);
                intent.putExtra(getString(R.string.intent_assignment_data_send), freshAssignment);
                intent.putExtra(getString(R.string.intent_assignment_fresh), true);
                startActivityForResult(intent, ASSIGNMENT_REQUEST);
            }
        });
        //Drawer
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //Maybe something needs to happen here
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //Add course button
        ImageButton addCourseBtn = (ImageButton) findViewById(R.id.addCourseBtn);
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.example.uottawa.cali.CourseActivity");
                intent.putExtra("isNewCourse", true);
                startActivity(intent);
            }
        });

        //ListView
        setSampleData();
        ArrayAdapter summaryListViewAdapter = new AssignmentListAdapter(this, assignmentsFile.toArray(new Assignment[assignmentsFile.size()]));
        ListView summaryListView = (ListView)findViewById(R.id.summaryListViewMain);
        summaryListView.setAdapter(summaryListViewAdapter);
        summaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                selectedIndex = position;
                Assignment item = (Assignment)parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), AssignmentActivity.class);
                intent.putExtra(getString(R.string.intent_assignment_data_send), item);
                intent.putExtra(getString(R.string.intent_assignment_fresh), false);
                startActivityForResult(intent, ASSIGNMENT_REQUEST);
            }
        });

        //Drawer layout ListView
        ArrayAdapter drawerAdapter = new DrawerListAdapter(this, coursesFile);
        final ListView drawerListView = (ListView)findViewById(R.id.drawerlist);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setClickable(true);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                selectedIndex = position;
                Course item = (Course)parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), CourseActivity.class);
                intent.putExtra("course", item);
                intent.putExtra("isNewCourse", false);
                startActivity(intent);
            }
        });
        setCourseDialog(drawerListView, coursesFile, drawerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ASSIGNMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                FileOperations assignmentOperation = (FileOperations)data.getSerializableExtra(getString(R.string.intent_assignment_operation));
                if (FileOperations.MODIFY == assignmentOperation) {
                    assignmentsFile.set(selectedIndex, (Assignment)data.getSerializableExtra(getString(R.string.intent_assignment_data_receive)));
                    reload();
                } else if (FileOperations.DELETE == assignmentOperation) {
                    assignmentsFile.remove(selectedIndex);
                    reload();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = item.getTitle().toString();
        //Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (title == "Notifications"){
            Intent intent = new Intent("com.example.uottawa.cali.Test");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void reload() {
        ArrayAdapter summaryListViewAdapter = new AssignmentListAdapter(this, assignmentsFile.toArray(new Assignment[assignmentsFile.size()]));
        ListView summaryListView = (ListView)findViewById(R.id.summaryListViewMain);
        summaryListView.setAdapter(summaryListViewAdapter);
    }

    private void setSampleData() {
        coursesFile = new ArrayList<>();
        assignmentsFile = new ArrayList<>();
        coursesFile.add(new Course("Networking", R.color.courseColor1, R.color.courseColor1a));
        coursesFile.add(new Course("UI Design", R.color.courseColor3, R.color.courseColor3a));
        coursesFile.add(new Course("UI Interface", R.color.courseColor7, R.color.courseColor8a));
        Assignment tempAssignment = new Assignment(coursesFile.get(1), "App Presentation", 66, new Date(), 1, AssignmentTypes.GROUP_WORK, "asd");
        assignmentsFile.add(tempAssignment);
        tempAssignment = new Assignment(coursesFile.get(0), "Routing Lab #8", 75, new Date(), 2, AssignmentTypes.LAB_REPORT, "dsa");
        assignmentsFile.add(tempAssignment);
        tempAssignment = new Assignment(coursesFile.get(1), "Assignment #6", 20, new Date(), 4, AssignmentTypes.PROBLEM_SET, "sad");
        assignmentsFile.add(tempAssignment);
    }

    //Used to set create a dialog for course listview on long press
    public void setCourseDialog(ListView listView, final ArrayList<Course> coursesFile, final ArrayAdapter drawerAdapter) {
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, final int position, long id) {

                //Do your tasks here
                final int courseSelected = position;
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Options");
                CharSequence[] items = new CharSequence[] {"Remove course"};
                alert.setItems(items, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int option) {
                        switch (option) {
                            case 0:
                                AlertDialog.Builder confirmation = new AlertDialog.Builder(MainActivity.this);
                                confirmation.setMessage("Are you sure you want to delete this entry?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                coursesFile.remove(courseSelected);
                                                drawerAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        }).show();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                alert.show();
                return true;
            }
        });
    }
}
