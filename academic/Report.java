package academic;

import java.io.Serializable;
import java.util.Date;

public class Report implements Serializable {
    private static final long serialVersionUID = 1L;

    public String courseCode;
    public String courseName;
    public int totalStudents;
    public int failedStudents;
    public double avgMarkPerCourse;
    public Date date;

    public Report(String courseCode, String courseName, int totalStudents, int failedStudents, double avgMarkPerCourse) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.totalStudents = totalStudents;
        this.failedStudents = failedStudents;
        this.avgMarkPerCourse = avgMarkPerCourse;
        this.date = new Date();
    }

    public void printReportDetails() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm");
        System.out.println("\n========================================");
        System.out.println("          ACADEMIC COURSE REPORT        ");
        System.out.println("========================================");
        System.out.println("Course:       " + courseCode + " - " + courseName);
        System.out.println("Generated on: " + formatter.format(date));
        System.out.println("----------------------------------------");
        System.out.println("Total Enrolled Students:  " + totalStudents);
        System.out.println("Total Failed Students:    " + failedStudents);
        System.out.printf("Course Average Mark:      %.2f\n", avgMarkPerCourse);
        System.out.println("========================================");
    }
}