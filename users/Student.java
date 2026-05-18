package users;

import java.io.*;
import java.util.*;
import academic.*; 

public class Student extends User {
    public double GPA = 0.0;
    public int yearOfStudy;
    public int totalCredits = 0;
    public String major;
    public boolean isResearcher;
    public List<Course> enrolledCourses = new ArrayList<>();
    public Map<Course, List<Mark>> marks = new HashMap<>();
    public java.util.HashMap<academic.Course, academic.CourseProgress> courseProgressMap = new java.util.HashMap<>();

    public Student() {
        super();
    }

    public double calculateGPA() {
        if (courseProgressMap.isEmpty()) {
            this.GPA = 0.0;
            return this.GPA;
        }

        double totalPoints = 0.0;
        int totalCreditsCounted = 0;

        for (java.util.Map.Entry<academic.Course, academic.CourseProgress> entry : courseProgressMap.entrySet()) {
            academic.Course course = entry.getKey();
            academic.CourseProgress progress = entry.getValue();

            if (progress.isFinalPassed) {
                totalPoints += progress.getGPAContribution() * course.credits;
                totalCreditsCounted += course.credits;
            }
        }

        if (totalCreditsCounted == 0) {
            this.GPA = 0.0;
            return this.GPA;
        }
        this.GPA = totalPoints / totalCreditsCounted;
        return this.GPA;
    }

    public void registerToCourse(Course c) {
        int maxCredits = 21;
        if (totalCredits + c.credits > maxCredits) {
            System.out.println("Cannot register: Credit limit exceeded!");
            return;
        }
        
        if (enrolledCourses.contains(c)) {
            System.out.println("You are already registered for this course.");
            return;
        }

        if (!c.getPendingStudents().contains(this)) {
            c.getPendingStudents().add(this); 
            System.out.println("Application for course '" + c.courseName + "' has been sent to the manager for review.");
        } else {
            System.out.println("You have already applied for this course, please wait for approval.");
        }
    }

    public void rateTeachers(java.util.Scanner scanner) {
        datastorage.DataStorage db = datastorage.DataStorage.getInstance();
        System.out.println("\n========================================");
        System.out.println("          FACULTY EVALUATION            ");
        System.out.println("========================================");

        if (this.courseProgressMap.isEmpty()) {
            System.out.println("You are not registered for any course. Evaluation is unavailable.");
            return;
        }
        java.util.List<Teacher> myTeachers = new java.util.ArrayList<>();
        
        for (academic.Course c : this.courseProgressMap.keySet()) {
            for (User u : db.getUsers()) {
                if (u instanceof Teacher) {
                    Teacher t = (Teacher) u;
                    if (t.courses.contains(c) && !myTeachers.contains(t)) {
                        myTeachers.add(t);
                    }
                }
            }
        }

        if (myTeachers.isEmpty()) {
            System.out.println("No teachers assigned to your courses yet.");
            return;
        }

        System.out.println("Available teachers:");
        java.util.Set<String> alreadyVoted = db.getVotedPairs().getOrDefault(this.getEmail(), new java.util.HashSet<>());

        for (int i = 0; i < myTeachers.size(); i++) {
            Teacher t = myTeachers.get(i);
            String status = alreadyVoted.contains(t.getEmail()) ? " Already evaluated" : "";
            System.out.printf("[%d] %s %s %s\n", i + 1, t.getFirstName(), t.getLastName(), status);
        }

        System.out.print("\nSelect teacher number for evaluation (or 0 to exit): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Please enter a valid number.");
            return;
        }
        
        if (choice <= 0 || choice > myTeachers.size()) return;

        Teacher selectedTeacher = myTeachers.get(choice - 1);
        if (alreadyVoted.contains(selectedTeacher.getEmail())) {
            System.out.println("You have already evaluated this teacher. Duplicate submission rejected.");
            return;
        }

        System.out.println("\nEvaluation for: " + selectedTeacher.getFirstName() + " " + selectedTeacher.getLastName());
        System.out.println("Enter scores from 1 to 5:");

        try {
            System.out.print("1. Teaching Methods: ");
            double methods = Double.parseDouble(scanner.nextLine());
            
            System.out.print("2. Fairness: ");
            double fairness = Double.parseDouble(scanner.nextLine());
            
            System.out.print("3. Apoliticality: ");
            double apolitical = Double.parseDouble(scanner.nextLine());

            if (methods < 1 || methods > 5 || fairness < 1 || fairness > 5 || apolitical < 1 || apolitical > 5) {
                System.out.println("Scores must be strictly between 1 and 5!");
                return;
            }

            selectedTeacher.addAnonymousRating(methods, fairness, apolitical);
            db.getVotedPairs().computeIfAbsent(this.getEmail(), k -> new java.util.HashSet<>()).add(selectedTeacher.getEmail());
            db.addLog("Student " + this.getEmail() + " completed confidential faculty evaluation for teacher " + selectedTeacher.getEmail());

            datastorage.DataStorage.saveData();
            System.out.println("Your responses have been accepted anonymously. Thank you for participating!");

        } catch (Exception e) {
            System.out.println("Invalid numeric format for scores.");
        }
    }

    public void viewMarks() {
        if (marks.isEmpty()) {
            System.out.println("No marks available yet.");
            return;
        }
        System.out.println("Marks for " + getFirstName() + " " + getLastName() + ":");
        for (Course c : enrolledCourses) {
            System.out.print(c.courseName + ": ");
            System.out.println(marks.getOrDefault(c, new ArrayList<>()));
        }
    }

    public void getTranscript() {
        System.out.println("------ TRANSCRIPT ------");
        System.out.println("Student: " + getFirstName() + " " + getLastName());
        System.out.println("Major: " + major);
        for (Course c : enrolledCourses) {
            System.out.println(c.courseName + " | Credits: " + c.credits + " | GPA Contribution: ...");
        }
        System.out.println("Total Credits: " + totalCredits);
        System.out.println("Current GPA: " + GPA);
        System.out.println("------------------------");
    }

    public void rateTeachers(Teacher t, int score) {
        System.out.println("Rated teacher " + t.getLastName() + " with score: " + score);
    }

    @Override
    public String toString() {
        return super.toString() + " | GPA: " + GPA + " | Year: " + yearOfStudy;
    }
}
