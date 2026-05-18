package users;

import enums.*;
import java.io.*;
import java.util.*;
import academic.*; 
import datastorage.DataStorage;
import exceptions.NotAResearcherException;

public class Manager extends Employee {

    public ManagerType type;

    public Manager() {
        super();
    }

    public void approveRegistration(Student s, Course c) {
        approveRegistration(c, s);
    }

    public void addCourse(Course c) {
        DataStorage.getInstance().courses.add(c);
        System.out.println("New course added to the system: " + c.courseName);
    }

    public void assignTeacher(Teacher t, Course c) {
        if (!t.courses.contains(c)) {
            t.courses.add(c);
            System.out.println("Teacher " + t.getFirstName() + " " + t.getLastName() + " is now teaching " + c.courseName);
        }
    }

    public void viewStudents() {
        List<User> allUsers = DataStorage.getInstance().getUsers();
        System.out.println("List of Students:");
        for (User u : allUsers) {
            if (u instanceof Student) {
                System.out.println("- " + u.getFirstName() + " " + u.getLastName() + " (" + ((Student) u).major + ")");
            }
        }
    }

    public void createReport() {
        System.out.println("Generating academic report for " + type + " department...");
        System.out.println("Report configuration synchronized successfully at " + new java.util.Date());
    }

    public void viewTeachers() {
        List<User> allUsers = DataStorage.getInstance().getUsers();
        System.out.println("List of Teachers:");
        for (User u : allUsers) {
            if (u instanceof Teacher) {
                System.out.println("- " + u.getFirstName() + " " + u.getLastName() + " | " + ((Teacher) u).title);
            }
        }
    }

    public void viewStudentsMark(Student s) {
        System.out.println("Checking marks for student: " + s.getLastName());
        s.viewMarks(); 
    }

    public void manageCourses() {
        List<Course> allCourses = DataStorage.getInstance().courses;
        System.out.println("Current active courses: " + allCourses.size());
        for (Course c : allCourses) {
            System.out.println("ID: " + c.courseCode + " | Name: " + c.courseName);
        }
    }

    public void removeStudentById(String studentId) {
        datastorage.DataStorage db = datastorage.DataStorage.getInstance();
        User toRemove = null;

        for (User u : db.getUsers()) {
            if (u instanceof Student && u.getId().equals(studentId)) {
                toRemove = u;
                break;
            }
        }

        if (toRemove != null) {
            db.getUsers().remove(toRemove);
            System.out.println("Student " + toRemove.getFirstName() + " " + toRemove.getLastName() + " successfully removed from the database.");
        } else {
            System.out.println("Student with ID " + studentId + " not found.");
        }
    }

    public void approveRegistration(Course c, Student s) {
        if (c.getPendingStudents().contains(s)) {
            c.getPendingStudents().remove(s); 
            
            if (!c.getRegisteredStudents().contains(s)) {
                c.getRegisteredStudents().add(s); 
                
                if (s.courseProgressMap == null) {
                    s.courseProgressMap = new java.util.HashMap<>();
                }
                s.courseProgressMap.put(c, new academic.CourseProgress()); 
                
                s.totalCredits += c.credits;       
            }
            System.out.println("Student " + s.getFirstName() + " successfully registered for course " + c.courseName);
        } else {
            System.out.println("This student did not apply for this course.");
        }
    }

    public void createResearchProject(String topic) {
        datastorage.DataStorage db = datastorage.DataStorage.getInstance();
        db.projects.add(new research.ResearchProject(topic));
        System.out.println("New research project '" + topic + "' successfully registered in KBTU.");
    }

    public void approveResearcherToProject(research.ResearchProject p, User user) {
        if (p.getPendingResearchers().contains(user)) {
            p.getPendingResearchers().remove(user); 
            try {
                p.addParticipant(user); 
                System.out.println("User " + user.getFirstName() + " successfully assigned to project: " + p.topic);
            } catch (NotAResearcherException e) {
                System.out.println("Approval rejected: " + e.getMessage());
                p.getPendingResearchers().add(user);
            }
        } else {
            System.out.println("This user did not apply for this project.");
        }
    }

    public void createNews(String title, String content) {
        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            System.out.println("News title and content cannot be empty.");
            return;
        }
        
        datastorage.DataStorage db = datastorage.DataStorage.getInstance();
        academic.News newPost = new academic.News(title, content, this);
        
        db.newsList.add(newPost);
        db.addLog("Manager " + this.getEmail() + " published new university announcement: " + title);
        
        System.out.println("University announcement successfully published to the KBTU feed.");
    }

    public void removeResearcherFromProject(research.ResearchProject p, String researcherId) {
        research.Researcher toRemove = null;
        for (research.Researcher r : p.participants) {
            User u = (User) r;
            if (u.getId().equals(researcherId)) {
                toRemove = r;
                break;
            }
        }
        if (toRemove != null) {
            p.removeParticipant(toRemove); 
            User u = (User) toRemove;
            u.getResearchProfile().projects.remove(p);
            System.out.println("Participant " + u.getFirstName() + " successfully excluded.");
        } else {
            System.out.println("Researcher with ID " + researcherId + " not found.");
        }
    }
}
