package academic;

import java.io.Serializable;
import java.util.*;
import users.Student;
import users.Teacher;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Student> pendingStudents = new ArrayList<>();
    public String courseCode;
    public String courseName;
    public int credits;
    private List<String> targetMajors = new ArrayList<>();
    
    private List<Student> registeredStudents = new ArrayList<>();
    private List<Teacher> assignedTeachers = new ArrayList<>();

    public Course(String courseCode, String courseName, int credits, List<String> targetMajors) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.targetMajors = targetMajors;
    }

    public List<String> getTargetMajors() {
        return targetMajors;
    }

    public List<Student> getPendingStudents() {
    return pendingStudents;
    }


    public List<Student> getRegisteredStudents() { return registeredStudents; }
    public List<Teacher> getAssignedTeachers() { return assignedTeachers; }
}