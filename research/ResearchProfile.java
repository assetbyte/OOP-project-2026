package research;

import java.io.Serializable;
import java.util.*;

public class ResearchProfile implements Researcher, Serializable {
    private static final long serialVersionUID = 1L;

    public double hIndex = 0.0;
    public List<ResearchPaper> papers = new ArrayList<>();
    public List<ResearchProject> projects = new ArrayList<>();

    public ResearchProfile() {}

    @Override
    public void printPapers(Comparator<ResearchPaper> c) {
        if (papers.isEmpty()) {
            System.out.println("No research papers published yet.");
            return;
        }
        
        List<ResearchPaper> sortedPapers = new ArrayList<>(papers);
        sortedPapers.sort(c);
        
        System.out.println("--- List of Research Papers ---");
        for (ResearchPaper p : sortedPapers) {
            System.out.println(p);
        }
    }

    @Override
    public void calculateHIndex() {
        if (papers.isEmpty()) {
            this.hIndex = 0;
            System.out.println("Your H-Index: 0 (No published papers)");
            return;
        }
        
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort((p1, p2) -> p2.citations - p1.citations);
        
        int h = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).citations >= (i + 1)) {
                h = i + 1;
            } else {
                break;
            }
        }
        this.hIndex = h;
        System.out.println("Your Hirsch Index (H-Index) has been recalculated: " + this.hIndex);
    }

    @Override
    public void joinProject(ResearchProject p) {
        if (!projects.contains(p)) {
            projects.add(p);
        }
    }
}