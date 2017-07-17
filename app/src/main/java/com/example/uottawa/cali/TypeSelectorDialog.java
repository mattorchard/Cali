package com.example.uottawa.cali;


import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class TypeSelectorDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_type_selector, container, false);
        ListView listView = (ListView)view.findViewById(R.id.typeListViewType);
        //listView.setOnItemClickListener();
        listView.setAdapter(new TypeSelectorAdapter(view.getContext()));
        return view;
    }
}
class TypeSelectorAdapter extends ArrayAdapter {

    private Context context;

    TypeSelectorAdapter(Context context){
        super(context, R.layout.assignment_list_item, AssignmentTypes.values());
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.type_selector_list_item, parent, false);
        AssignmentTypes type = AssignmentTypes.values()[position];
        TextView header = (TextView)rowView.findViewById(R.id.headerTextViewTypeListItem);
        ImageButton image = (ImageButton)rowView.findViewById(R.id.imageImageButtonTypeListItem);
        header.setText(context.getString(type.getNameID()));
        image.setImageResource(type.getImageID());
        return rowView;
    }
}