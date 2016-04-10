package com.cmu.watchdog.nibbles.dummy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.cmu.watchdog.nibbles.R;

/**
 * Created by Alice on 4/5/16.
 */
public class AddPetFragment extends Fragment {

    ImageButton addBtn;
    EditText name;
    EditText breed;
    RadioGroup gender;

    Fragment frag;
    FragmentTransaction fragTransaction;

    public AddPetFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_pet, container, false);

        addBtn = (ImageButton)view.findViewById(R.id.submit_button);
//        name = (EditText)view.findViewById(R.id.pet_name);
//        breed = (EditText)view.findViewById(R.id.breed);
//        gender = (RadioGroup)view.findViewById(R.id.gender);
//
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new PetManagementFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });

        return view;
    }

}
