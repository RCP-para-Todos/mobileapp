package com.jk.rcp.main.data.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jk.rcp.R;
import com.jk.rcp.main.data.model.event.Event;

import java.util.List;

public class EventListAdapter extends ArrayAdapter<Event> {
    Context context;

    public EventListAdapter(Context context, List<Event> items) {
        super(context, 0, items);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Event rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.event_item, null);
            holder = new ViewHolder();
            holder.datetime = convertView.findViewById(R.id.datetime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.datetime.setText(rowItem.getFormattedDate());


        return convertView;
    }

    private class ViewHolder {
        TextView datetime;
    }
}
