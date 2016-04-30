package com.cmu.watchdog.nibbles.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.Command;
import com.cmu.watchdog.nibbles.models.Pet;

import java.sql.SQLException;

/**
 * Created by alicesypark on 4/28/16.
 */
public class PetProfileFragment extends Fragment {


    private MainActivity activity;
    private Fragment frag;
    private FragmentTransaction fragTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pet_profile, container, false);

        activity = (MainActivity) getActivity();
/*
        int feed_time = command.getValue();
        int hour = feed_time / 60;
        int minute = feed_time % 60;
        String ampm = " A.M.";
        if (hour > 12) {
            hour -= 12;
            ampm = " P.M.";
        }
        String desc = command.getCommand_desc();
        desc = desc.substring(desc.length() - 7);


        Pet pet = command.getPet();
        String pn = pet.getName();
        if (pet == null) {
            System.out.println("============================= pet doesn't exist");
        } else {
             pn = pet.getName();
            System.out.println("============================= " + pn);
        }

        TextView pet_name = (TextView) view.findViewById(R.id.pet);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView days = (TextView) view.findViewById(R.id.days);

        pet_name.setText(pet.getName());
        time.setText(String.format("%d : %02d %s", hour, minute, ampm));


        String dayDesc = new String();
        for (int i = 0; i < desc.length(); i++) {
            if (desc.charAt(i) == '1') {
                if (i == 0) {
                    dayDesc += "Monday \n";
                } else if (i == 1) {
                    dayDesc += "Tuesday \n";
                } else if (i == 2) {
                    dayDesc += "Wednesday \n";
                } else if (i == 3) {
                    dayDesc += "Thursday \n";
                } else if (i == 4) {
                    dayDesc += "Friday \n";
                } else if (i == 5) {
                    dayDesc += "Saturday \n";
                } else if (i == 6) {
                    dayDesc += "Sunday \n";
                }
            }
        }
        days.setText(dayDesc);

        Button delete_button = (Button) view.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    activity.removeCommand(command);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                frag = new ScheduleFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });
*/
        return view;

    }
}
