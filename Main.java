import java.util.Scanner;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import datastorage.DataStorage;
import users.*;
import academic.Course;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DataStorage db = DataStorage.getInstance();

        
        if (db.getUsers().isEmpty()) {
            System.out.println("[System] Первый запуск. Создание главного аккаунта Администратора...");
            Admin mainAdmin = new Admin();
            mainAdmin.setFirstName("Главный");
            mainAdmin.setLastName("Администратор");
            mainAdmin.setEmail("admin@kbtu.kz"); 
            mainAdmin.setPassword("admin123");
            mainAdmin.department = "IT-Support";
            
            db.getUsers().add(mainAdmin);
            DataStorage.saveData();
            
            System.out.println("[System] Дефолтный Администратор успешно создан!");
            System.out.println("         Логин: admin@kbtu.kz | Пароль: admin123");
            System.out.println("==================================================");
        }

        
        while (true) {
            System.out.println("========================================");
            System.out.println("     УНИВЕРСИТЕТСКАЯ СИСТЕМА КБТУ       ");
            System.out.println("========================================");
            System.out.println("Вход как:");
            System.out.println("1. Менеджер");
            System.out.println("2. Студент");
            System.out.println("3. Преподаватель (Teacher)");
            System.out.println("4. Администратор (Admin)"); 
            System.out.println("5. Выйти из программы (Сохранить всё)"); 
            System.out.print("Выберите роль: ");
            
            String roleChoice = scanner.nextLine();
            
            if (roleChoice.equals("5")) {
                DataStorage.saveData();
                System.out.println("Данные успешно сохранены. Программа завершена.");
                break;
            }

            switch (roleChoice) {
                case "1":
                    handleLoginAndMenu(Manager.class);
                    break;
                case "2":
                    handleStudentEntrance();
                    break;
                case "3":
                    handleLoginAndMenu(Teacher.class);
                    break;
                case "4": 
                    handleLoginAndMenu(Admin.class); 
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    
    private static void handleLoginAndMenu(Class<? extends User> targetRole) {
        System.out.print("\nВведите Email: ");
        String email = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        DataStorage db = DataStorage.getInstance();
        User user = db.getUserByEmail(email);

        if (user != null && targetRole.isInstance(user) && user.getPassword().equals(password)) {
            System.out.println("\nУспешный вход! Добро пожаловать, " + user.getFirstName() + " (" + targetRole.getSimpleName() + ")");
            userMainMenu(user);
        } else {
            System.out.println("\n[Ошибка] Неверный email/пароль или у вас нет этой роли.");
        }
    }

    private static void handleStudentEntrance() {
        System.out.println("\n--- РАЗДЕЛ СТУДЕНТА ---");
        System.out.println("1. Войти в существующий аккаунт");
        System.out.println("2. Зарегистрировать новый аккаунт студента");
        System.out.print("Выберите действие: ");
        String choice = scanner.nextLine();

        DataStorage db = DataStorage.getInstance();

        if (choice.equals("1")) {
            handleLoginAndMenu(Student.class);
        } else if (choice.equals("2")) {
            System.out.println("\n--- РЕГИСТРАЦИЯ СТУДЕНТА ---");
            System.out.print("Имя: "); String name = scanner.nextLine();
            System.out.print("Фамилия: "); String lastName = scanner.nextLine();
            System.out.print("Email (будет логином): "); String email = scanner.nextLine();
            System.out.print("Пароль: "); String password = scanner.nextLine();
            System.out.print("Специальность (Major, например CSSE): "); String major = scanner.nextLine();

            if (db.getUserByEmail(email) != null) {
                System.out.println("[Ошибка] Пользователь с таким Email уже существует!");
                return;
            }

            Student newStudent = new Student();
            newStudent.setFirstName(name);
            newStudent.setLastName(lastName);
            newStudent.setEmail(email);
            newStudent.setPassword(password);
            newStudent.major = major;

            db.getUsers().add(newStudent);
            DataStorage.saveData(); 
            System.out.println("[Успех] Аккаунт студента успешно создан с ID: " + newStudent.getId() + "! Теперь вы можете войти.");
        }
    }

    private static void userMainMenu(User user) {
        while (true) {
            User freshUser = DataStorage.getInstance().getUserByEmail(user.getEmail());
            if (freshUser != null) {
                user = freshUser;
            }

            System.out.println("\n--- ГЛАВНОЕ МЕНЮ (" + user.getFirstName() + ") ---");
        
            if (user instanceof Manager) {
                System.out.println("1. Посмотреть все курсы");
                System.out.println("2. Добавить новый курс");
                System.out.println("3. Посмотреть всех студентов");
                System.out.println("4. Добавить нового преподавателя");
                System.out.println("5. Назначить преподавателя на курс");
                System.out.println("6. Удалить студента по ID");
                System.out.println("7. Посмотреть заявки на курсы и одобрить");
                System.out.println("8. Сгенерировать и отправить академический отчет (Report)");
                System.out.println("10. [Наука] Создать новый Научный Проект (Topic)");
                System.out.println("11. [Наука] Посмотреть заявки на проекты и одобрить");
                System.out.println("12. [Наука] Управление проектами (Просмотр и исключение участников)");
                System.out.println("13. [Наука] Исключить участника из Научного Проекта");
                System.out.println("14. [Наука] Назначить научного руководителя 4-курcнику");
                System.out.println("15. Написать сообщение коллеге/студенту (UML-Mail)");
                System.out.println("16. [Наука] Печать публикаций всех researchers (с сортировкой)");
                System.out.println("17. [Наука] Top cited researcher по School и Year");
            } 
            else if (user instanceof Admin) { 
                System.out.println("1. Создать/Добавить нового пользователя");
                System.out.println("2. Удалить пользователя по Email");
                System.out.println("3. Посмотреть системные логи (Действия пользователей)");
                System.out.println("15. Написать сообщение коллеге/студенту (UML-Mail)");
                System.out.println("16. [Наука] Печать публикаций всех researchers (с сортировкой)");
                System.out.println("17. [Наука] Top cited researcher по School и Year");
            } 
            else {
                if (user instanceof Student) {
                    System.out.println("1. Посмотреть мой транскрипт/оценки");
                    System.out.println("2. Зарегистрироваться на курс");
                    System.out.println("3. Открыть Электронный Журнал (Поурочные оценки)");
                    System.out.println("4. Оценить преподавательский состав (Опрос КБТУ)");
                } else if (user instanceof Teacher) {
                    System.out.println("1. Посмотреть мои курсы");
                    System.out.println("2. Выставить поурочный балл");
                    System.out.println("3. Выставить баллы за Финал");
                    System.out.println("4. Посмотреть мой анонимный рейтинг");
                    System.out.println("15. Написать сообщение коллеге/студенту (UML-Mail)");
                }
                System.out.println("12. [Наука] Подать заявку на вступление в Научный Проект");
                if (!user.isResearcher()) {
                    System.out.println("14. [Наука] Активировать профиль исследователя");
                }
            }

            if (user.isResearcher()) {
                System.out.println("8. Открыть Научный Кабинет");
            }
            System.out.println("9. Открыть Входящие сообщения (Inbox: " + user.getInbox().size() + ")");

            System.out.println("0. Выйти из аккаунта");
            System.out.print("Выберите опцию: ");
            
            String option = scanner.nextLine();

            if (option.equals("0")) break;

            if (option.equals("9")) {
                System.out.println("\n--- ВХОДЯЩИЕ СООБЩЕНИЯ ---");
                if (user.getInbox().isEmpty()) {
                    System.out.println("Ваш почтовый ящик пуст.");
                } else {
                    for (String msg : user.getInbox()) {
                        System.out.println(msg);
                    }
                }
                continue; 
            }
            if ((option.equals("16") || option.equals("17")) && !(user instanceof Admin || user instanceof Manager)) {
                System.out.println("[Ошибка] Доступно только для Admin/Manager.");
                continue;
            }
            if (option.equals("16")) {
                printAllResearchersPapersSorted();
                continue;
            }
            if (option.equals("17")) {
                printTopCitedResearcherOfSchoolByYear();
                continue;
            }
            if (option.equals("15") && user instanceof Employee) {
                System.out.println("\n--- ОТПРАВКА СООБЩЕНИЯ (UML) ---");
                System.out.print("Введите Email получателя: ");
                String targetEmail = scanner.nextLine();
                
                User receiver = DataStorage.getInstance().getUserByEmail(targetEmail);
                if (receiver != null) {
                    System.out.print("Введите текст сообщения: ");
                    String text = scanner.nextLine();
                    
                    
                    ((Employee) user).sendMessage(receiver, text);
                    DataStorage.saveData();
                } else {
                    System.out.println("[Ошибка] Пользователь с таким Email не найден в системе КБТУ.");
                }
                continue;
            }

            
            
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                if (option.equals("1")) {
                    System.out.println("\n--- СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ ---");
                    System.out.println("Кому создаем аккаунт? 1 - Студент, 2 - Преподаватель, 3 - Менеджер, 4 - Админ");
                    String type = scanner.nextLine();
                    
                    System.out.print("Введите Email: "); String email = scanner.nextLine();
                    System.out.print("Введите Пароль: "); String pass = scanner.nextLine();
                    System.out.print("Введите Имя: "); String name = scanner.nextLine();
                    System.out.print("Введите Фамилию: "); String lName = scanner.nextLine();
                    System.out.print("Введите номер телефона: "); String phone = scanner.nextLine(); 

                    User newUser = null;
                    if (type.equals("1")) {
                        Student s = new Student();
                        System.out.print("Введите мейджор (например, IS): "); s.major = scanner.nextLine();
                        newUser = s;
                    } else if (type.equals("2")) {
                        newUser = new Teacher();
                    } else if (type.equals("3")) {
                        newUser = new Manager();
                    } else if (type.equals("4")) {
                        newUser = new Admin();
                    }

                    if (newUser != null) {
                        newUser.setEmail(email);
                        newUser.setPassword(pass);
                        newUser.setFirstName(name);
                        newUser.setLastName(lName);
                        newUser.setPhone(phone); 
                        
                        admin.addUser(newUser);
                        DataStorage.saveData();
                    } else {
                        System.out.println("Неверный тип.");
                    }
    }
                
                 else if (option.equals("2")) {
                    System.out.println("\n--- УДАЛЕНИЕ ПОЛЬЗОВАТЕЛЯ ---");
                    System.out.print("Введите Email пользователя для удаления: ");
                    String email = scanner.nextLine();
                    
                    User target = DataStorage.getInstance().getUserByEmail(email);
                    if (target != null) {
                        admin.removeUser(target);
                        DataStorage.saveData();
                    } else {
                        System.out.println("[Ошибка] Пользователь с таким Email не найден.");
                    }

                } else if (option.equals("3")) {
                    admin.seeLogs();
                }
            } 
            
            else if (user instanceof Manager) {
                if (option.equals("10")) {
                    System.out.print("Введите тему научного проекта (Topic): ");
                    String topic = scanner.nextLine();
                    ((Manager) user).createResearchProject(topic);
                    DataStorage.saveData();
                } else if (option.equals("11")) {
                    System.out.println("\n--- МОДЕРАЦИЯ НАУЧНЫХ ПРОЕКТОВ ---");
                    boolean hasPending = false;
                    for (research.ResearchProject p : DataStorage.getInstance().projects) {
                        if (!p.pendingResearchers.isEmpty()) {
                            hasPending = true;
                            System.out.println("\nПроект: " + p.topic);
                            for (research.Researcher r : p.pendingResearchers) {
                                User u = (User) r;
                                System.out.println("  -> ID: " + u.getId() + " | " + u.getFirstName() + " (" + u.getClass().getSimpleName() + ")");
                            }
                        }
                    }
                    if (!hasPending) { 
                        System.out.println("Нет активных заявок на научные проекты."); 
                        continue; 
                    }
                    System.out.print("\nВведите топик проекта: "); String topic = scanner.nextLine();
                    System.out.print("Введите ID соискателя: "); String resId = scanner.nextLine();
                    research.ResearchProject targetProj = null;
                    for (research.ResearchProject p : DataStorage.getInstance().projects) if (p.topic.equalsIgnoreCase(topic)) targetProj = p;
                    User targetUser = null;
                    if (targetProj != null) {
                        for (research.Researcher r : targetProj.pendingResearchers) {
                            User u = (User) r; if (u.getId().equals(resId)) targetUser = u;
                        }
                    }
                    if (targetProj != null && targetUser != null) {
                        ((Manager) user).approveResearcherToProject(targetProj, targetUser);
                        DataStorage.saveData();
                    } else System.out.println("[Ошибка] Неверные данные.");

                } else if (option.equals("12")) {
                    System.out.println("\n--- ГЛОБАЛЬНЫЙ МОНИТОРИНГ НАУЧНЫХ ПРОЕКТОВ ---");
                    List<research.ResearchProject> allProjects = DataStorage.getInstance().projects;
                    if (allProjects.isEmpty()) {
                        System.out.println("В системе КБТУ пока нет созданных научных проектов.");
                        continue;
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    for (int i = 0; i < allProjects.size(); i++) {
                        research.ResearchProject proj = allProjects.get(i);
                        String formattedDate = formatter.format(proj.date);
                        System.out.printf("[%d] Тема: %s | Дата старта: %s | Участников: %d | Статей: %d\n", 
                                i + 1, proj.topic, formattedDate, proj.participants.size(), proj.publishedPapers.size());
                    }
                    System.out.print("\nВыберите номер проекта для детального просмотра (или 0 для возврата): ");
                    Integer projectChoice = readIntOrNull(null);
                    if (projectChoice == null) continue;
                    if (projectChoice == 0) continue;
                    if (projectChoice < 1 || projectChoice > allProjects.size()) {
                        System.out.println("[Ошибка] Неверный номер проекта.");
                        continue;
                    }
                    
                    research.ResearchProject selectedProj = allProjects.get(projectChoice - 1);
                    System.out.println("\n========================================");
                    System.out.println("ПРОЕКТ: " + selectedProj.topic);
                    System.out.println("----------------------------------------");
                    System.out.println("СПИСОК ТЕКУЩИХ УЧАСТНИКОВ:");
                    if (selectedProj.participants.isEmpty()) {
                        System.out.println("  (В данном проекте пока нет активных исследователей)");
                    } else {
                        for (research.Researcher r : selectedProj.participants) {
                            User u = (User) r;
                            System.out.printf("  -> ID: %s | %s %s (%s)\n", u.getId(), u.getFirstName(), u.getLastName(), u.getClass().getSimpleName());
                        }
                    }
                    System.out.println("========================================");
                    System.out.print("Хотите исключить кого-то? Введите ID участника (или Enter для отмены): ");
                    String idToRemove = scanner.nextLine();
                    if (!idToRemove.trim().isEmpty()) {
                        ((Manager) user).removeResearcherFromProject(selectedProj, idToRemove);
                        DataStorage.saveData();
                    }

                } else if (option.equals("13")) {
                    System.out.println("\n--- БЫСТРОЕ ИСКЛЮЧЕНИЕ ИЗ НАУЧНОГО ПРОЕКТА ---");
                    if (DataStorage.getInstance().projects.isEmpty()) {
                        System.out.println("В системе КБТУ пока нет активных научных проектов.");
                        continue;
                    }
                    System.out.print("Введите точное название проекта: "); String topic = scanner.nextLine();
                    System.out.print("Введите ID участника для удаления: "); String resId = scanner.nextLine();
                    research.ResearchProject targetProj = null;
                    for (research.ResearchProject p : DataStorage.getInstance().projects) if (p.topic.equalsIgnoreCase(topic)) targetProj = p;
                    if (targetProj != null) {
                        ((Manager) user).removeResearcherFromProject(targetProj, resId);
                        DataStorage.saveData();
                    } else System.out.println("[Ошибка] Проект не найден.");
                } else {
                    
                    handleManagerActions((Manager) user, option);
                }
            } 
            
            
            else {
                if (option.equals("12")) {
                    System.out.println("\n--- ДОСТУПНЫЕ НАУЧНЫЕ ПРОЕКТЫ ---");
                    if (DataStorage.getInstance().projects.isEmpty()) {
                        System.out.println("В университете пока нет активных научных тем.");
                        continue;
                    }
                    for (research.ResearchProject p : DataStorage.getInstance().projects) {
                        System.out.println("- " + p.topic + " (Участников: " + p.participants.size() + ")");
                    }
                    System.out.print("Введите название проекта для заявки: ");
                    String topic = scanner.nextLine();
                    research.ResearchProject targetProj = null;
                    for (research.ResearchProject p : DataStorage.getInstance().projects) if (p.topic.equalsIgnoreCase(topic)) targetProj = p;
                    if (targetProj != null) {
                        if (!user.isResearcher()) {
                            System.out.println("[Ошибка] Сначала активируйте исследовательский профиль (опция 14).");
                            continue;
                        }
                        if (!targetProj.pendingResearchers.contains(user)) {
                            targetProj.pendingResearchers.add(user);
                            DataStorage.saveData();
                            System.out.println("[Успех] Ваша заявка отправлена Менеджеру.");
                        } else System.out.println("Вы уже подали заявку.");
                    } else System.out.println("Проект не найден.");
                } else if (option.equals("14")) {
                    if (!user.isResearcher()) {
                        user.activateResearchProfile();
                        DataStorage.saveData();
                    } else {
                        System.out.println("Профиль исследователя уже активирован.");
                    }
                } else if (user instanceof Student) {
                    handleStudentActions((Student) user, option);
                } else if (user instanceof Teacher) {
                    handleTeacherActions((Teacher) user, option);
                }
            }

            
            if (option.equals("8") && user.isResearcher()) {
                researchMenu(user);
            }
        } 
    } 

    private static void researchMenu(User user) {
        while (true) {
            System.out.println("\n--- НАУЧНЫЙ КАБИНЕТ ИССЛЕДОВАТЕЛЯ ---");
            System.out.println("1. Посмотреть мои публикации");
            System.out.println("2. Опубликовать командную статью по текущему проекту");
            System.out.println("3. Пересчитать мой H-Index");
            System.out.println("0. Вернуться в главное меню");
            System.out.print("Выберите опцию: ");
            String option = scanner.nextLine();

            if (option.equals("0")) break;

            if (option.equals("1")) {
                Comparator<research.ResearchPaper> comparator = chooseResearchComparatorFromConsole();
                user.printPapers(comparator);
            } else if (option.equals("2")) {
                if (user.getResearchProfile().projects.isEmpty()) {
                    System.out.println("Вы пока не состоите ни в одном одобренном научном проекте.");
                    continue;
                }
                System.out.println("Выберите проект для публикации статьи:");
                for (research.ResearchProject p : user.getResearchProfile().projects) {
                    System.out.println("- " + p.topic);
                }
                System.out.print("Введите название проекта: ");
                String pTopic = scanner.nextLine();
                research.ResearchProject targetP = null;
                for (research.ResearchProject p : user.getResearchProfile().projects) {
                    if (p.topic.equalsIgnoreCase(pTopic)) targetP = p;
                }
                if (targetP != null) {
                    System.out.print("Введите название статьи: "); String title = scanner.nextLine();
                    Integer cites = readIntOrNull("Количество цитирований (citations): ");
                    if (cites == null || cites < 0) {
                        System.out.println("[Ошибка] Цитирования должны быть целым числом >= 0.");
                        continue;
                    }
                    System.out.print("Журнал/конференция: ");
                    String journal = scanner.nextLine();
                    Integer startPage = readIntOrNull("Первая страница статьи: ");
                    Integer endPage = readIntOrNull("Последняя страница статьи: ");
                    if (startPage == null || endPage == null || startPage <= 0 || endPage < startPage) {
                        System.out.println("[Ошибка] Неверно задан диапазон страниц.");
                        continue;
                    }
                    System.out.print("DOI: "); String doi = scanner.nextLine();
                    
                    research.ResearchPaper paper = new research.ResearchPaper(title, cites, doi, journal, startPage, endPage);
                    
                    
                    for (research.Researcher res : targetP.participants) {
                        User u = (User) res;
                        paper.authors.add(u);
                        u.getResearchProfile().papers.add(paper); 
                    }
                    targetP.addPaper(paper); 
                    DataStorage.saveData();
                    System.out.println("[УСПЕХ] Статья '" + title + "' опубликована! Все участники проекта '" + targetP.topic + "' автоматически добавлены в соавторы.");
                } else System.out.println("Вы не состоите в этом проекте.");
            } else if (option.equals("3")) {
                user.calculateHIndex();
            }
        }
    }

    private static void handleManagerActions(Manager manager, String option) {
        DataStorage db = DataStorage.getInstance();
        
        if (option.equals("1")) {
            manager.manageCourses();
        } else if (option.equals("2")) {
            System.out.print("Код курса (CS101): "); String code = scanner.nextLine();
            System.out.print("Название курса: "); String name = scanner.nextLine();
            Integer credits = readIntOrNull("Кредиты: ");
            if (credits == null || credits <= 0) {
                System.out.println("[Ошибка] Кредиты должны быть положительным целым числом.");
                return;
            }
            System.out.print("Для каких специальностей курс (через запятую, напр. CSSE,IS): "); String majorsInput = scanner.nextLine();
            List<String> majorsList = new ArrayList<>();
            for (String m : majorsInput.split(",")) majorsList.add(m.trim().toUpperCase());
            manager.addCourse(new Course(code, name, credits, majorsList));
            System.out.println("[Успех] Курс успешно добавлен.");
        } else if (option.equals("3")) {
            manager.viewStudents();
        } else if (option.equals("4")) {
            System.out.print("Имя: "); String firstName = scanner.nextLine();
            System.out.print("Фамилия: "); String lastName = scanner.nextLine();
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Пароль: "); String password = scanner.nextLine();
            System.out.print("Специальность (CSSE/IS): "); String specialty = scanner.nextLine();
            Teacher teacher = new Teacher();
            teacher.setFirstName(firstName); teacher.setLastName(lastName); teacher.setEmail(email); teacher.setPassword(password);
            teacher.setTitle(enums.TeacherTitle.LECTURER); teacher.specialty = specialty.toUpperCase(); teacher.department = manager.department;
            db.getUsers().add(teacher);
            DataStorage.saveData();
            System.out.println("Преподаватель добавлен с ID: " + teacher.getId());
        } else if (option.equals("5")) {
            System.out.print("Введите Email преподавателя: "); String tEmail = scanner.nextLine();
            System.out.print("Введите код курса: "); String cCode = scanner.nextLine();
            Teacher targetTeacher = null;
            for (User u : db.getUsers()) if (u instanceof Teacher && u.getEmail().equalsIgnoreCase(tEmail)) targetTeacher = (Teacher) u;
            Course targetCourse = null;
            for (Course c : db.courses) if (c.courseCode.equalsIgnoreCase(cCode)) targetCourse = c;
            if (targetTeacher != null && targetCourse != null) {
                manager.assignTeacher(targetTeacher, targetCourse);
                if (!targetCourse.getAssignedTeachers().contains(targetTeacher)) targetCourse.getAssignedTeachers().add(targetTeacher);
                DataStorage.saveData();
                System.out.println("[Успех] Преподаватель назначен вести курс.");
            } else System.out.println("[Ошибка] Не найдено.");
        } else if (option.equals("6")) {
            System.out.print("\nВведите ID для удаления студента: ");
            String idToDelete = scanner.nextLine();
            manager.removeStudentById(idToDelete);
            DataStorage.saveData();
        } else if (option.equals("7")) {
            
            System.out.println("\n--- МОДЕРАЦИЯ ЗАЯВОК НА КУРСЫ ---");
            List<Course> activeCourses = db.courses;
            boolean hasPending = false;
            for (Course c : activeCourses) {
                if (!c.getPendingStudents().isEmpty()) {
                    hasPending = true;
                    System.out.println("\nКурс: " + c.courseCode + " - " + c.courseName);
                    for (Student s : c.getPendingStudents()) System.out.println("  -> ID: " + s.getId() + " | " + s.getFirstName());
                }
            }
            if (!hasPending) { System.out.println("Нет необработанных заявок на курсы."); return; }
            System.out.print("\nВведите код курса: "); String cCode = scanner.nextLine();
            System.out.print("Введите ID студента: "); String sId = scanner.nextLine();
            Course targetCourse = null; for (Course c : db.courses) if (c.courseCode.equalsIgnoreCase(cCode)) targetCourse = c;
            Student targetStudent = null;
            if (targetCourse != null) for (Student s : targetCourse.getPendingStudents()) if (s.getId().equals(sId)) targetStudent = s;
            if (targetCourse != null && targetStudent != null) { manager.approveRegistration(targetCourse, targetStudent); DataStorage.saveData(); }
            else System.out.println("[Ошибка] Не найдено.");
        }
        else if (option.equals("8")) {
            generateCourseReport((manager));
        } else if (option.equals("14")) {
            System.out.println("\n--- НАЗНАЧЕНИЕ НАУЧНОГО РУКОВОДИТЕЛЯ ---");
            List<Student> fourthYearStudents = new ArrayList<>();
            for (User u : db.getUsers()) {
                if (u instanceof Student && ((Student) u).yearOfStudy == 4) {
                    fourthYearStudents.add((Student) u);
                }
            }

            if (fourthYearStudents.isEmpty()) {
                System.out.println("В системе нет студентов 4 курса.");
                return;
            }

            System.out.println("Список студентов 4 курса:");
            for (Student s : fourthYearStudents) {
                System.out.println("- ID: " + s.getId() + " | " + s.getFirstName() + " " + s.getLastName());
            }
            System.out.print("Введите ID студента: ");
            String studentId = scanner.nextLine();

            Student targetStudent = null;
            for (Student s : fourthYearStudents) {
                if (s.getId().equals(studentId)) {
                    targetStudent = s;
                    break;
                }
            }
            if (targetStudent == null) {
                System.out.println("Студент не найден.");
                return;
            }

            System.out.print("Введите Email научного руководителя: ");
            String supervisorEmail = scanner.nextLine();
            User supervisor = db.getUserByEmail(supervisorEmail);
            if (supervisor == null) {
                System.out.println("Пользователь не найден.");
                return;
            }

            try {
                targetStudent.assignResearchSupervisor(supervisor);
                DataStorage.saveData();
            } catch (exceptions.NotAResearcherException | exceptions.LowHIndexException e) {
                System.out.println("Назначение отклонено: " + e.getMessage());
            }
        }
    }

    private static void handleStudentActions(Student student, String option) {
        DataStorage db = DataStorage.getInstance();
        if (option.equals("1")) {
            System.out.println("\n========================================");
            System.out.println("       ОФИЦИАЛЬНЫЙ ТРАНСКРИПТ СТУДЕНТА  ");
            System.out.println("========================================");
            System.out.println("Студент: " + student.getFirstName() + " " + student.getLastName());
            System.out.println("Специальность: " + student.major);
            System.out.println("Программа: Bachelor");
            if (student.yearOfStudy == 4) {
                if (student.researchSupervisor instanceof User) {
                    User supervisor = (User) student.researchSupervisor;
                    System.out.println("Научный руководитель: " + supervisor.getFirstName() + " " + supervisor.getLastName() + " (" + supervisor.getEmail() + ")");
                } else {
                    System.out.println("Научный руководитель: не назначен");
                }
            }
            System.out.println("----------------------------------------");
            
            boolean hasClosedCourses = false;

            for (java.util.Map.Entry<Course, academic.CourseProgress> entry : student.courseProgressMap.entrySet()) {
                academic.CourseProgress p = entry.getValue();
                

                if (p.isFinalPassed) {
                    hasClosedCourses = true;
                    Course c = entry.getKey();
                    System.out.printf("- %s: %s | Кредиты: %d | Итог: %.1f баллов | Оценка: %s | Вес GPA: %.2f\n", 
                            c.courseCode, c.courseName, c.credits, p.getTotalScore(), p.getLetterGrade(), p.getGPAContribution());
                }
            }
            
            if (!hasClosedCourses) {
                System.out.println("В данном семестре пока нет официально закрытых дисциплин.");
            }
            
            System.out.println("----------------------------------------");

            System.out.printf("ОБЩИЙ ТЕКУЩИЙ GPA СТУДЕНТА: %.2f\n", student.calculateGPA());
            System.out.println("========================================");
        } 
        

        else if (option.equals("2")) {
            System.out.println("\nДоступные курсы для вашей специальности (" + student.major + "):");
            boolean availableFound = false;
            
            for (Course c : db.courses) {
                if (c.getTargetMajors().contains(student.major.toUpperCase())) {
                    if (!student.courseProgressMap.containsKey(c) && !c.getPendingStudents().contains(student)) {
                        System.out.println("- Код: " + c.courseCode + " | Название: " + c.courseName + " (" + c.credits + " credits)");
                        availableFound = true;
                    }
                }
            }
            
            if (!availableFound) {
                System.out.println("Нет доступных новых курсов для регистрации.");
                return;
            }
            
            System.out.print("\nВведите КОД курса для регистрации (например, CS101): ");
            String courseCode = scanner.nextLine();
            
            Course targetCourse = null;
            for (Course c : db.courses) {
                if (c.courseCode.equalsIgnoreCase(courseCode) && c.getTargetMajors().contains(student.major.toUpperCase())) {
                    targetCourse = c;
                    break;
                }
            }
            
            if (targetCourse != null) {
                if (!targetCourse.getPendingStudents().contains(student)) {
                    targetCourse.getPendingStudents().add(student);
                    DataStorage.getInstance().addLog("Студент " + student.getEmail() + " подал заявку на курс " + targetCourse.courseCode);
                    DataStorage.saveData();
                    System.out.println("[Успех] Ваша заявка на курс " + targetCourse.courseCode + " отправлена Менеджеру КБТУ на апрув.");
                } else {
                    System.out.println("[Внимание] Вы уже подали заявку на этот курс и ожидаете одобрения.");
                }
            } else {
                System.out.println("[Ошибка] Курс с таким кодом не найден или не предназначен для вашего мейджора.");
            }
        }
        

        else if (option.equals("3")) {
            System.out.println("\n--- ТЕКУЩИЙ ЭЛЕКТРОННЫЙ ЖУРНАЛ СТУДЕНТА ---");
            if (student.courseProgressMap.isEmpty()) {
                System.out.println("Вы пока не зачислены ни на один активный курс.");
                return;
            }

            List<Course> activeCourses = new ArrayList<>(student.courseProgressMap.keySet());
            for (int i = 0; i < activeCourses.size(); i++) {
                Course c = activeCourses.get(i);
                academic.CourseProgress p = student.courseProgressMap.get(c);
                System.out.printf("[%d] %s: %s (Текущий баланс: %.1f/60.0)%s\n", 
                        i + 1, c.courseCode, c.courseName, p.getCurrentTermScore(), p.isFinalPassed ? " [ЗАКРЫТ]" : "");
            }

            System.out.print("\nВыберите номер курса для просмотра поурочных оценок (или 0 для возврата): ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("[Ошибка] Введите корректное число.");
                return;
            }

            if (choice > 0 && choice <= activeCourses.size()) {
                Course chosenCourse = activeCourses.get(choice - 1);
                academic.CourseProgress p = student.courseProgressMap.get(chosenCourse);
                
                System.out.println("\n========================================");
                System.out.println("ДЕТАЛИЗАЦИЯ ЖУРНАЛА ПО ДИСЦИПЛИНЕ: " + chosenCourse.courseName.toUpperCase());
                System.out.println("========================================");
                
                if (p.dailyMarks.isEmpty()) {
                    System.out.println("  (У вас пока нет выставленных оценок за работу на занятиях)");
                } else {
                   
                    for (academic.LessonMark lm : p.dailyMarks) {
                        System.out.println("  " + lm);
                    }
                }
                
                System.out.println("----------------------------------------");
                System.out.printf("ИТОГО ТЕКУЩИЙ БАЛЛ (МАКС 60): %.1f\n", p.getCurrentTermScore());
                System.out.printf("БАЛЛ ЗА ФИНАЛЬНЫЙ ЭКЗАМЕН (МАКС 40): %.1f %s\n", p.finalExamMark, p.isFinalPassed ? "" : "(Экзамен еще не сдан)");
                if (p.isFinalPassed) {
                    System.out.printf("ИТОГОВЫЙ СУММАРНЫЙ БАЛЛ: %.1f из 100.0 (Оценка: %s)\n", p.getTotalScore(), p.getLetterGrade());
                }
                System.out.println("========================================");
            }
        }
        else if (option.equals("4")) {
        student.rateTeachers(scanner); 
    }
    }

    private static void handleTeacherActions(Teacher teacher, String option) {
        if (!option.equals("4") && teacher.courses.isEmpty()) {
            System.out.println("У вас нет активных курсов.");
            return;
        }

        if (option.equals("1")) {
            teacher.viewCourses();
        } 
        else if (option.equals("2")) {
            System.out.println("\n--- ВЫСТАВЛЕНИЕ ПОУРОЧНОГО БАЛЛА ---");
            Course selectedCourse = selectTeacherCourse(teacher);
            if (selectedCourse == null) return;

            Student selectedStudent = selectCourseStudent(selectedCourse);
            if (selectedStudent == null) return;

            Double score = readDoubleOrNull("Введите балл за занятие (например, от 0 до 4): ");
            if (score == null || score < 0 || score > 4) {
                System.out.println("[Ошибка] Балл за занятие должен быть в диапазоне 0..4.");
                return;
            }
            System.out.print("Комментарий/Тип занятия: ");
            String comment = scanner.nextLine();


            academic.CourseProgress progress = selectedStudent.courseProgressMap.get(selectedCourse);
            if (progress == null) {
                progress = new academic.CourseProgress();
                selectedStudent.courseProgressMap.put(selectedCourse, progress);
            }

            progress.dailyMarks.add(new academic.LessonMark(score, comment));
            DataStorage.getInstance().addLog("Преподаватель " + teacher.getLastName() + " выставил балл " + score + " студенту " + selectedStudent.getEmail());
            DataStorage.saveData();
            System.out.printf("[Успех] Балл назначен. Текущая сумма студента за семестр: %.1f/60.0\n", progress.getCurrentTermScore());
        } 
        else if (option.equals("3")) {
            System.out.println("\n--- ВЫСТАВЛЕНИЕ БАЛЛОВ ЗА ФИНАЛ ---");
            Course selectedCourse = selectTeacherCourse(teacher);
            if (selectedCourse == null) return;

            Student selectedStudent = selectCourseStudent(selectedCourse);
            if (selectedStudent == null) return;

            academic.CourseProgress progress = selectedStudent.courseProgressMap.get(selectedCourse);

            if (progress == null) {
                progress = new academic.CourseProgress();
                selectedStudent.courseProgressMap.put(selectedCourse, progress);
            }

            if (progress.isFinalPassed) {
                System.out.println("[Внимание] Финал по этому курсу уже был выставлен!");
            }

            Double finalScore = readDoubleOrNull("Введите балл за Финальный Экзамен (0-40): ");
            if (finalScore == null || finalScore < 0 || finalScore > 40) {
                System.out.println("[Ошибка] Балл финального экзамена должен быть в диапазоне 0..40.");
                return;
            }

            progress.finalExamMark = finalScore;
            progress.isFinalPassed = true;

            DataStorage.getInstance().addLog("Преподаватель " + teacher.getLastName() + " ЗАКРЫЛ КУРС для " + selectedStudent.getEmail() + " с финалом " + finalScore);
            DataStorage.saveData();
            System.out.printf("[Успех] Финал сохранен! Итоговый балл: %.1f (%s). Курс внесен в транскрипт.\n", 
                    progress.getTotalScore(), progress.getLetterGrade());
        }
        else if (option.equals("4")) {
            teacher.printRatingStats();
}
    }

    private static void generateCourseReport(Manager manager) {
        DataStorage db = DataStorage.getInstance();
        System.out.println("\n========================================");
        System.out.println("       ГЕНЕРАЦИЯ ОТЧЕТОВ ПО КУРСАМ      ");
        System.out.println("========================================");

        java.util.List<Course> allCourses = db.courses;
        if (allCourses.isEmpty()) {
            System.out.println("В системе КБТУ пока нет созданных курсов.");
            return;
        }

        
        for (int i = 0; i < allCourses.size(); i++) {
            Course c = allCourses.get(i);
            System.out.printf("[%d] %s - %s\n", i + 1, c.courseCode, c.courseName);
        }

        System.out.print("\nВыберите номер курса для генерации репорта (или 0 для отмены): ");
        Integer choice = readIntOrNull(null);
        if (choice == null || choice <= 0 || choice > allCourses.size()) return;

        Course selectedCourse = allCourses.get(choice - 1);

        
        int totalStudents = 0;
        int failedStudents = 0;
        double totalMarksSum = 0.0;
        int studentsWithMarksCount = 0;

        for (User u : db.getUsers()) {
            if (u instanceof Student) {
                Student student = (Student) u;
                if (student.courseProgressMap != null && student.courseProgressMap.containsKey(selectedCourse)) {
                    totalStudents++;
                    academic.CourseProgress progress = student.courseProgressMap.get(selectedCourse);
                    
                    double studentTotalScore = 0.0;
                    if (progress.dailyMarks != null) {
                        for (academic.LessonMark m : progress.dailyMarks) {
                            studentTotalScore += m.score; 
                        }
                    }
                    studentTotalScore += progress.finalExamMark;

                    if (studentTotalScore > 0) {
                        totalMarksSum += studentTotalScore;
                        studentsWithMarksCount++;
                    }

                    if (progress.isFinalPassed && studentTotalScore < 50.0) {
                        failedStudents++;
                    }
                }
            }
        }

        double avgMark = (studentsWithMarksCount == 0) ? 0.0 : (totalMarksSum / studentsWithMarksCount);

        
        academic.Report report = new academic.Report(selectedCourse.courseCode, selectedCourse.courseName, totalStudents, failedStudents, avgMark);
        
        
        report.printReportDetails();

        System.out.print("\nОтправить и зафиксировать этот отчет? (1 - Да, 0 - Нет): ");
        String confirm = scanner.nextLine();
        if (confirm.equals("1")) {
            
            db.addLog("Менеджер " + manager.getEmail() + " сгенерировал и утвердил академический отчет по курсу " + selectedCourse.courseCode);
            System.out.println("[Успех] Отчет успешно отправлен в архив и зафиксирован в системе безопасности.");
            DataStorage.saveData();
        } else {
            System.out.println("Генерация отчета отменена.");
        }
    }

    private static Comparator<research.ResearchPaper> chooseResearchComparatorFromConsole() {
        System.out.println("Сортировать публикации по:");
        System.out.println("1. Citations (по убыванию)");
        System.out.println("2. Date published (сначала новые)");
        System.out.println("3. Article length/pages (по убыванию)");
        System.out.print("Выберите тип сортировки: ");
        String sortChoice = scanner.nextLine();

        if (sortChoice.equals("2")) {
            return (p1, p2) -> p2.getPublicationDate().compareTo(p1.getPublicationDate());
        }
        if (sortChoice.equals("3")) {
            return (p1, p2) -> Integer.compare(p2.getArticleLengthInPages(), p1.getArticleLengthInPages());
        }
        return (p1, p2) -> Integer.compare(p2.getCitations(), p1.getCitations());
    }

    private static void printAllResearchersPapersSorted() {
        DataStorage db = DataStorage.getInstance();
        Comparator<research.ResearchPaper> comparator = chooseResearchComparatorFromConsole();

        System.out.println("\n--- ПУБЛИКАЦИИ ВСЕХ ИССЛЕДОВАТЕЛЕЙ KBTU ---");
        int researchersPrinted = 0;

        for (User u : db.getUsers()) {
            if (!u.isResearcher()) {
                continue;
            }
            researchersPrinted++;
            System.out.println("\nResearcher: " + u.getFirstName() + " " + u.getLastName() + " | " + u.getEmail());
            u.printPapers(comparator);
        }

        if (researchersPrinted == 0) {
            System.out.println("В системе пока нет активных исследователей.");
        }
    }

    private static void printTopCitedResearcherOfSchoolByYear() {
        DataStorage db = DataStorage.getInstance();
        Set<String> schools = new HashSet<>();
        for (User u : db.getUsers()) {
            if (u.isResearcher()) {
                schools.add(getSchoolName(u));
            }
        }
        if (schools.isEmpty()) {
            System.out.println("В системе нет активных исследователей.");
            return;
        }

        System.out.println("Доступные School значения: " + schools);
        System.out.print("Введите School (или ALL): ");
        String school = scanner.nextLine().trim().toUpperCase();
        Integer year = readIntOrNull("Введите год (например 2026): ");
        if (year == null) {
            return;
        }

        if (school.equals("ALL")) {
            for (String schoolName : schools) {
                printTopCitedForSpecificSchool(db, schoolName, year);
            }
            return;
        }

        printTopCitedForSpecificSchool(db, school, year);
    }

    private static void printTopCitedForSpecificSchool(DataStorage db, String school, int year) {
        User topResearcher = null;
        int topCitations = -1;
        for (User u : db.getUsers()) {
            if (!u.isResearcher()) {
                continue;
            }
            if (!getSchoolName(u).equalsIgnoreCase(school)) {
                continue;
            }
            int citationsInYear = getCitationsInYear(u, year);
            if (citationsInYear > topCitations) {
                topCitations = citationsInYear;
                topResearcher = u;
            }
        }

        if (topResearcher == null) {
            System.out.println("Для выбранного School нет исследователей.");
            return;
        }
        if (topCitations <= 0) {
            System.out.println("В выбранном School нет публикаций за " + year + ".");
            return;
        }

        System.out.println("TOP CITED RESEARCHER [" + school + ", " + year + "]: "
                + topResearcher.getFirstName() + " " + topResearcher.getLastName()
                + " | Citations: " + topCitations);
    }

    private static String getSchoolName(User u) {
        if (u instanceof Student) {
            Student s = (Student) u;
            return (s.major == null || s.major.trim().isEmpty()) ? "UNASSIGNED" : s.major.trim().toUpperCase();
        }
        if (u instanceof Employee) {
            Employee e = (Employee) u;
            return (e.department == null || e.department.trim().isEmpty()) ? "UNASSIGNED" : e.department.trim().toUpperCase();
        }
        return "GENERAL";
    }

    private static int getCitationsInYear(User u, int year) {
        if (u.getResearchProfile() == null) {
            return 0;
        }
        int total = 0;
        for (research.ResearchPaper p : u.getResearchProfile().papers) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(p.getPublicationDate());
            if (calendar.get(Calendar.YEAR) == year) {
                total += p.getCitations();
            }
        }
        return total;
    }

    private static Integer readIntOrNull(String prompt) {
        if (prompt != null) {
            System.out.print(prompt);
        }
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[Ошибка] Введите корректное целое число.");
            return null;
        }
    }

    private static Double readDoubleOrNull(String prompt) {
        if (prompt != null) {
            System.out.print(prompt);
        }
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[Ошибка] Введите корректное число.");
            return null;
        }
    }

    
    private static Course selectTeacherCourse(Teacher t) {
        System.out.println("Выберите код курса:");
        for (Course c : t.courses) System.out.println("- " + c.courseCode);
        System.out.print("Ввод: ");
        String code = scanner.nextLine();
        for (Course c : t.courses) if (c.courseCode.equalsIgnoreCase(code)) return c;
        System.out.println("Курс не найден.");
        return null;
    }

    
    private static Student selectCourseStudent(Course c) {
        if (c.getRegisteredStudents().isEmpty()) {
            System.out.println("На курсе нет студентов.");
            return null;
        }
        System.out.println("Студенты на курсе:");
        for (Student s : c.getRegisteredStudents()) System.out.println("- " + s.getEmail());
        System.out.print("Введите Email студента: ");
        String email = scanner.nextLine();
        for (Student s : c.getRegisteredStudents()) if (s.getEmail().equalsIgnoreCase(email)) return s;
        System.out.println("Студент не найден.");
        return null;
    }
}
