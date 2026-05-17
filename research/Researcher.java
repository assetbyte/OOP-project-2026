package research;

import java.util.Comparator;

public interface Researcher {
    
    void printPapers(Comparator<ResearchPaper> c);
    
    void calculateHIndex();

    void joinProject(ResearchProject p);
}