package com.example.uottawa.cali;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class DrawerListAdapter extends ArrayAdapter {

    private Activity context;
    private ArrayList<Course> courses;

    public DrawerListAdapter(Activity context, ArrayList<Course> courses) {
        super(context,R.layout.list_single, courses);
        this.context = context;
        this.courses = courses;
    }

    public View getView(int position, View view, ViewGroup parent) {

        if (courses.get(position).getName().equals(Course.getUnsetCourse().getName())) {
            return new View(context);
        }
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single, null, true);
        TextView courseText = (TextView) rowView.findViewById(R.id.txt);
        CardView colorCircle = (CardView) rowView.findViewById(R.id.colorCircle);

        int colorIndex = courses.get(position).getColorIndex();
        colorCircle.setBackgroundTintList(context.getResources().getColorStateList(colorIndex));
        courseText.setText(courses.get(position).getName());

        return rowView;
    }

}