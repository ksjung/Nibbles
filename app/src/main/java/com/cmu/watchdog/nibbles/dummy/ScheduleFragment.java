package com.cmu.watchdog.nibbles.dummy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cmu.watchdog.nibbles.R;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class ScheduleFragment extends Fragment {
    Fragment frag;
    FragmentTransaction fragTransaction;

    public ScheduleFragment() {
        // Empty constructor required for fragment subclasses
    }
    public void onCreate() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        Button feedBtn = (Button)view.findViewById(R.id.feedButton);
        feedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new FeedFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });

        Button addScheduleBtn = (Button)view.findViewById(R.id.addButton);
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