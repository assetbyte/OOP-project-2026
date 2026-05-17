package datastorage;

import java.io.*;
import java.util.*;
import users.User;
import academic.Course;

public class DataStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Set<String>> votedPairs = new HashMap<>();
    private static DataStorage instance;
    private List<String> systemLogs = new ArrayList<>();
    public List<academic.News> newsList = new ArrayList<>();
    private static final String FILE_PATH = "mini_wsp_db.ser";

    private List<User> users = new ArrayList<>();
    public List<research.ResearchProject> projects = new ArrayList<>();
    public List<Course> courses = new ArrayList<>();

    private DataStorage() {}

    public static synchronized DataStorage getInstance() {
        if (instance == null) {
            instance = loadData();
        }
        return instance;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<String> getSystemLogs() {
        if (this.systemLogs == null) {
            this.systemLogs = new ArrayList<>();
        }
        return this.systemLogs;
    }

    public void addLog(String action) {
        getSystemLogs().add(new java.util.Date() + ": " + action);
    }

    public Map<String, Set<String>> getVotedPairs() {
        if (this.votedPairs == null) {
            this.votedPairs = new HashMap<>();
        }
        return this.votedPairs;
    }

    public User getUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(getInstance());
            System.out.println("Data successfully saved to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Failed to save data: " + e.getMessage());
        }
    }

    public static DataStorage loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Database file not found. Initialized clean database instance.");
            return new DataStorage();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            System.out.println("Data successfully loaded from file.");
            return (DataStorage) ois.readObject();
        } catch (Exception e) {
            System.out.println("Failed to read database file. Initialized clean instance. Exception: " + e.getMessage());
            return new DataStorage();
        }
    }
}