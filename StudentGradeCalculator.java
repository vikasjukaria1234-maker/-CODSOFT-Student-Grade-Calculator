import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class StudentGradeCalculator extends JFrame {

    // --- GUI Components ---
    private final JTextField subjectsField;
    private final JTextArea marksArea;
    private final JTextArea resultArea;
    private final JButton calculateButton;
    private final JButton clearButton;

    public StudentGradeCalculator() {
        // --- Frame Setup ---
        setTitle("Student Grade Calculator");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // --- Top Panel: Number of Subjects ---
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Enter Number of Subjects"));
        subjectsField = new JTextField(10);
        topPanel.add(new JLabel("Subjects:"));
        topPanel.add(subjectsField);
        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel: Marks Input ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Enter Marks (one per line, out of 100)"));
        marksArea = new JTextArea(8, 30);
        marksArea.setLineWrap(true);
        marksArea.setWrapStyleWord(true);
        JScrollPane marksScroll = new JScrollPane(marksArea);
        centerPanel.add(marksScroll, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // --- Bottom Panel: Buttons and Result ---
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        calculateButton = new JButton("Calculate Grade");
        clearButton = new JButton("Clear");
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        // Result Area
        resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Result"));
        bottomPanel.add(resultScroll, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- Button Actions ---
        calculateButton.addActionListener(this::calculateGrade);
        clearButton.addActionListener(e -> clearAll());

        // --- Show Window ---
        setVisible(true);
    }

    // --- Method: Calculate Grade ---
    private void calculateGrade(ActionEvent e) {
        String subjectsText = subjectsField.getText().trim();
        String marksText = marksArea.getText().trim();

        if (subjectsText.isEmpty() || marksText.isEmpty()) {
            resultArea.setText("⚠️ Please enter both number of subjects and marks!");
            return;
        }

        int numSubjects;
        try {
            numSubjects = Integer.parseInt(subjectsText);
        } catch (NumberFormatException ex) {
            resultArea.setText("❌ Invalid number of subjects! Please enter a whole number.");
            return;
        }

        String[] marksLines = marksText.split("\\n");
        if (marksLines.length != numSubjects) {
            resultArea.setText("⚠️ Please enter exactly " + numSubjects + " marks (one per line).");
            return;
        }

        int totalMarks = 0;
        for (String line : marksLines) {
            try {
                int marks = Integer.parseInt(line.trim());
                if (marks < 0 || marks > 100) {
                    resultArea.setText("❌ Invalid marks! Each mark must be between 0 and 100.");
                    return;
                }
                totalMarks += marks;
            } catch (NumberFormatException ex) {
                resultArea.setText("❌ Invalid input! Please enter only numeric marks.");
                return;
            }
        }

        double average = (double) totalMarks / numSubjects;
        String grade = getGrade(average);

        resultArea.setText(String.format(
                "----- STUDENT RESULT -----\n" +
                "Total Marks: %d / %d\n" +
                "Average Percentage: %.2f%%\n" +
                "Grade: %s",
                totalMarks, numSubjects * 100, average, grade
        ));
    }

    // --- Helper Method: Determine Grade ---
    private String getGrade(double avg) {
        if (avg >= 90) return "A+";
        else if (avg >= 80) return "A";
        else if (avg >= 70) return "B";
        else if (avg >= 60) return "C";
        else if (avg >= 50) return "D";
        else return "F (Fail)";
    }

    // --- Method: Clear Input and Result ---
    private void clearAll() {
        subjectsField.setText("");
        marksArea.setText("");
        resultArea.setText("");
    }

    // --- Main Method ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGradeCalculator::new);
    }
}
