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
import com.cmu.watchdog.nibbles.models.DatabaseHandler;
import com.cmu.watchdog.nibbles.models.Pet;

import java.sql.SQLException;

/**
 * Created by alicesypark on 4/28/16.
 */
public class FeedSchedulePageFragment extends Fragment {
    private DatabaseHandler db;

    private Command command;
    private MainActivity activity;
    private Fragment frag;
    private FragmentTransaction fragTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_schedule_page, container, false);

        activity = (MainActivity) getActivity();
        command = activity.getSelectedCommand();

        int feed_time = command.getValue();
        int hour = feed_time / 60;
        int minute = feed_time % 60;
        String ampm = " A.M.";
        if (hour > 12) {
            hour -= 12;
            ampm = " P.M.";
        }
//        String desc = command.getCommand_desc();
//        desc = desc.substring(desc.length() - 7);


        Pet pet = command.getPet();

        TextView pet_name = (TextView) view.findViewById(R.id.pet);
        TextView time = (TextView) view.findViewById(R.id.time);

        pet_name.setText(pet.getName());
        time.setText(String.format("%d : %02d %s", hour, minute, ampm));




        Button delete_button = (Button) view.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    db = new DatabaseHandler();
                    db.connectDB();

                    db.removeCommand(command);

                    db.closeDB();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                frag = new ScheduleFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });

        return view;

    }
}
