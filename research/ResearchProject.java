package research;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResearchProject implements Serializable {

    public String topic;
    public List<ResearchPaper> publishedPapers = new ArrayList<>();
    public List<Researcher> participants = new ArrayList<>();
    
    public List<Researcher> pendingResearchers = new ArrayList<>(); 
    public Date date;

    public ResearchProject() {
        this.date = new Date();
    }

    public ResearchProject(String topic) {
        this();
        this.topic = topic;
    }
    
    
    public List<Researcher> getPendingResearchers() {
        return pendingResearchers;
    }
    
    public void addParticipant(Researcher r) {
        if (r != null && !participants.contains(r)) {
            participants.add(r);
            r.joinProject(this); 
            System.out.println("Researcher added to project: " + topic);
        }
    }

    public void removeParticipant(Researcher r) {
        if (participants.remove(r)) {
            System.out.println("Researcher removed from project: " + topic);
        }
    }

    public void addPaper(ResearchPaper p) {
        if (p != null && !publishedPapers.contains(p)) {
            publishedPapers.add(p);
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = formatter.format(date);
        
        return "Project: " + topic + " | Started: " + formattedDate + " | Participants: " + participants.size() +  " | Papers: " + publishedPapers.size();
    }

    public String getProjectName() {
        return topic;
    }
}