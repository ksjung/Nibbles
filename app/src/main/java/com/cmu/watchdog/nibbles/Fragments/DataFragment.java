package com.cmu.watchdog.nibbles.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import java.sql.SQLException;
import java.util.Map;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.Pet;


public class DataFragment extends Fragment{
    UpdateBack backUpdate;
    Handler handler = new Handler();

    TextView name;
    ImageView activityIcon;
    TextView activityText;
    Pet selectedPet;
    TextView temperature;
    TextView humidity;

    public DataFragment() {

    }

    @Override
    public void onDestroyView() {
        System.out.println("in data fragment on destroy");
        backUpdate.cancel(true);
        handler.removeCallbacks(dataRunnable);
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pet_data_display, container, false);
        name = (TextView) view.findViewById(R.id.pet_name);
        activityIcon = (ImageView) view.findViewById(R.id.activity_icon);
        activityText = (TextView) view.findViewById(R.id.activity_text);
        temperature = (TextView) view.findViewById(R.id.temperature_value);
        humidity = (TextView) view.findViewById(R.id.humidity_value);

        //Activity Text Animation
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        activityText.startAnimation(anim);

        handler.post(dataRunnable);

        return view;
    }

    private Runnable dataRunnable = new Runnable() {
        @Override
        public void run() {
            if (backUpdate != null) {
                backUpdate.cancel(true);
            }
            backUpdate = new UpdateBack();
            backUpdate.execute();
            handler.postDelayed(dataRunnable, 1000);
        }
    };

    //TODO: change to static
    private class UpdateBack extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(Void... params) {
            MainActivity activity = (MainActivity) getActivity();
            Map<String, String> result = null;
            System.out.println("in data fragment");
            try {
                result = activity.getBackpackData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Map<String, String> result) {
            if (result == null || result.isEmpty()) {
                return;
            }

            //activity
            if (result.containsKey("activity")) {
                String activity = result.get("activity");

                if (activity == null) {
                    activityIcon.setImageResource(R.drawable.paw);
                    activityText.setText("No data available. Please refresh");
                }
                else if (activity.equals("walking")) {
                    activityIcon.setImageResource(R.drawable.walking);
                    activityText.setText("Walking");
                }
                else if (activity.equals("running")) {
                    activityIcon.setImageResource(R.drawable.running);
                    activityText.setText("Running");
                }
                else if (activity.equals("resting")) {
                    activityIcon.setImageResource(R.drawable.resting);
                    activityText.setText("Resting");
                }
                else {
                    activityIcon.setImageResource(R.drawable.paw);
                    activityText.setText("Please refresh");
                }
            }

            //humidity
            if (result.containsKey("humidity")) {
                humidity.setText(result.get("humidity"));
            }

            //temperature
            if (result.containsKey("temperature")) {
                temperature.setText(result.get("temperature"));
            }
        }
    }
}
