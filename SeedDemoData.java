import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import academic.Course;
import academic.CourseProgress;
import academic.LessonMark;
import datastorage.DataStorage;
import enums.ManagerType;
import enums.TeacherTitle;
import research.ResearchPaper;
import research.ResearchProject;
import users.Admin;
import users.Employee;
import users.Manager;
import users.Student;
import users.Teacher;
import users.User;

public class SeedDemoData {
    public static void main(String[] args) {
        DataStorage db = DataStorage.getInstance();
        resetDatabase(db);

        Admin mainAdmin = createAdmin("Aida", "Sarsen", "admin@kbtu.kz", "admin123", "IT-Support");
        Admin backupAdmin = createAdmin("Nur", "Bek", "admin2@kbtu.kz", "admin123", "Security");

        Manager orManager = createManager("Maksat", "Ibragim", "manager.or@kbtu.kz", "manager123", "OR", ManagerType.OR);
        Manager depManager = createManager("Dana", "Akhmet", "manager.dep@kbtu.kz", "manager123", "CS Department", ManagerType.DEPARTMENT);

        Teacher profTeacher = createTeacher("Erlan", "Tulegenov", "teacher.prof@kbtu.kz", "teacher123", "CSSE", TeacherTitle.PROFESSOR);
        Teacher seniorTeacher = createTeacher("Ainur", "Zhumabayeva", "teacher.senior@kbtu.kz", "teacher123", "IS", TeacherTitle.SENIOR_LECTOR);
        Teacher lecturerTeacher = createTeacher("Timur", "Sadykov", "teacher.lecturer@kbtu.kz", "teacher123", "DS", TeacherTitle.LECTURER);

        Student s1 = createStudent("Aliya", "Kenes", "student1@kbtu.kz", "student123", "CSSE", 2);
        Student s2 = createStudent("Dias", "Omir", "student2@kbtu.kz", "student123", "IS", 3);
        Student s3 = createStudent("Miras", "Nazar", "student3@kbtu.kz", "student123", "CSSE", 4);
        Student s4 = createStudent("Aruzhan", "Bektur", "student4@kbtu.kz", "student123", "DS", 1);

        Employee labEngineer = new Employee();
        labEngineer.setFirstName("Olzhas");
        labEngineer.setLastName("Rysbay");
        labEngineer.setEmail("employee.researcher@kbtu.kz");
        labEngineer.setPassword("employee123");
        labEngineer.department = "Research Office";
        labEngineer.activateResearchProfile();

        db.getUsers().addAll(Arrays.asList(
                mainAdmin, backupAdmin,
                orManager, depManager,
                profTeacher, seniorTeacher, lecturerTeacher,
                s1, s2, s3, s4,
                labEngineer));

        Course oop = new Course("CS201", "Object-Oriented Programming", 5, Arrays.asList("CSSE", "IS"));
        Course dbms = new Course("IS202", "Database Systems", 5, Arrays.asList("IS", "CSSE"));
        Course ai = new Course("CS405", "AI for Research", 6, Arrays.asList("CSSE", "DS"));
        Course calc = new Course("DS101", "Calculus I", 4, Arrays.asList("DS"));
        db.courses.addAll(Arrays.asList(oop, dbms, ai, calc));

        assignTeacher(depManager, profTeacher, oop);
        assignTeacher(depManager, seniorTeacher, oop);
        assignTeacher(depManager, seniorTeacher, dbms);
        assignTeacher(depManager, lecturerTeacher, calc);
        assignTeacher(depManager, profTeacher, ai);

        approveStudentForCourse(orManager, s1, oop);
        approveStudentForCourse(orManager, s1, dbms);
        approveStudentForCourse(orManager, s2, oop);
        approveStudentForCourse(orManager, s2, dbms);
        approveStudentForCourse(orManager, s3, oop);
        approveStudentForCourse(orManager, s3, ai);
        approveStudentForCourse(orManager, s4, calc);

        addProgress(s1, oop, new double[] {3.5, 3.7, 4.0, 3.8, 3.9}, 34.0, true);
        addProgress(s1, dbms, new double[] {2.5, 2.8, 3.0}, 0.0, false);
        addProgress(s2, oop, new double[] {3.0, 2.7, 2.9, 3.1}, 28.0, true);
        addProgress(s2, dbms, new double[] {2.0, 1.5, 1.8, 2.2}, 18.0, true);
        addProgress(s3, oop, new double[] {4.0, 4.0, 3.9, 4.0}, 39.0, true);
        addProgress(s3, ai, new double[] {3.8, 3.6, 3.9}, 0.0, false);
        addProgress(s4, calc, new double[] {3.0, 3.2, 3.1}, 24.0, true);

        // Pending request (for manager moderation demo)
        dbms.getPendingStudents().add(s4);

        // Ratings and anti-duplicate map demo
        profTeacher.addAnonymousRating(5, 5, 5);
        profTeacher.addAnonymousRating(4, 5, 5);
        seniorTeacher.addAnonymousRating(4, 4, 5);
        db.getVotedPairs().computeIfAbsent(s1.getEmail(), k -> new java.util.HashSet<>()).add(profTeacher.getEmail());

        // Messaging/inbox demo
        depManager.sendMessage(profTeacher, "Please finalize AI course syllabus.");
        profTeacher.sendMessage(depManager, "Syllabus draft is ready.");
        backupAdmin.sendMessage(s2, "Please update your profile phone number.");

        // Research module demo
        profTeacher.activateResearchProfile();
        seniorTeacher.activateResearchProfile();
        s3.activateResearchProfile();

        ResearchProject rp1 = new ResearchProject("AI for Education Analytics");
        ResearchProject rp2 = new ResearchProject("Data Engineering in Smart Campus");
        db.projects.addAll(Arrays.asList(rp1, rp2));

        rp1.addParticipant(profTeacher);
        rp1.addParticipant(seniorTeacher);
        rp1.addParticipant(s3);
        rp1.addParticipant(labEngineer);

        rp2.addParticipant(seniorTeacher);
        rp2.addParticipant(labEngineer);

        addPaperToProject(rp1, "Adaptive AI Tutoring with Explainable Models", 26, "10.1000/xyz123", profTeacher, seniorTeacher, s3);
        addPaperToProject(rp1, "Student Success Prediction on University Data", 15, "10.1000/xyz124", profTeacher, labEngineer);
        addPaperToProject(rp2, "Event-Driven Data Pipeline for Campus Systems", 9, "10.1000/xyz125", seniorTeacher, labEngineer);

        // Pending research applications (for manager moderation demo)
        rp2.getPendingResearchers().add(s1);
        rp2.getPendingResearchers().add(lecturerTeacher);

        // News + logs demo
        depManager.createNews("Research Week", "Join Research Week activities in the main hall.");
        depManager.createNews("Course Registration Reminder", "Registration deadline is Friday 18:00.");
        db.addLog("Demo dataset generated by SeedDemoData.");

        DataStorage.saveData();
        System.out.println("Demo database generated successfully: mini_wsp_db.ser");
        printSummary(db);
    }

    private static void resetDatabase(DataStorage db) {
        db.getUsers().clear();
        db.courses.clear();
        db.projects.clear();
        db.newsList.clear();
        db.getSystemLogs().clear();
        db.getVotedPairs().clear();
    }

    private static Admin createAdmin(String fn, String ln, String email, String pass, String dept) {
        Admin a = new Admin();
        a.setFirstName(fn);
        a.setLastName(ln);
        a.setEmail(email);
        a.setPassword(pass);
        a.department = dept;
        return a;
    }

    private static Manager createManager(String fn, String ln, String email, String pass, String dept, ManagerType type) {
        Manager m = new Manager();
        m.setFirstName(fn);
        m.setLastName(ln);
        m.setEmail(email);
        m.setPassword(pass);
        m.department = dept;
        m.type = type;
        return m;
    }

    private static Teacher createTeacher(String fn, String ln, String email, String pass, String specialty, TeacherTitle title) {
        Teacher t = new Teacher();
        t.setFirstName(fn);
        t.setLastName(ln);
        t.setEmail(email);
        t.setPassword(pass);
        t.specialty = specialty;
        t.title = title;
        t.department = "CS";
        return t;
    }

    private static Student createStudent(String fn, String ln, String email, String pass, String major, int year) {
        Student s = new Student();
        s.setFirstName(fn);
        s.setLastName(ln);
        s.setEmail(email);
        s.setPassword(pass);
        s.major = major;
        s.yearOfStudy = year;
        return s;
    }

    private static void assignTeacher(Manager manager, Teacher teacher, Course course) {
        manager.assignTeacher(teacher, course);
        if (!course.getAssignedTeachers().contains(teacher)) {
            course.getAssignedTeachers().add(teacher);
        }
    }

    private static void approveStudentForCourse(Manager manager, Student student, Course course) {
        if (!course.getPendingStudents().contains(student)) {
            course.getPendingStudents().add(student);
        }
        manager.approveRegistration(course, student);
        if (!student.enrolledCourses.contains(course)) {
            student.enrolledCourses.add(course);
        }
    }

    private static void addProgress(Student s, Course c, double[] dailyScores, double finalExamScore, boolean finalPassed) {
        CourseProgress p = s.courseProgressMap.get(c);
        if (p == null) {
            p = new CourseProgress();
            s.courseProgressMap.put(c, p);
        }
        for (double score : dailyScores) {
            p.dailyMarks.add(new LessonMark(score, "seeded lesson activity"));
        }
        p.finalExamMark = finalExamScore;
        p.isFinalPassed = finalPassed;
        s.calculateGPA();
    }

    private static void addPaperToProject(ResearchProject project, String title, int citations, String doi, User... authors) {
        ResearchPaper paper = new ResearchPaper(title, citations, doi);
        List<User> realAuthors = new ArrayList<>(Arrays.asList(authors));
        for (User author : realAuthors) {
            paper.authors.add(author);
            if (author.getResearchProfile() != null && !author.getResearchProfile().papers.contains(paper)) {
                author.getResearchProfile().papers.add(paper);
            }
        }
        project.addPaper(paper);
    }

    private static void printSummary(DataStorage db) {
        int admins = 0, managers = 0, teachers = 0, students = 0, employees = 0;
        for (User u : db.getUsers()) {
            if (u instanceof Admin) admins++;
            else if (u instanceof Manager) managers++;
            else if (u instanceof Teacher) teachers++;
            else if (u instanceof Student) students++;
            else if (u instanceof Employee) employees++;
        }
        System.out.println("Users total: " + db.getUsers().size()
                + " | Admins: " + admins
                + " | Managers: " + managers
                + " | Teachers: " + teachers
                + " | Students: " + students
                + " | Other employees: " + employees);
        System.out.println("Courses: " + db.courses.size()
                + " | Research projects: " + db.projects.size()
                + " | News: " + db.newsList.size()
                + " | Logs: " + db.getSystemLogs().size());
    }
}
