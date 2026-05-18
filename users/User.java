package users;

import research.Researcher;
import java.io.Serializable;
import research.ResearchProfile;
import datastorage.DataStorage;
import exceptions.*;

public abstract class User implements Serializable, research.Researcher {
    private static final long serialVersionUID = 1L;
    private java.util.List<String> inbox = new java.util.ArrayList<>();
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private ResearchProfile researchProfile = null; 

    public User() {
        this.id = String.valueOf((int)(Math.random() * 9000) + 1000);
    }

    public User(String id, String email, String firstName, String lastName, String password) {
        this.id = (id != null && !id.trim().isEmpty())
                ? id
                : String.valueOf((int) (Math.random() * 9000) + 1000);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public boolean login(String inputEmail, String inputPassword) 
            throws UserNotFoundException, InvalidCredentialsException {
        DataStorage db = DataStorage.getInstance();
        User foundUser = db.getUserByEmail(inputEmail); 
        
        if (foundUser == null) {
            throw new UserNotFoundException("User with email " + inputEmail + " not found!");
        }
        if (!foundUser.getPassword().equals(inputPassword)) {
            throw new InvalidCredentialsException("Invalid password!");
        }
        return true;
    }

    public boolean isResearcher() {
        return this.researchProfile != null;
    }

    public void activateResearchProfile() {
        if (this.researchProfile == null) {
            this.researchProfile = new ResearchProfile();
            System.out.println("Research profile successfully activated!");
        }
    }

    public java.util.List<String> getInbox() {
        if (this.inbox == null) {
            this.inbox = new java.util.ArrayList<>();
        }
        return this.inbox;
    }

    public void receiveMessage(String message) {
        getInbox().add(message);
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public ResearchProfile getResearchProfile() { return researchProfile; }
    
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return this.phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "[" + id + "] " + firstName + " " + lastName + " (" + email + ")";
    }

    @Override
    public void printPapers(java.util.Comparator<research.ResearchPaper> c) {
        if (isResearcher()) this.getResearchProfile().printPapers(c);
    }

    @Override
    public void calculateHIndex() {
        if (isResearcher()) this.getResearchProfile().calculateHIndex();
    }

    @Override
    public void joinProject(research.ResearchProject p) {
        if (isResearcher()) {
            if (!this.getResearchProfile().projects.contains(p)) {
                this.getResearchProfile().projects.add(p);
            }
        }
    }
}
