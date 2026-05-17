package academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseProgress implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<LessonMark> dailyMarks = new ArrayList<>();
    public double finalExamMark = 0.0;
    public boolean isFinalPassed = false;

    public double getCurrentTermScore() {
        double sum = 0;
        for (LessonMark m : dailyMarks) {
            sum += m.score;
        }
        return Math.min(sum, 60.0);
    }

    public double getTotalScore() {
        return getCurrentTermScore() + finalExamMark;
    }
    public String getLetterGrade() {
        double total = getTotalScore();
        if (total >= 95) return "A";
        if (total >= 90) return "A-";
        if (total >= 85) return "B+";
        if (total >= 80) return "B";
        if (total >= 75) return "B-";
        if (total >= 70) return "C+";
        if (total >= 65) return "C";
        if (total >= 60) return "C-";
        if (total >= 55) return "D+";
        if (total >= 50) return "D";
        return "F";
    }

    public double getGPAContribution() {
        String letter = getLetterGrade();
        switch (letter) {
            case "A":  return 4.0;
            case "A-": return 3.67;
            case "B+": return 3.33;
            case "B":  return 3.0;
            case "B-": return 2.67;
            case "C+": return 2.33;
            case "C":  return 2.0;
            case "C-": return 1.67;
            case "D+": return 1.33;
            case "D":  return 1.0;
            default:   return 0.0;
        }
    }
}