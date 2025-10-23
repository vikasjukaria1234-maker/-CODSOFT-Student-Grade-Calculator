import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

// ---- Student Class ----
class Student implements Serializable {
    private String name;
    private String rollNumber;
    private String grade;

    public Student(String name, String rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() { return name; }
    public String getRollNumber() { return rollNumber; }
    public String getGrade() { return grade; }

    public void setName(String name) { this.name = name; }
    public void setGrade(String grade) { this.grade = grade; }

    @Override
    public String toString() {
        return String.format("Roll No: %s | Name: %s | Grade: %s", rollNumber, name, grade);
    }
}

// ---- Management System Class ----
public class StudentManagementSystem extends JFrame {
    private final JTextField nameField, rollField, gradeField, searchField;
    private final JTextArea displayArea;
    private final ArrayList<Student> students;
    private final File dataFile = new File("students.txt");

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Initialize student list and load data
        students = new ArrayList<>();
        loadDataFromFile();

        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add / Edit Student"));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Roll Number:"));
        rollField = new JTextField();
        inputPanel.add(rollField);

        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        JButton addButton = new JButton("Add Student");
        JButton editButton = new JButton("Edit Student");
        inputPanel.add(addButton);
        inputPanel.add(editButton);

        // --- Search Panel ---
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search / Remove Student"));
        searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        JButton removeButton = new JButton("Remove");
        JButton showAllButton = new JButton("Show All");

        searchPanel.add(new JLabel("Roll No:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(removeButton);
        searchPanel.add(showAllButton);

        // --- Display Area ---
        displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student Records"));

        // Add Panels to Frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---

        addButton.addActionListener(e -> addStudent());
        editButton.addActionListener(e -> editStudent());
        searchButton.addActionListener(e -> searchStudent());
        removeButton.addActionListener(e -> removeStudent());
        showAllButton.addActionListener(e -> showAllStudents());

        // Show data when starting
        showAllStudents();
        setVisible(true);
    }

    // ---- Functional Methods ----

    private void addStudent() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String grade = gradeField.getText().trim();

        if (name.isEmpty() || roll.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                JOptionPane.showMessageDialog(this, "❌ Student with this Roll Number already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        students.add(new Student(name, roll, grade));
        saveDataToFile();
        clearFields();
        showAllStudents();
    }

    private void editStudent() {
        String roll = rollField.getText().trim();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Enter Roll Number to edit a student.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                s.setName(nameField.getText().trim());
                s.setGrade(gradeField.getText().trim());
                saveDataToFile();
                clearFields();
                showAllStudents();
                JOptionPane.showMessageDialog(this, "✅ Student details updated!");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "❌ Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void searchStudent() {
        String roll = searchField.getText().trim();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Enter Roll Number to search!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                displayArea.setText("🔍 Student Found:\n" + s.toString());
                return;
            }
        }
        displayArea.setText("❌ No student found with Roll No: " + roll);
    }

    private void removeStudent() {
        String roll = searchField.getText().trim();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Enter Roll Number to remove!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            Student s = it.next();
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                it.remove();
                saveDataToFile();
                showAllStudents();
                JOptionPane.showMessageDialog(this, "🗑️ Student removed successfully!");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "❌ Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showAllStudents() {
        StringBuilder sb = new StringBuilder("📋 All Students:\n");
        for (Student s : students) {
            sb.append(s.toString()).append("\n");
        }
        if (students.isEmpty()) sb.append("No students found.");
        displayArea.setText(sb.toString());
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        gradeField.setText("");
    }

    // ---- File Handling ----
    private void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(students);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage());
        }
    }

    private void loadDataFromFile() {
        if (!dataFile.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList) {
                students.addAll((ArrayList<Student>) obj);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    // ---- Main ----
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementSystem::new);
    }
}
