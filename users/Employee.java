package users;

import java.io.*;
import java.util.*;
import datastorage.DataStorage;

public class Employee extends User {
    public double salary;
    public String department;
    protected List<String> messageHistory = new ArrayList<>();

    public Employee() {
        super();
    }

    public void sendMessage(User receiver, String text) {
        if (receiver != null && text != null && !text.trim().isEmpty()) {
            String formattedMessage = "[" + new java.util.Date() + "] From " + this.getEmail() + ": " + text;
            
            receiver.receiveMessage(formattedMessage);
            messageHistory.add("To " + receiver.getEmail() + ": " + text);
            
            datastorage.DataStorage.getInstance().addLog("Employee " + this.getEmail() + " sent a message to " + receiver.getEmail());
            
            System.out.println("Message for " + receiver.getEmail() + " successfully delivered.");
        } else {
            System.out.println("Failed to send message. Please verify input data.");
        }
    }

    public void viewMessageHistory() {
        System.out.println("Message history for " + getFirstName() + " " + getLastName() + ":");
        if (messageHistory.isEmpty()) {
            System.out.println("No messages sent yet.");
        } else {
            for (String msg : messageHistory) {
                System.out.println("- " + msg);
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + " | Dept: " + department + " | Salary: " + salary;
    }
}
