package com.example.openplug;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlugAdapter extends ArrayAdapter<Plug> {

    private int layoutResourceId;

    private static final String LOG_TAG = "PlugAdapter";

    public PlugAdapter(Context context, List<Plug> plugs) {
        super(context, R.layout.fragment_plug);
        layoutResourceId = R.layout.fragment_plug;
        addAll(plugs);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            Plug item = getItem(position);
            View v = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                v = inflater.inflate(layoutResourceId, null);

            } else {
                v = convertView;
            }

            TextView name = (TextView) v.findViewById(R.id.plug_name);
            TextView ip = (TextView) v.findViewById(R.id.plug_ip);
            TextView id = (TextView) v.findViewById(R.id.plug_id);
            Button on = (Button) v.findViewById(R.id.plug_on);
            Button off = (Button) v.findViewById(R.id.plug_off);

            name.setText(item.name);
            ip.setText(item.ip);
            id.setText(item.id);
            on.setTag(item.ip);
            off.setTag(item.ip);

            return v;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error", ex);
            return null;
        }
    }

}