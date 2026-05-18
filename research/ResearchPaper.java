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
    public String journal;
    public int startPage;
    public int endPage;

    public ResearchPaper() {
    }

    public ResearchPaper(String title, int citations, String doi) {
        this(title, citations, doi, "KBTU Journal", 1, 1, new Date());
    }

    public ResearchPaper(String title, int citations, String doi, String journal, int startPage, int endPage) {
        this(title, citations, doi, journal, startPage, endPage, new Date());
    }

    public ResearchPaper(String title, int citations, String doi, String journal, int startPage, int endPage, Date publicationDate) {
        this.title = title;
        this.citations = citations;
        this.doi = doi;
        this.journal = (journal == null || journal.trim().isEmpty()) ? "KBTU Journal" : journal;
        this.startPage = Math.max(1, startPage);
        this.endPage = Math.max(this.startPage, endPage);
        this.publicationDate = (publicationDate == null) ? new Date() : publicationDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = formatter.format(publicationDate);
        
        return String.format("Paper: '%s' | Journal: %s | Published: %s | Pages: %d-%d (%d p.) | Citations: %d | DOI: %s", 
                title, journal, formattedDate, startPage, endPage, getArticleLengthInPages(), citations, doi);
    }

    public int getCitations() {
        return citations;
    }

    public String getTitle() {
        return title;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public int getArticleLengthInPages() {
        return Math.max(1, endPage - startPage + 1);
    }
}
