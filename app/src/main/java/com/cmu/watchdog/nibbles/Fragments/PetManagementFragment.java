package com.cmu.watchdog.nibbles.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.Pet;

import java.sql.SQLException;
import java.util.List;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class PetManagementFragment extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;

    public PetManagementFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_petmanagement, container, false);

        Button addBtn = (Button) view.findViewById(R.id.add_button);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new AddPetFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });

        MainActivity activity = (MainActivity) getActivity();

        try {
            List<Pet> pets = activity.getPets();
            String ex = pets.get(0).getName();
            addBtn.setText(ex);
            System.out.println("******************* PET NAME = " + ex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return view;
    }
}