package com.cmu.watchdog.nibbles.models;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ksjung on 4/29/16.
 */
public class DatabaseHandler {

    private static final String ip = "128.237.224.100";
    private static final String database_name = "watchdog";
    private static final String username = "watchdog";
    private static final String password = "watchdog";

    private static final String url = "jdbc:mysql://" + ip + ":3306/" + database_name; // CHANGE THIS TO RPI DB
    private static Connection conn = null;

    public static void connectDB() {
        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("=============================" + url);
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, username, password);
                System.out.println("------------------------------Database connection established");
            }
        } catch (SQLException e) {
            System.out.println("********SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void closeDB() {
        try {
            if (conn != null && !conn.isClosed()) {

                conn.close();
                System.out.println("===== Database connection terminated====");
            }
        } catch (Exception e) { /* ignore close errors */ }
    }

    public List<Device> setDevices(Map<Integer, Device> deviceMap) throws  SQLException {
        List<Device> devices = new ArrayList<Device>();
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
                deviceMap.put(device_id, device);
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

    public List<Pet> setPets(Map<Integer, Pet> petMap) throws SQLException {
        List<Pet> pets = new ArrayList<Pet>();
        String query = "SELECT * FROM watchdog.pet";
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                String gender = rs.getString("gender");
                String breed = rs.getString("breed");
                int age = rs.getInt("age");
                int id = rs.getInt("pet_id");
                Pet pet = new Pet(name, type, gender, breed, age, id);
                pets.add(pet);
                petMap.put(id, pet);
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

    public List<Command> setCommands(Map<Integer, Device> deviceMap) throws SQLException {
        List<Command> commands = new ArrayList<Command>();
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
                if (value == -1) {
                    continue;
                }
                Command command = new Command(command_id, device_id, command_desc, value);
                commands.add(command);
                Device d = deviceMap.get(device_id);
                command.setDevice(deviceMap.get(d));
                command.setPet(d.getPet());
            }
        } catch (SQLException e ) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return commands;
    }

    public Map<String, String> getBackpackData(int backpack) throws SQLException {
        Statement stmt = null;
        Map<String, String> result = new HashMap<String, String>();
        if (backpack != -1) {
            String query = "select * from watchdog.data WHERE data.device_id = ";
            query += backpack;
            query += " AND (data_desc = 'activity' OR data_desc = 'temperature' OR data_desc = 'humidity')";
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

    public void removeCommand(Command com) throws SQLException {

        String query = "DELETE FROM watchdog.commands " +
                "WHERE command_id = " + com.getCommand_id();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query); // use 'executedUpdate' when inserting data into db
        } catch (SQLException e ) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public int getCurrentWeight(int feeder) throws SQLException {
        Statement stmt = null;
        int result = -1;
        if (feeder != -1) {
            String query = "select * from watchdog.data WHERE data.device_id = ";
            query += feeder;
            query += " AND data.data_desc = 'weight-now'";
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    result = rs.getInt("VALUE");
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

    public List<WeightResult> getFeederData(int feeder) throws SQLException {
        Statement stmt = null;
        List<WeightResult> result = new ArrayList<WeightResult>();
        if (feeder != -1) {
            String query = "select * from watchdog.data WHERE data.device_id = ";
            query += feeder;
            query += " AND data_desc='weight'";
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("updated_at");
                    String date = new SimpleDateFormat("MM/dd").format(ts);
                    WeightResult weightResult = new WeightResult(date, rs.getInt("VALUE"));
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

    public List<Float> getPrediction(int backpack) throws SQLException {
        Statement stmt = null;
        List<Float> result = new ArrayList<Float>(24);
        if (backpack != -1) {
            String query = "select * from watchdog.data WHERE data.device_id = ";
            query += backpack;
            query += " AND data_desc='prediction'";
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Integer hour = (Integer) rs.getInt("hour");
                    result.add(hour, rs.getFloat("VALUE"));
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
