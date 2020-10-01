package com.example.openplug;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        final Context context = getApplicationContext();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.fragment_popup);
                dialog.show();

                Button addPlug = dialog.findViewById(R.id.add_plug);
                addPlug.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uuid = UUID.randomUUID().toString();

                        SharedPreferences appSharedPrefs = PreferenceManager
                                .getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                        Gson gson = new Gson();

                        String devices = appSharedPrefs.getString(getString(R.string.pref_devices), "[]");
                        List<Plug> plugs = gson.fromJson(devices, new TypeToken<List<Plug>>(){}.getType());

                        Plug plug = new Plug();
                        EditText nameObj = dialog.findViewById(R.id.name);
                        EditText passwordObj = dialog.findViewById(R.id.ip);
                        plug.name = nameObj.getText().toString();
                        plug.ip = passwordObj.getText().toString();
                        plug.id = uuid;

                        if(plug.name.isEmpty()) {
                            Toast.makeText(context,getString(R.string.error_missing_name),Toast.LENGTH_SHORT).show();
                            return;
                        }

                        plugs.add(plug);

                        String json = gson.toJson(plugs);
                        prefsEditor.putString(getString(R.string.pref_devices), json);
                        prefsEditor.commit();

                        update();
                        dialog.dismiss();

                        Toast.makeText(context,getString(R.string.new_device_created),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


        update();

    }

    public void update() {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        ListView lv = findViewById(R.id.listview);

        Gson gson = new Gson();
        String devices = appSharedPrefs.getString(getString(R.string.pref_devices), "[]");
        List<Plug> plugs = gson.fromJson(devices, new TypeToken<List<Plug>>(){}.getType());

        final PlugAdapter adapter = new PlugAdapter(this, plugs);

        lv.setAdapter(adapter);

        //getting status is slooooooooooooooooooooooooow, better implement 2 buttons for on/off instead of 1 toggle button
        /*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Plug item = adapter.getItem(position);
                final TPLinkHS100 plug = new TPLinkHS100(item.ip);

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            if(plug.isOn()){
                                plug.switchOff();
                            } else{
                                plug.switchOn();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        update();
                    }
                };

                thread.start();
            }
        });
         */
    }

    public void turnOn(View view) {

        Button b = (Button) view;
        String ip = (String) b.getTag();
        final TPLinkHS100 plug = new TPLinkHS100(ip);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    plug.switchOn();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //update();
            }
        };

        thread.start();
    }

    public void turnOff(View view) {

        Button b = (Button) view;
        String ip = (String) b.getTag();
        final TPLinkHS100 plug = new TPLinkHS100(ip);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    plug.switchOff();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //update();
            }
        };

        thread.start();
    }


}