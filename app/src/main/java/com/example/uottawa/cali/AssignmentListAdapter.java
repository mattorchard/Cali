package com.example.uottawa.cali;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AssignmentListAdapter extends ArrayAdapter {

    private Context context;
    private Assignment[] assignments;

    public AssignmentListAdapter(Context context, Assignment[] assignments){
        super(context, R.layout.assignment_list_item, assignments);
        this.context = context;
        this.assignments = assignments;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.assignment_list_item, parent, false);
        Assignment assignment = assignments[position];


        TextView title = (TextView)rowView.findViewById(R.id.assignmentTitleTextViewListElement);
        title.setText(assignment.getTitle());

        TextView dueDate = (TextView)rowView.findViewById(R.id.assignmentDateTextViewListElement);
        dueDate.setText(assignment.getDateString());

        View progress = (View)rowView.findViewById(R.id.assignmentProgressViewListElement);
        View progressInverse = (View)rowView.findViewById(R.id.assignmentProgressInverseViewListElement);
        progress.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, assignment.getComplete()));
        progressInverse.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 100 - assignment.getComplete()));
        return rowView;
    }
}
