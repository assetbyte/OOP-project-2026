package academic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LessonMark implements Serializable {
    private static final long serialVersionUID = 1L;

    public double score;       
    public Date date;          
    public String comment;     

    public LessonMark(double score, String comment) {
        this.score = score;
        this.comment = comment;
        this.date = new Date(); 
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return String.format("[%s] Score: %.1f | Note: %s", formatter.format(date), score, comment);
    }
}