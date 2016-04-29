package com.cmu.watchdog.nibbles.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.Command;
import com.cmu.watchdog.nibbles.models.Pet;

import java.sql.SQLException;
import java.util.List;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class PetManagementFragment extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;
    List<Pet> pets;

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

        ListView listView = (ListView) view.findViewById(R.id.petsList);
        listOnclicker(listView);
        try {
            activity.setListView(listView);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void listOnclicker(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
            {
                MainActivity activity = (MainActivity) getActivity();

                Pet pet = activity.getPetAtIndex(position);

                activity.setSelectedPet(pet);

                Fragment frag;
                FragmentTransaction fragTransaction;

                frag = new PetProfileFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });
    }

}