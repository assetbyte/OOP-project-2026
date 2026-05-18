package users;

import java.io.*;
import java.util.*;
import datastorage.DataStorage;

public class Admin extends Employee {
    private static final long serialVersionUID = 1L;

    public Admin() {
        super();
    }

    public void addUser(User u) {
        if (u != null) {
            if (u.getEmail() == null || u.getEmail().trim().isEmpty()) {
                System.out.println("User email cannot be empty.");
                return;
            }
            if (DataStorage.getInstance().getUserByEmail(u.getEmail()) != null) {
                System.out.println("User with email " + u.getEmail() + " already exists.");
                return;
            }
            DataStorage.getInstance().getUsers().add(u);
            DataStorage.getInstance().addLog("Admin added user: " + u.getEmail() + " (" + u.getClass().getSimpleName() + ")");
            System.out.println("User " + u.getEmail() + " successfully added to the system.");
        }
    }

    public void removeUser(User u) {
        boolean removed = DataStorage.getInstance().getUsers().remove(u);
        if (removed) {
            DataStorage.getInstance().addLog("Admin removed user: " + u.getEmail());
            System.out.println("User " + u.getEmail() + " removed from the system.");
        } else {
            System.out.println("User not found.");
        }
    }

    public void seeLogs() {
        System.out.println("\n--- CENTRAL SYSTEM LOG ARCHIVE (TOTAL LOGS: " + DataStorage.getInstance().getSystemLogs().size() + ") ---");
        if (DataStorage.getInstance().getSystemLogs().isEmpty()) {
            System.out.println("System logs are empty.");
        } else {
            for (String log : DataStorage.getInstance().getSystemLogs()) {
                System.out.println(log);
            }
        }
        System.out.println("--------------------------------------------------");
    }

    @Override
    public String toString() {
        return "Administrator: " + getFirstName() + " " + getLastName() + " (Department: " + department + ")";
    }
}
