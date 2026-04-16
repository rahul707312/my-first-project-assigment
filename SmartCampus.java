1import java.io.*;
import java.util.*;

// Custom Exception
class InvalidFeeException extends Exception {
    public InvalidFeeException(String message) {
        super(message);
    }
}

// Student Class
class Student {
    int studentId;
    String name;
    String email;

    public Student(int studentId, String name, String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return studentId + " - " + name + " - " + email;
    }
}

// Course Class
class Course {
    int courseId;
    String courseName;
    double fee;

    public Course(int courseId, String courseName, double fee) throws InvalidFeeException {
        if (fee < 0) {
            throw new InvalidFeeException("Course fee cannot be negative!");
        }
        this.courseId = courseId;
        this.courseName = courseName;
        this.fee = fee;
    }

    @Override
    public String toString() {
        return courseId + " - " + courseName + " - Fee: " + fee;
    }
}

// Thread Class for Enrollment Processing
class EnrollmentProcessor extends Thread {
   private final String message;

    public EnrollmentProcessor(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            System.out.println("Processing enrollment...");
            Thread.sleep(2000);
            System.out.println(message);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted!");
        }
    }
}

// Main Class
public class SmartCampus {

    static HashMap<Integer, Student> students = new HashMap<>();
    static HashMap<Integer, Course> courses = new HashMap<>();
    static HashMap<Integer, ArrayList<Course>> enrollments = new HashMap<>();

    static Scanner sc = new Scanner(System.in);

    public static void addStudent() {
        try {
            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            students.put(id, new Student(id, name, email));
            System.out.println("Student added successfully!");

        } catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine();
        }
    }

    public static void addCourse() {
        try {
            System.out.print("Enter Course ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Course Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Fee: ");
            double fee = sc.nextDouble();

            courses.put(id, new Course(id, name, fee));
            System.out.println("Course added successfully!");

        } catch (InvalidFeeException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine();
        }
    }

    public static void enrollStudent() {
        try {
            System.out.print("Enter Student ID: ");
            int sid = sc.nextInt();

            System.out.print("Enter Course ID: ");
            int cid = sc.nextInt();

            if (!students.containsKey(sid) || !courses.containsKey(cid)) {
                System.out.println("Invalid Student or Course ID!");
                return;
            }

            enrollments.putIfAbsent(sid, new ArrayList<>());
            enrollments.get(sid).add(courses.get(cid));

            // Thread for processing
            EnrollmentProcessor ep = new EnrollmentProcessor("Enrollment Successful!");
            ep.start();

        } catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine();
        }
    }

    public static void viewStudents() {
        for (Student s : students.values()) {
            System.out.println(s);
        }
    }

    public static void viewEnrollments() {
        for (int sid : enrollments.keySet()) {
            System.out.println("Student: " + students.get(sid).name);
            for (Course c : enrollments.get(sid)) {
                System.out.println("  -> " + c.courseName);
            }
        }
    }

    // BONUS: Save to file
    public static void saveData() {
        try {
            FileWriter fw = new FileWriter("data.txt");
            for (Student s : students.values()) {
                fw.write(s.toString() + "\n");
            }
            fw.close();
            System.out.println("Data saved to file!");
        } catch (IOException e) {
            System.out.println("File error!");
        }
    }

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n===== SMART CAMPUS MENU =====");
            System.out.println("1. Add Student");
            System.out.println("2. Add Course");
            System.out.println("3. Enroll Student");
            System.out.println("4. View Students");
            System.out.println("5. View Enrollments");
            System.out.println("6. Save Data");
            System.out.println("7. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: addStudent(); break;
                case 2: addCourse(); break;
                case 3: enrollStudent(); break;
                case 4: viewStudents(); break;
                case 5: viewEnrollments(); break;
                case 6: saveData(); break;
                case 7:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}

