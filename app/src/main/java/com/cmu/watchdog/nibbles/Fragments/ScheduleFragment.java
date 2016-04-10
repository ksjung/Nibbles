package com.cmu.watchdog.nibbles.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;


import netP5.*;
import oscP5.*;


/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class ScheduleFragment extends Fragment {
    Fragment frag;
    FragmentTransaction fragTransaction;

    String ip = "128.237.194.104"; // raspberry pi

    OscP5 oscP5;
    NetAddress remote;

    int portThis = 12002;
    int port = 12002;

    public ScheduleFragment() {
        // Empty constructor required for fragment subclasses
    }
    public void onCreate() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        ImageButton feedBtn = (ImageButton)view.findViewById(R.id.feedButton);


        feedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hi im kljlkjljljlkhere", "bykljljkljljllklkjlkjljkljljljle");
                OscP5 oscP5 = new OscP5(this, portThis);
                NetAddress remote = new NetAddress(ip, port);

                String msg = "on";
                OscMessage dirMessage = new OscMessage("/msg");
                dirMessage.add(msg);
                oscP5.send(dirMessage, remote);
                MainActivity activity = (MainActivity) getActivity();
                activity.sendOscpMessage("ON FROM THE APP");
                frag = new FeedFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });


        Button addScheduleBtn = (Button) view.findViewById(R.id.addButton);
        addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new AddScheduleFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });
        return view;
    }

}