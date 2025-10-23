import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.*;

public class NumberGame extends JFrame {

    // --- Game Constants ---
    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 100;
    private static final int MAX_ATTEMPTS = 7;

    // --- Game State Variables ---
    private int secretNumber;
    private int attemptsLeft;
    private int totalRoundsWon;
    private int totalScore;

    // --- GUI Components ---
    private final JTextField guessField;
    private final JTextArea feedbackArea;
    private final JLabel scoreLabel;
    private final JButton guessButton;
    private final JButton newGameButton;

    public NumberGame() {  // ✅ Constructor name must match class name
        // --- 1. Frame Setup ---
        setTitle("Number Guessing Game");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Outer layout

        // --- 2. Initialize Game State and Start First Round ---
        totalRoundsWon = 0;
        totalScore = 0;

        // --- 3. Create Components ---

        // Input Panel (North)
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Your Guess"));
        guessField = new JTextField(10);
        guessButton = new JButton("Guess!");

        inputPanel.add(new JLabel(String.format("Enter %d-%d:", MIN_RANGE, MAX_RANGE)));
        inputPanel.add(guessField);
        inputPanel.add(guessButton);
        add(inputPanel, BorderLayout.NORTH);

        // Feedback/Log Area (Center)
        feedbackArea = new JTextArea("Welcome! Click 'Start New Game' to begin.", 10, 30);
        feedbackArea.setEditable(false);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Game Log & Feedback"));
        add(scrollPane, BorderLayout.CENTER);

        // Score and Control Panel (South)
        JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        scoreLabel = new JLabel("Score: 0 | Rounds Won: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        newGameButton = new JButton("Start New Game");

        controlPanel.add(scoreLabel);
        controlPanel.add(newGameButton);
        add(controlPanel, BorderLayout.SOUTH);

        // --- 4. Add Action Listeners ---
        guessButton.addActionListener(this::handleGuess);
        newGameButton.addActionListener(e -> newRound());

        // --- 5. Final Window Setup ---
        setLocationRelativeTo(null);
        setVisible(true);

        // Start first round
        newRound();
    }

    // --- Game Logic Methods ---

    private void newRound() {
        Random random = new Random();
        secretNumber = random.nextInt(MAX_RANGE - MIN_RANGE + 1) + MIN_RANGE;
        attemptsLeft = MAX_ATTEMPTS;

        guessField.setText("");
        guessButton.setEnabled(true);

        String welcomeMessage = String.format("--- NEW ROUND ---\nI've generated a number between %d and %d.\nYou have %d attempts.\n",
                MIN_RANGE, MAX_RANGE, MAX_ATTEMPTS);
        feedbackArea.setText(welcomeMessage);
        updateScoreLabel();
    }

    private void handleGuess(ActionEvent e) {
        if (attemptsLeft <= 0) {
            feedbackArea.append("\nGame over! Click 'Start New Game' to play again.");
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(guessField.getText().trim());
        } catch (NumberFormatException ex) {
            feedbackArea.append("\n❌ Invalid input. Please enter a whole number.");
            guessField.setText("");
            return;
        }

        guessField.setText("");

        if (guess < MIN_RANGE || guess > MAX_RANGE) {
            feedbackArea.append(String.format("\n⚠️ Guess must be between %d and %d.", MIN_RANGE, MAX_RANGE));
            return;
        }

        attemptsLeft--;
        int attemptsTaken = MAX_ATTEMPTS - attemptsLeft;

        if (guess == secretNumber) {
            roundWon(attemptsTaken);
        } else if (guess < secretNumber) {
            feedbackArea.append(String.format("\nGuess %d: 🔺 Too low! (%d attempts left)", guess, attemptsLeft));
        } else {
            feedbackArea.append(String.format("\nGuess %d: 🔻 Too high! (%d attempts left)", guess, attemptsLeft));
        }

        if (attemptsLeft == 0 && guess != secretNumber) {
            roundLost();
        }
    }

    private void roundWon(int attemptsTaken) {
        guessButton.setEnabled(false);
        totalRoundsWon++;
        int scoreThisRound = MAX_ATTEMPTS - attemptsTaken + 1;
        totalScore += scoreThisRound;

        feedbackArea.append(String.format("\n\n🎉 CONGRATULATIONS! The number was %d.", secretNumber));
        feedbackArea.append(String.format("\nIt took you %d attempts. You earned %d points!", attemptsTaken, scoreThisRound));
        updateScoreLabel();
    }

    private void roundLost() {
        guessButton.setEnabled(false);
        feedbackArea.append(String.format("\n\n💀 GAME OVER! You ran out of attempts. The number was %d.", secretNumber));
    }

    private void updateScoreLabel() {
        scoreLabel.setText(String.format("Score: %d | Rounds Won: %d", totalScore, totalRoundsWon));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberGame::new); // ✅ Fixed class name reference
    }
}

    
    

