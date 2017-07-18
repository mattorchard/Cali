package com.example.uottawa.cali;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


class CourseSelectorAdapter extends ArrayAdapter {

    private Context context;
    private Course[] courseList;

    public CourseSelectorAdapter(Context context, Course[] courseList) {
        super(context, R.layout.course_selector_list_item, courseList);
        this.context = context;
        this.courseList = courseList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.course_selector_list_item, parent, false);
        Course course = courseList[position];
        TextView name = (TextView)rowView.findViewById(R.id.courseNameTextViewListItem);
        name.setText(course.getName());
        ImageView courseColor = (ImageView)rowView.findViewById(R.id.courseColorListItem);
        courseColor.setColorFilter(ContextCompat.getColor(context, course.getColorIndex()));
        return rowView;
    }
}
