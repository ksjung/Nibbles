package com.cmu.watchdog.nibbles;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
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
import android.widget.TimePicker;

import com.cmu.watchdog.nibbles.Fragments.PetManagementFragment;
import com.cmu.watchdog.nibbles.Fragments.ScheduleFragment;
import com.cmu.watchdog.nibbles.Fragments.ActivityFragment;
import com.cmu.watchdog.nibbles.models.Device;
import com.cmu.watchdog.nibbles.models.Pet;

import java.util.ArrayList;
import java.util.Calendar;

import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;
import java.sql.*;
import java.util.List;

import javax.sql.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    String ip = "128.237.236.199"; // raspberry pi

    OscP5 oscP5;
    NetAddress remote;

    int portThis = 12003;
    int port = 12003;

    private Connection conn = null;

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

        NetMan nm = new NetMan();
        nm.connect();

        connectDB();
    }

    private void connectDB() {
//        Connection conn = null;
        try {
            String url = "jdbc:mysql://" + ip + ":3306/watchdog"; // CHANGE THIS TO RPI DB
            System.out.println("=============================" + url);
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, "watchdog", "watchdog");
            System.out.println("------------------------------Database connection established");
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

        String query = "SELECT * FROM watchdog.pet";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println("======================================");
//                System.out.println(rs);
//            System.out.println(rs.si)
                String name = rs.getString("name");
                System.out.println(name);
                System.out.println("======================================");
            }

        }
        catch (SQLException e ) {
            System.out.println("***************************************");
            System.out.println("SQL NOT WORKING");
            System.out.println("***************************************");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
//            if (stmt != null) { stmt.close(); }
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
        } else if (id == R.id.nav_pets) {
            Fragment fragment = new PetManagementFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_activity) {
            Fragment fragment = new ActivityFragment();
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

    public void sendOscpMessage(String str) {
        OscP5 oscP5 = new OscP5(this, portThis);
        NetAddress remote = new NetAddress(ip, port);

        String msg = str;
        OscMessage dirMessage = new OscMessage("/msg");
        dirMessage.add(msg);
        oscP5.send(dirMessage, remote);
    }

    public List<Pet> getPets() throws SQLException {
        List<Pet> pets = new ArrayList<Pet>();
        String query = "SELECT * FROM watchdog.pet";
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
//                Pet(String name, String gender, String type, String breed, int age, int id)
                String name = rs.getString("NAME");
                String type = rs.getString("TYPE");
                String gender = rs.getString("GENDER");
                String breed = rs.getString("BREED");
                int age = rs.getInt("AGE");
                int id = rs.getInt("PET_ID");
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
        return pets;
    }

    public List<Device> getDevices() throws  SQLException {
        List<Device> devices = new ArrayList<Device>();
        String query = "select * from watchdog.device";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
//                Device(int device_id, String name, int pet_id)
                int device_id = rs.getInt("DEVICE_ID");
                String name = rs.getString("NAME");
                int pet_id = rs.getInt("PET_ID");
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
        return devices;
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
}
