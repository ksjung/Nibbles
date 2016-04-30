package com.cmu.watchdog.nibbles;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import com.cmu.watchdog.nibbles.Fragments.PetManagementFragment;
import com.cmu.watchdog.nibbles.Fragments.SampleFragment;
import com.cmu.watchdog.nibbles.Fragments.ScheduleFragment;
import com.cmu.watchdog.nibbles.Fragments.WeightFragment;
import com.cmu.watchdog.nibbles.Fragments.SelectPetToMonitorFragment;
import com.cmu.watchdog.nibbles.Fragments.WebCamViewFragment;
import com.cmu.watchdog.nibbles.models.Command;
import com.cmu.watchdog.nibbles.models.DatabaseHandler;
import com.cmu.watchdog.nibbles.models.Device;
import com.cmu.watchdog.nibbles.models.Pet;
import com.cmu.watchdog.nibbles.models.WeightResult;

import java.util.ArrayList;
import java.util.Calendar;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

//    String ip = "128.237.236.199"; // raspberry pi
//    private String ip = "128.237.187.196"; // localhost
    private String ip = "128.237.224.100";
    private String database_name = "watchdog";
    private String username = "watchdog";
    private String password = "watchdog";

    private Connection conn = null;

    private Map<Integer, Pet> petMap = new HashMap<Integer, Pet>();
    private Map<Integer, Device> deviceMap = new HashMap<Integer, Device>();
    private List<Pet> pets;
    private List<Device> devices;
    private List<Command> commands;

    private Pet selectedPet;
    private Device selectedDevice;
    private Command selectedCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        DatabaseHandler db = new DatabaseHandler();
        db.connectDB();
        try {
            devices = db.setDevices(deviceMap);

            pets = db.setPets(petMap);
            connectPetsDevices();

            commands = db.setCommands(deviceMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeDB();
    }

    public void connectPetsDevices() {
        for (Device device : devices) {
            int pet_id = device.getPet_id();
            int device_id = device.getDevice_id();
            Pet pet = petMap.get(pet_id);
            pet.addDevice(device);
            device.setPet(pet);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_feed) {
            Fragment fragment = new ScheduleFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_monitor) {
            Fragment fragment = new SelectPetToMonitorFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_pets) {
            Fragment fragment = new PetManagementFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_weight) {
            Fragment fragment = new WeightFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_web_view) {
            Fragment fragment = new WebCamViewFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public List<Pet> getPets() {
        return pets;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setListView(ListView listView) throws SQLException {
        String[] petNames = new String[pets.size()];
        for (int i = 0; i < pets.size(); i++) {
            petNames[i] = pets.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, petNames);
        listView.setAdapter(adapter);
    }


    public Pet getPetAtIndex(int i) {
        return pets.get(i);
    }

    public Command getCommandAtIndex(int i) {
        return commands.get(i);
    }

    public List<Command> getCommands() {
        DatabaseHandler db = new DatabaseHandler();
        db.connectDB();

        try {
            commands = db.setCommands(deviceMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commands;
    }

    public Pet getSelectedPet() {
        return selectedPet;
    }

    public void setSelectedPet(Pet selectedPet) {
        this.selectedPet = selectedPet;
    }

    public Device getSelectedDevice() {
        return selectedDevice;
    }

    public Command getSelectedCommand() {
        return selectedCommand;
    }


    public void setSelectedCommand(Command selectedCommand) {
        this.selectedCommand = selectedCommand;
    }

    public void setSelectedDevice(Device selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    public Device getDeviceById(int id) {
        return deviceMap.get(id);
    }

    public Pet getPetById(int id) {
        return petMap.get(id);
    }


}
