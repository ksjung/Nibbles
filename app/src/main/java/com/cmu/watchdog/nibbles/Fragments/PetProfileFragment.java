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

    private Button delButton;
    private Button editButton;

    private TextView pet_text;

    private Pet pet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pet_profile, container, false);

        activity = (MainActivity) getActivity();

        delButton = (Button) view.findViewById(R.id.del_button);
        pet_text = (TextView) view.findViewById(R.id.pet_name);
        pet = activity.getSelectedPet();

        pet_text.setText(pet.getName());
        delButtonClicker();

//
//        Button delete_button = (Button) view.findViewById(R.id.delete_button);
//        delete_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    activity.removeCommand(command);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//                frag = new ScheduleFragment();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
//                fragTransaction.commit();
//            }
//        });

        return view;

    }

    private void delButtonClicker() {
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    activity.removePet(pet);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }

                frag = new PetManagementFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });
    }
}
