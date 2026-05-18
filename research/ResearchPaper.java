package research;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResearchPaper implements Serializable {
    private static final long serialVersionUID = 1L;

    public String title;
    public List<Researcher> authors = new ArrayList<>();
    public Date publicationDate;
    public int citations;
    public String doi;

    public ResearchPaper() {
    }

    public ResearchPaper(String title, int citations, String doi) {
        this.title = title;
        this.citations = citations;
        this.doi = doi;
        this.publicationDate = new Date();
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = formatter.format(publicationDate);
        
        return String.format("Paper: '%s' | Published: %s | Citations: %d | DOI: %s", 
                title, formattedDate, citations, doi);
    }

    public int getCitations() {
        return citations;
    }

    public String getTitle() {
        return title;
    }
}
