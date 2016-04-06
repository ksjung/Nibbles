package com.cmu.watchdog.nibbles.dummy;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TimePicker;

import com.cmu.watchdog.nibbles.R;

import java.util.Calendar;

/**
 * Created by Helen on 4/5/16.
 */
public class AddScheduleFragment extends Fragment {
    public AddScheduleFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_schedule, container, false);
        return view;
    }

}
