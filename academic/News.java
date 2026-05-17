package academic;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import users.Manager;

public class News implements Serializable {
    private static final long serialVersionUID = 1L;

    public String title;
    public String content;
    public Manager author;
    public Date date;

    public News(String title, String content, Manager author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = new Date();
    }

    public void printNewsDetails() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        System.out.println("\n----------------------------------------");
        System.out.println("Title: " + title);
        System.out.println("Date:  " + formatter.format(date));
        System.out.println("By:    " + author.getFirstName() + " " + author.getLastName());
        System.out.println("----------------------------------------");
        System.out.println(content);
        System.out.println("----------------------------------------");
    }
}