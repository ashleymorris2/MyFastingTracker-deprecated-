package com.avaygo.myfastingtracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;

/**
 * Created by Ash on 01/03/2015.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<ObjectDrawerItem> {

    Context context;
    ObjectDrawerItem [] data = null;

    public NavigationDrawerAdapter(Context context, ObjectDrawerItem [] data) {
        super(context, R.layout.listview_navigation_row ,data);

        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = convertView;

        if (itemView == null){
            itemView = layoutInflater.inflate(R.layout.listview_navigation_row, parent,false);
        }

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewIcon);
        TextView textView = (TextView) itemView.findViewById(R.id.textViewName);

        final ObjectDrawerItem currentItem = data[position];
        imageView.setImageResource(currentItem.getIcon());
        textView.setText(currentItem.getName());

        return itemView;
    }
}
