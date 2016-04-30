package com.cmu.watchdog.nibbles.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.Command;
import com.cmu.watchdog.nibbles.models.DatabaseHandler;
import com.cmu.watchdog.nibbles.models.Device;
import com.cmu.watchdog.nibbles.models.Pet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class ScheduleFragment extends Fragment {
    DatabaseHandler db;
    Fragment frag;
    FragmentTransaction fragTransaction;

    private ListView listView;

    public ScheduleFragment() {
        // Empty constructor required for fragment subclasses
    }


    public void onCreate() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        ImageButton feedBtn = (ImageButton)view.findViewById(R.id.feedButton);
        feedBtnListener(feedBtn);

        Button addScheduleBtn = (Button) view.findViewById(R.id.addButton);
        addBtnListener(addScheduleBtn);


        listView = (ListView) view.findViewById(R.id.scheduleList);

        listOnclicker(listView);

        refreshscheduleList();
        return view;
    }

    private void feedBtnListener(ImageButton feedBtn) {
        feedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                Pet pet = activity.getSelectedPet();
                List<Device> devices = activity.getDevices();
                Device device = devices.get(0);
                for (int i = 0; i < devices.size(); i++) {
                    if (devices.get(i).getPet_id() == pet.getId()) {
                        device = devices.get(i);
                        break;
                    }
                }

                try {
                    db = new DatabaseHandler();
                    db.connectDB();

                    String query = String.format("INSERT INTO watchdog.commands VALUES (null, %s, 'feed now', -1, 1)", device.getDevice_id());
                    db.sendCommand(query);
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("SENDING COMMAND");
                    System.out.println(query);
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    db.closeDB();
                    Toast toast= Toast.makeText(activity,
                            "FED MY PET!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } catch (SQLException e) {
                    System.out.println("..................................");
                    System.out.println("SQL EXCEPTION");
                    System.out.println("..................................");
                    e.printStackTrace();
                    Toast toast= Toast.makeText(activity,
                            "Could not feed the pet.\nPlease try again!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }

//


//                frag = new FeedFragment();
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
//                fragTransaction.commit();
            }
        });
    }

    private void addBtnListener(Button addScheduleBtn) {
        addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Pet pet = activity.getPetAtIndex(position);
//
//                List<Device> devices = activity.getDevices();
//                Device device = devices.get(0);
//                for (int i = 0; i < devices.size(); i++) {
//                    if (devices.get(i).getPet_id() == pet.getId()) {
//                        device = devices.get(i);
//                        break;
//                    }
//                }
//
//                activity.setSelectedPet(pet);
//                activity.setSelectedDevice(device);

                frag = new AddScheduleFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });
    }

    private void listOnclicker(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                MainActivity activity = (MainActivity) getActivity();


                Command command = activity.getCommandAtIndex(position);
                System.out.println(command.toString() + " *** ID = " + command.getCommand_desc());

                activity.setSelectedCommand(command);

                Fragment frag;
                FragmentTransaction fragTransaction;

                frag = new FeedSchedulePageFragment();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.content_frame, frag);
                fragTransaction.commit();
            }
        });
    }

    public void refreshscheduleList() {

        MainActivity activity = (MainActivity) getActivity();

        List<Command> commands = activity.getCommands();
        List<String> schedulesArray = new ArrayList<String>();
        int count = 0;

        for (int i = 0; i < commands.size(); i++) {
            int value = commands.get(i).getValue();
            if (value != -1) {
                int hour = value / 60;
                int minute = value % 60;
                String x = "A.M.";
                if (hour > 12) {
                    hour -= 12;
                    x = "P.M.";
                }
                int device_id = commands.get(i).getDevice_id();
                Device device = activity.getDeviceById(device_id);
                Pet pet = activity.getPetById(device.getPet_id());
                schedulesArray.add(String.format("%d : %02d %s", hour, minute, x));
                count++;
            }
        }
        String[] schedules = new String[schedulesArray.size()];
        schedules = schedulesArray.toArray(schedules);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, schedules);
        listView.setAdapter(adapter);
    }
}