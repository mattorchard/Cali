package com.example.uottawa.cali;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Quang-Tri on 16/07/2017.
 */


public class ColorListDialog extends DialogFragment {

    private Integer[] color1 = new Integer[] {R.color.courseColor1, R.color.courseColor5a, R.color.courseColor3};
    private Integer[] color2 = new Integer[] {R.color.courseColor7, R.color.courseColor4, R.color.courseColor2};
    private Integer[] color3 = new Integer[] {R.color.courseColor6, R.color.courseColor3a, R.color.colorPrimaryDark};
    private int colorId;
    private EditNameDialogListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        colorId = 0;
        listener = (EditNameDialogListener) getActivity();

        View view = inflater.inflate(R.layout.color_circle_layout, container, false);

        ListView column1 = (ListView) view.findViewById(R.id.firstColumn);
        column1.setAdapter(new ColorListAdapter(view.getContext(), new ArrayList<>(Arrays.asList(color1))));
        column1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                colorId = color1[i];
                listener.onFinishEditDialog(colorId);
                dismiss();
            }
        });

        final ListView column2 = (ListView) view.findViewById(R.id.secondColumn);
        column2.setAdapter(new ColorListAdapter(view.getContext(), new ArrayList<>(Arrays.asList(color2))));
        column2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                colorId = color2[i];
                listener.onFinishEditDialog(colorId);
                dismiss();
            }
        });

        final ListView column3 = (ListView) view.findViewById(R.id.thirdColumn);
        column3.setAdapter(new ColorListAdapter(view.getContext(), new ArrayList<>(Arrays.asList(color3))));
        column3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                colorId = color3[i];
                listener.onFinishEditDialog(colorId);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }
}

interface EditNameDialogListener {
    void onFinishEditDialog(int colorId);
}

class ColorListAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Integer> color;

    ColorListAdapter(Context context, ArrayList<Integer> color) {
        super(context,R.layout.list_single, color);
        this.context = context;
        this.color = color;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.color_circle_single, parent, false);

        CardView colorCircle = (CardView) rowView.findViewById(R.id.colorCircleSingle);
        colorCircle.setBackgroundTintList(ResourcesCompat.getColorStateList(context.getResources(), color.get(position), null));

        return rowView;
    }
}
