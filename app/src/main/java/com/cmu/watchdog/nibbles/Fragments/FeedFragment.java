package com.cmu.watchdog.nibbles.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import oscP5.*;
//import com.cmu.watchdog.nibbles.netP5.*;

import com.cmu.watchdog.nibbles.R;

/**
 * Created by Helen on 4/5/16.
 */
public class FeedFragment extends Fragment{
    OscP5 oscP5;
//    NetAddress remote;

    public FeedFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_now, container, false);
        return view;
    }
}
