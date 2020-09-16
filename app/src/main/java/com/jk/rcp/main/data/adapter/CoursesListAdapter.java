package com.jk.rcp.main.data.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jk.rcp.R;
import com.jk.rcp.main.data.model.course.Course;

import java.util.List;

public class CoursesListAdapter extends ArrayAdapter<Course> {
    Context context;

    public CoursesListAdapter(Context context, List<Course> items) {
        super(context, 0, items);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Course rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.course_item, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(rowItem.getName());


        return convertView;
    }

    private class ViewHolder {
        TextView name;
    }
}
