package com.cmu.watchdog.nibbles.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.Device;
import com.cmu.watchdog.nibbles.models.Pet;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Helen on 4/5/16.
 */
public class AddScheduleFragment extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;

    NumberPicker hourPicker;
    NumberPicker minutePicker;
    TextView ap;

    private Boolean am = true;

    public AddScheduleFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_schedule, container, false);

        MainActivity activity = (MainActivity) getActivity();

        hourPicker = (NumberPicker) view.findViewById(R.id.hourPicker);
        minutePicker = (NumberPicker) view.findViewById(R.id.minutePicker);
        ap = (TextView) view.findViewById(R.id.ampm);

        hourPicker.setMinValue(1); //from array first value
        hourPicker.setMaxValue(12);
        hourPicker.setWrapSelectorWheel(true);

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // do something here
                if ((newVal == 1) && (oldVal == 12)) {
                    if (am) {
                        ap.setText("P.M.");
                    } else {
                        ap.setText("A.M.");
                    }
                    am = !am;
                } else if ((newVal == 12) && (oldVal == 1)) {
                    if (am) {
                        ap.setText("P.M.");
                    } else {
                        ap.setText("A.M.");
                    }
                    am = !am;
                }
            }
        });

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);
        minutePicker.setWrapSelectorWheel(true);


        ImageButton doneButton = (ImageButton) view.findViewById(R.id.doneButton);
        doneBtnListener(doneButton);
        return view;
    }

    private void doneBtnListener(ImageButton doneButton) {
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = hourPicker.getValue();
                int minute = minutePicker.getValue();

                if (!am) {
                    hour += 12;
                }
                int time = hour * 60 + minute;

                MainActivity activity = (MainActivity) getActivity();
                Pet pet = activity.getSelectedPet();
                Device device = activity.getSelectedDevice();

                try {
                    String query = String.format("INSERT INTO watchdog.commands VALUES (null, %s, 'feed', %d, 1)", device.getDevice_id(), time);
                    activity.sendCommand(query);
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("SENDING COMMAND");
                    System.out.println(query);
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                } catch (SQLException e) {
                    System.out.println("..................................");
                    System.out.println("SQL EXCEPTION");
                    System.out.println("..................................");
                    e.printStackTrace();
                }

                frag = new ScheduleFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });
    }

}
