package com.cmu.watchdog.nibbles.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import java.sql.SQLException;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.Pet;

import android.graphics.drawable.Drawable;

public class DataFragment extends Fragment{

    TextView name;
    ImageView activityIcon;
    TextView activityText;
    Pet selectedPet;
    TextView weight;
    TextView temperature;
    TextView humidity;

    public DataFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pet_data_display, container, false);
        name = (TextView) view.findViewById(R.id.pet_name);
        activityIcon = (ImageView) view.findViewById(R.id.activity_icon);
        activityText = (TextView) view.findViewById(R.id.activity_text);
        weight = (TextView) view.findViewById(R.id.weight_value);
        temperature = (TextView) view.findViewById(R.id.temperature_value);
        humidity = (TextView) view.findViewById(R.id.humidity_value);

        //Activity Text Animation
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        activityText.startAnimation(anim);

        refreshPetActivity();

        final Button refreshButton = (Button) view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshPetActivity();
            }
        });

        return view;
    }

    private void refreshPetActivity() {
        MainActivity activity = (MainActivity) getActivity();
        //<div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>

        selectedPet = activity.getSelectedPet();
        name.setText(selectedPet.getName());
        int id = selectedPet.getId();
        try {
            String recentActivity = activity.getRecentActivity(id);
            if (recentActivity == null) {
                activityIcon.setImageResource(R.drawable.paw);
                activityText.setText("No data available. Please refresh");
            }
            else if (recentActivity.equals("walking")) {
                activityIcon.setImageResource(R.drawable.walking);
                activityText.setText("Walking");
            }
            else if (recentActivity.equals("running")) {
                activityIcon.setImageResource(R.drawable.running);
                activityText.setText("Running");
            }
            else if (recentActivity.equals("resting")) {
                activityIcon.setImageResource(R.drawable.resting);
                activityText.setText("Resting");
            }
            else {
                activityIcon.setImageResource(R.drawable.paw);
                activityText.setText("Please refresh");
            }

            //Get backpack data
            int device_id = activity.getBackpack(id);
            System.out.print("device id");
            System.out.println(device_id);
            int weightValue = activity.getWeight(device_id);
            if (weightValue != -1) {
                weight.setText(weightValue);
            }
            else {
                weight.setText("unavailable");
            }
            String temperatureValue = activity.getTemperature(device_id);
            if (temperature != null) {
                temperature.setText(temperatureValue);
            }
            else {
                temperature.setText("unavailable");
            }
            String humidityValue = activity.getHumidity(device_id);
            if (humidity != null) {
                humidity.setText(humidityValue);
            }
            else {
                humidity.setText("unavailable");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
