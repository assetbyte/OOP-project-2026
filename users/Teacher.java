package users;

import academic.Mark;
import java.io.Serializable;
import java.util.*;
import academic.Course;
import enums.TeacherTitle;

public class Teacher extends Employee {
    private static final long serialVersionUID = 1L;

    public TeacherTitle title;
    public String specialty; 
    public List<Course> courses = new ArrayList<>();
    private double totalTeachingMethodsScore = 0.0;
    private double totalFairnessScore = 0.0;
    private double totalApoliticalScore = 0.0;
    private int ratingsCount = 0;

    public Teacher() {
        super();
    }

    public void viewCourses() {
        System.out.println("\n=== MY COURSES AND STUDENTS (" + specialty + ") ===");
        if (courses.isEmpty()) {
            System.out.println("No courses assigned to you yet.");
            return;
        }
        for (Course c : courses) {
            System.out.println("\nCourse: " + c.courseCode + " - " + c.courseName + " " + c.getTargetMajors());
            System.out.println("Registered students:");
            for (Student s : c.getRegisteredStudents()) {
                System.out.println("  * " + s.getFirstName() + " " + s.getLastName());
            }
        }
    }

    public void addAnonymousRating(double methods, double fairness, double apolitical) {
        this.totalTeachingMethodsScore += methods;
        this.totalFairnessScore += fairness;
        this.totalApoliticalScore += apolitical;
        this.ratingsCount++;
    }

    public void printRatingStats() {
        System.out.println("\n--- TEACHER RATING PERFORMANCE ---");
        if (ratingsCount == 0) {
            System.out.println("No ratings available yet.");
            return;
        }
        System.out.printf("Teaching Methods: %.2f / 5.0\n", (totalTeachingMethodsScore / ratingsCount));
        System.out.printf("Fairness:         %.2f / 5.0\n", (totalFairnessScore / ratingsCount));
        System.out.printf("Apoliticality:    %.2f / 5.0\n", (totalApoliticalScore / ratingsCount));
        System.out.printf("Total students voted: %d\n", ratingsCount);
    }

    public void putMark(Student s, Course c, double att1, double att2, double fin) {
        if (!courses.contains(c)) {
            System.out.println("You are not teaching this course!");
            return;
        }
        if (!c.getRegisteredStudents().contains(s)) {
            System.out.println("Student is not registered for this course!");
            return;
        }

        List<Mark> studentCourseMarks = s.marks.get(c);
        if (studentCourseMarks == null) {
            studentCourseMarks = new ArrayList<>();
            s.marks.put(c, studentCourseMarks);
        }

        Mark newMark = new Mark(att1, att2, fin);
        studentCourseMarks.add(newMark);

        System.out.println("Marks assigned for student " + s.getFirstName() + " " + s.getLastName() + 
                           ". Total: " + newMark.totalScore + " (" + newMark.letterGrade + ")");
    }
}