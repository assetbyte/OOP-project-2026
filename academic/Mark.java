package academic;

import java.io.Serializable;

public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;

    public double attestation1;
    public double attestation2;
    public double finalExam;
    public double totalScore;
    public String letterGrade;

    public Mark() {
        this.letterGrade = "No Grade";
    }

    public Mark(double attestation1, double attestation2, double finalExam) {
        this.attestation1 = attestation1;
        this.attestation2 = attestation2;
        this.finalExam = finalExam;
        calculateTotal();
    }

    public void calculateTotal() {
        this.totalScore = this.attestation1 + this.attestation2 + this.finalExam;
        toLetter(); 
    }

    public void toLetter() {
        if (totalScore >= 95) letterGrade = "A";
        else if (totalScore >= 90) letterGrade = "A-";
        else if (totalScore >= 85) letterGrade = "B+";
        else if (totalScore >= 80) letterGrade = "B";
        else if (totalScore >= 75) letterGrade = "B-";
        else if (totalScore >= 70) letterGrade = "C+";
        else if (totalScore >= 65) letterGrade = "C";
        else if (totalScore >= 60) letterGrade = "C-";
        else if (totalScore >= 55) letterGrade = "D+";
        else if (totalScore >= 50) letterGrade = "D";
        else letterGrade = "F";
    }

    @Override
    public String toString() {
        return String.format("[Att1: %.1f, Att2: %.1f, Final: %.1f -> Total: %.1f (%s)]", 
                attestation1, attestation2, finalExam, totalScore, letterGrade);
    }
}