package com.cmu.watchdog.nibbles.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import oscP5.*;
//import com.cmu.watchdog.nibbles.netP5.*;

import com.cmu.watchdog.nibbles.R;

public class DataFragment extends Fragment{

    TextView name;
    ImageView activityIcon;
    TextView activityText;

    public DataFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pet_data_display, container, false);
        name = (TextView) view.findViewById(R.id.pet_name);
        activityIcon = (ImageView) view.findViewById(R.id.activity_icon);
        activityText = (TextView) view.findViewById(R.id.activity_text);
        
        return view;
    }
}
