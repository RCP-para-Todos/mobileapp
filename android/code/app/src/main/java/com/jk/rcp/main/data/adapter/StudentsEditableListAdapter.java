package com.jk.rcp.main.data.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jk.rcp.R;
import com.jk.rcp.main.data.model.course.Student;

import java.util.List;

public class StudentsEditableListAdapter extends ArrayAdapter<Student> {
    Context context;
    private List<Student> list;

    public StudentsEditableListAdapter(Context context, List<Student> items) {
        super(context, 0, items);
        this.context = context;
        this.list = items;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Student rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.student_editable_item, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(rowItem.getName());
        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)convertView.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView name;
    }
}
