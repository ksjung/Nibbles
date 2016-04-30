package com.cmu.watchdog.nibbles.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.DatabaseHandler;

import java.sql.SQLException;

/**
 * Created by Alice on 4/5/16.
 */
public class AddPetFragment extends Fragment {

    private DatabaseHandler db;
    ImageButton addBtn;
    EditText name;
    EditText breed;
    EditText age;
    RadioGroup gender;
    RadioGroup type;
    Button uploadButton;

    Fragment frag;
    FragmentTransaction fragTransaction;

    public AddPetFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_pet, container, false);

        db = new DatabaseHandler();
        db.connectDB();

        addBtn = (ImageButton)view.findViewById(R.id.submit_button);

        name = (EditText)view.findViewById(R.id.pet_name);
        type = (RadioGroup)view.findViewById(R.id.type);
        breed = (EditText)view.findViewById(R.id.breed);
        gender = (RadioGroup)view.findViewById(R.id.gender);
        age = (EditText)view.findViewById(R.id.age);




        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameStr = name.getText().toString();
                String breedStr = breed.getText().toString();

                // GETTING TYPE
                int typeSelectedId = type.getCheckedRadioButtonId();
                View typeRadioButton = type.findViewById(typeSelectedId);
                int typeRadioId = type.indexOfChild(typeRadioButton);
                RadioButton typeBtn = (RadioButton) type.getChildAt(typeRadioId);
                String typeStr = (String) typeBtn.getText();

                // GETTING GENDER
                int selectedId = gender.getCheckedRadioButtonId();
                View radioButton = gender.findViewById(selectedId);
                int radioId = gender.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) gender.getChildAt(radioId);
                String genderStr = (String) btn.getText();

                String ageStr = age.getText().toString();

                MainActivity activity = (MainActivity) getActivity();
                try {
                    db = new DatabaseHandler();
                    db.connectDB();
                    db.addPet(nameStr, typeStr, genderStr, ageStr, breedStr);
                    db.closeDB();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("NAME IS + " + name.getText().toString());
                System.out.println("Gender IS + " + genderStr);

                frag = new PetManagementFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });

        return view;
    }

}
