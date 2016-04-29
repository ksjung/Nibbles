package com.cmu.watchdog.nibbles;

import android.app.Dialog;
import android.app.TimePickerDialog;
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
import com.cmu.watchdog.nibbles.Fragments.ScheduleFragment;
import com.cmu.watchdog.nibbles.Fragments.WeightFragment;
import com.cmu.watchdog.nibbles.Fragments.SelectPetToMonitorFragment;
import com.cmu.watchdog.nibbles.Fragments.WebCamViewFragment;
import com.cmu.watchdog.nibbles.models.Command;
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

    private List<Pet> pets;
    private List<Device> devices;
    private List<Command> commands;

    private Pet selectedPet;
    private Device selectedDevice;


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

        connectDB();
    }

    private void connectDB() {
        try {
            String url = "jdbc:mysql://" + ip + ":3306/" + database_name; // CHANGE THIS TO RPI DB
            System.out.println("=============================" + url);
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("------------------------------Database connection established");
            setDevices();
            setPets();
            setCommands();
        } catch (SQLException e) {
            System.out.println("********SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                    System.out.println("Database connection terminated");
//                } catch (Exception e) { /* ignore close errors */ }
//            }
        }
    }

    private void setCommands() throws SQLException {
        commands = new ArrayList<Command>();
        String query = "SELECT * FROM watchdog.commands";
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
        //                Pet(String name, String gender, String type, String breed, int age, int id)
                int command_id = rs.getInt("command_id");
                int device_id = rs.getInt("device_id");
                String command_desc = rs.getString("command_desc");
                int value = rs.getInt("value");
                Command command = new Command(command_id, device_id, command_desc, value);
                commands.add(command);
            }
        } catch (SQLException e ) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            if (stmt != null) { stmt.close(); }
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

    private void setPets() throws SQLException {
        pets = new ArrayList<Pet>();
        String query = "SELECT * FROM watchdog.pet";
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
//                Pet(String name, String gender, String type, String breed, int age, int id)
                String name = rs.getString("name");
                String type = rs.getString("type");
                String gender = rs.getString("gender");
                String breed = rs.getString("breed");
                int age = rs.getInt("age");
                int id = rs.getInt("pet_id");
                Pet pet = new Pet(name, type, gender, breed, age, id);
                pets.add(pet);
            }
        } catch (SQLException e ) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    private void setDevices() throws  SQLException {
        devices = new ArrayList<Device>();
        String query = "select * from watchdog.device";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
//                Device(int device_id, String name, int pet_id)
                int device_id = rs.getInt("device_id");
                String name = rs.getString("name");
                int pet_id = rs.getInt("pet_id");
                Device device = new Device(device_id, name, pet_id);
                devices.add(device);
            }
        }
        catch (SQLException e ) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    public void sendCommand(String query) throws  SQLException {

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query); // use 'executedUpdate' when inserting data into db
        } catch (SQLException e ) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return;
    }


    public void setListView(ListView listView) throws SQLException {
        setPets();

        String[] petNames = new String[pets.size()];
        for (int i = 0; i < pets.size(); i++) {
            petNames[i] = pets.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, petNames);
        listView.setAdapter(adapter);
    }

    public void setScheduleListView(ListView listView) throws SQLException {
        setCommands();
        int count = 0;
        List<String> schedulesArray = new ArrayList<String>();

        for (int i = 0; i < commands.size(); i++) {
            int value = commands.get(i).getValue();
            if (value != -1) {
                int hour = value / 60;
                int minute = value % 60;
                String x = "A.M.";
                if (hour > 12) {
                    hour -= 12;
                    x = "P.M";
                }
                int device_id = commands.get(i).getDevice_id();
                Device device = getDeviceById(device_id);
                Pet pet = getPetById(device.getPet_id());
                schedulesArray.add(String.format("[ %s ]  %d : %02d %s", pet.getName(), hour, minute, x));
                count++;
            }
        }
        String[] schedules = new String[schedulesArray.size()];
        schedules = schedulesArray.toArray(schedules);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, schedules);
        listView.setAdapter(adapter);
    }

    public void addPet(String name, String type, String gender, String age, String breed) throws SQLException {
        String template = "INSERT INTO watchdog.pet VALUES (null, '%s', '%s', '%s', %s, '%s')";
        String query = String.format(template, name, type, gender, age, breed);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query); // use 'executedUpdate' when inserting data into db
        } catch (SQLException e ) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return;
    }

    public Pet getPetAtIndex(int i) {
        return pets.get(i);
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

    public void setSelectedDevice(Device selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    public List<WeightResult> getFeederData() throws SQLException {
        Statement stmt = null;
        int feeder = -1;
        List<WeightResult> result = new ArrayList<WeightResult>();
        //TODO: check for pet_id specific devices
        for (Device device : devices) {
            String deviceName = device.getName();
            if (deviceName.equals("FEEDER")) {
                feeder = device.getDevice_id();
            }
        }
        if (feeder != -1) {
            String query = "select * from watchdog.data WHERE data.device_id = ";
            query += feeder;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    WeightResult weightResult = new WeightResult(rs.getTimestamp("updated_at"), rs.getInt("VALUE"));
                    result.add(weightResult);
                }
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        return result;
    }

    public Map<String, String> getBackpackData() throws SQLException {
        Statement stmt = null;
        Map<String, String> result = new HashMap<String, String>();
        int backpack = -1;
        //TODO: check for pet_id specific devices
        for (Device device : devices) {
            String deviceName = device.getName();
            if (deviceName.equals("BACKPACK")) {
                backpack = device.getDevice_id();
            }
        }
        if (backpack != -1) {
            String query = "select * from watchdog.data WHERE data.device_id = ";
            query += backpack;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    result.put(rs.getString("data_desc"), rs.getString("VALUE"));
                }
            } catch (SQLException e ) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            } finally {
                if (stmt != null) { stmt.close(); }
            }
        }
        return result;
    }

    private Device getDeviceById(int id) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getDevice_id() == id) {
                return devices.get(i);
            }
        }
        return null;
    }

    private Pet getPetById(int id) {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId() == id) {
                return pets.get(i);
            }
        }
        return null;
    }
}
