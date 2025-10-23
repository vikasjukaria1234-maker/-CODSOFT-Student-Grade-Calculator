import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;


class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        } else {
            return false; 
        }
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        } else {
            return false; 
        }
    }
}


public class ATMInterface extends JFrame {

    private final BankAccount account; 
    private final JTextField amountField;
    private final JTextArea messageArea;
    private final JButton withdrawButton;
    private final JButton depositButton;
    private final JButton checkBalanceButton;
    private final JButton clearButton;

    public ATMInterface() {
        
        account = new BankAccount(1000.0); 

        
        setTitle("ATM Machine Interface");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Enter Amount"));
        amountField = new JTextField(10);
        topPanel.add(new JLabel("₹"));
        topPanel.add(amountField);
        add(topPanel, BorderLayout.NORTH);

        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Select Transaction"));
        withdrawButton = new JButton("Withdraw");
        depositButton = new JButton("Deposit");
        checkBalanceButton = new JButton("Check Balance");
        clearButton = new JButton("Clear");

        buttonPanel.add(withdrawButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(checkBalanceButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.CENTER);

        
        messageArea = new JTextArea(8, 30);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Transaction Details"));
        add(scrollPane, BorderLayout.SOUTH);

        
        withdrawButton.addActionListener(this::handleWithdraw);
        depositButton.addActionListener(this::handleDeposit);
        checkBalanceButton.addActionListener(e -> showBalance());
        clearButton.addActionListener(e -> clearFields());

        setVisible(true);
    }

    
    private void handleWithdraw(ActionEvent e) {
        String input = amountField.getText().trim();

        if (input.isEmpty()) {
            showMessage("⚠️ Please enter an amount to withdraw.");
            return;
        }

        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                showMessage("❌ Invalid amount! Please enter a positive value.");
                return;
            }

            boolean success = account.withdraw(amount);
            if (success) {
                showMessage(String.format("✅ Withdrawal successful!\nYou withdrew ₹%.2f\nRemaining balance: ₹%.2f",
                        amount, account.getBalance()));
            } else {
                showMessage(String.format("❌ Transaction failed! Insufficient balance.\nYour balance: ₹%.2f",
                        account.getBalance()));
            }
        } catch (NumberFormatException ex) {
            showMessage("❌ Invalid input! Please enter a numeric amount.");
        }
    }

   
    private void handleDeposit(ActionEvent e) {
        String input = amountField.getText().trim();

        if (input.isEmpty()) {
            showMessage("⚠️ Please enter an amount to deposit.");
            return;
        }

        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                showMessage("❌ Invalid amount! Please enter a positive value.");
                return;
            }

            boolean success = account.deposit(amount);
            if (success) {
                showMessage(String.format("✅ Deposit successful!\nYou deposited ₹%.2f\nNew balance: ₹%.2f",
                        amount, account.getBalance()));
            } else {
                showMessage("❌ Invalid amount entered for deposit.");
            }
        } catch (NumberFormatException ex) {
            showMessage("❌ Invalid input! Please enter a numeric amount.");
        }
    }

    
    private void showBalance() {
        showMessage(String.format("💰 Current Balance: ₹%.2f", account.getBalance()));
    }

    
    private void clearFields() {
        amountField.setText("");
        messageArea.setText("");
    }

    
    private void showMessage(String msg) {
        messageArea.setText(msg);
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATMInterface::new);
    }
}
