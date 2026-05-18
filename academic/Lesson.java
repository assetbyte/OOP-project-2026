package academic;
import enums.*;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Lesson implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public Lesson() {
    }

    /**
     * 
     */
    public LessonType type;

    /**
     * 
     */
    public Date date;

}
