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


import oscP5.*;
//import com.cmu.watchdog.nibbles.netP5.*;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import android.graphics.drawable.Drawable;

public class DataFragment extends Fragment{

    TextView name;
    ImageView activityIcon;
    TextView activityText;

    public DataFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pet_data_display, container, false);
        name = (TextView) view.findViewById(R.id.pet_name);
        activityIcon = (ImageView) view.findViewById(R.id.activity_icon);
        activityText = (TextView) view.findViewById(R.id.activity_text);

        //Activity Text Animation
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the time of the blink with this parameter
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
        try {
            String recentActivity = activity.getRecentActivity(4);
            if (recentActivity == "walking") {
                activityIcon.setImageResource(R.drawable.walking);
                activityText.setText("Walking");
            }
            else if (recentActivity == "running") {
                activityIcon.setImageResource(R.drawable.running);
                activityText.setText("Running");
            }
            else if (recentActivity == "resting") {
                activityIcon.setImageResource(R.drawable.resting);
                activityText.setText("Resting");
            }
            else {
                activityText.setText("Please refresh");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
