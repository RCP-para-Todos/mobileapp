package com.jk.rcp.main.data.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jk.rcp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FeltEarthquakeListAdapter extends ArrayAdapter<String> {
    Context context;

    public FeltEarthquakeListAdapter(Context context, List<String> items) {
        super(context, 0, items);
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final String rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.felt_earthquake_item, null);
            holder = new ViewHolder();
            holder.type = convertView.findViewById(R.id.movementType);
            holder.datetime = convertView.findViewById(R.id.movementDateTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String[] itemArray = rowItem.split("-");
        Date dateTime = new Date(Long.valueOf(itemArray[0]));
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        holder.type.setText(itemArray[1]);
        holder.datetime.setText(dateFormat.format(dateTime));

        return convertView;
    }

    private class ViewHolder {
        TextView type;
        TextView datetime;
    }
}
