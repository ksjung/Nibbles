package com.cmu.watchdog.nibbles.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.Device;
import com.cmu.watchdog.nibbles.models.Pet;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by alicesypark on 4/24/16.
 */
public class SelectPetToFeedFragment  extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_now, container, false);

        MainActivity activity = (MainActivity) getActivity();

        ListView listView = (ListView) view.findViewById(R.id.petsList);

        try {
            activity.setListView(listView);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
            {
                MainActivity activity = (MainActivity) getActivity();
                System.out.println("********* CLICKED *************");

                Pet pet = activity.getPetAtIndex(position);
                System.out.println(pet.toString() + " *** ID = " +pet.getId());

                List<Device> devices = activity.getDevices();
                Device device = devices.get(0);
                for (int i = 0; i < devices.size(); i++) {
                    if (devices.get(i).getPet_id() == pet.getId()) {
                        device = devices.get(i);
                        break;
                    }
                }

                activity.setSelectedPet(pet);
                activity.setSelectedDevice(device);

                Fragment frag;
                FragmentTransaction fragTransaction;

                frag = new AddScheduleFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });

        return view;

    }
}
