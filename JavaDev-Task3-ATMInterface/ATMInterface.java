import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

// 1. Transaction Class
class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getDetails() {
        return type + ": $" + amount;
    }
}

// 2. Account Class
class Account {
    private String userId;
    private String pin;
    private double balance;
    private ArrayList<Transaction> history;

    public Account(String userId, String pin, double initialBalance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = initialBalance;
        this.history = new ArrayList<>();
    }

    public String getUserId() { return userId; }
    public double getBalance() { return balance; }
    
    public boolean checkPin(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    public void deposit(double amount) {
        balance += amount;
        history.add(new Transaction("Deposit", amount));
        System.out.println("Successfully deposited $" + amount);
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Error: Insufficient Funds!");
            return false;
        }
        balance -= amount;
        history.add(new Transaction("Withdraw", amount));
        System.out.println("Successfully withdrew $" + amount);
        return true;
    }
    
    public void recordTransfer(String type, double amount) {
        history.add(new Transaction(type, amount));
    }

    public ArrayList<Transaction> getHistory() {
        return history;
    }
}

// 3. Bank Class
class Bank {
    private HashMap<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }

    public void addAccount(Account account) {
        accounts.put(account.getUserId(), account);
    }

    public Account authenticate(String userId, String pin) {
        Account acc = accounts.get(userId);
        if (acc != null && acc.checkPin(pin)) {
            return acc;
        }
        return null;
    }

    public Account getAccount(String userId) {
        return accounts.get(userId);
    }
}

// 4. ATM Class
class ATM {
    private Bank bank;
    private Account currentAccount;
    private Scanner scanner;

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Welcome to the ATM ===");
        loginAttempt(3); // Start with 3 attempts
    }

    // Recursive Login Logic (No loops)
    private void loginAttempt(int attemptsLeft) {
        if (attemptsLeft == 0) {
            System.out.println("Maximum incorrect attempts reached. Exiting system.");
            return;
        }

        System.out.print("\nEnter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        currentAccount = bank.authenticate(userId, pin);

        if (currentAccount != null) {
            System.out.println("\nLogin Successful! Welcome, " + userId);
            showMainMenu();
        } else {
            System.out.println("Invalid ID or PIN. Attempts remaining: " + (attemptsLeft - 1));
            loginAttempt(attemptsLeft - 1); 
        }
    }

    // Recursive Menu Display
    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
        System.out.print("Select an option (1-5): ");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                System.out.println("\n-- Transaction History --");
                ArrayList<Transaction> hist = currentAccount.getHistory();
                if (hist.isEmpty()) {
                    System.out.println("No past transactions.");
                } else {
                    printHistoryRecursively(hist, 0); 
                }
                break;
            case 2:
                System.out.print("\nEnter amount to withdraw: $");
                double wAmount = Double.parseDouble(scanner.nextLine());
                currentAccount.withdraw(wAmount);
                break;
            case 3:
                System.out.print("\nEnter amount to deposit: $");
                double dAmount = Double.parseDouble(scanner.nextLine());
                currentAccount.deposit(dAmount);
                break;
            case 4:
                System.out.print("\nEnter recipient User ID: ");
                String recipientId = scanner.nextLine();
                Account recipient = bank.getAccount(recipientId);
                
                if (recipient == null) {
                    System.out.println("Error: Recipient account not found.");
                } else if (recipientId.equals(currentAccount.getUserId())) {
                    System.out.println("Error: Cannot transfer to your own account.");
                } else {
                    System.out.print("Enter amount to transfer: $");
                    double tAmount = Double.parseDouble(scanner.nextLine());
                    
                    if (currentAccount.withdraw(tAmount)) { 
                        recipient.deposit(tAmount);
                       
                        currentAccount.getHistory().remove(currentAccount.getHistory().size() - 1);
                        recipient.getHistory().remove(recipient.getHistory().size() - 1);
                        
                        currentAccount.recordTransfer("Transfer Out to " + recipientId, tAmount);
                        recipient.recordTransfer("Transfer In from " + currentAccount.getUserId(), tAmount);
                        System.out.println("Transfer of $" + tAmount + " to " + recipientId + " successful.");
                    }
                }
                break;
            case 5:
                System.out.println("\nThank you for using our ATM. Goodbye!");
                return; 
            default:
                System.out.println("Invalid option. Please try again.");
        }
        
        showMainMenu();
    }

    private void printHistoryRecursively(ArrayList<Transaction> list, int index) {
        if (index >= list.size()) {
            return;
        }
        System.out.println(list.get(index).getDetails());
        printHistoryRecursively(list, index + 1);
    }
}

// 5. Main Class
public class ATMInterface {
    public static void main(String[] args) {
        Bank myBank = new Bank();
        
        // Creating some dummy accounts
        myBank.addAccount(new Account("user1", "1234", 1000.0));
        myBank.addAccount(new Account("user2", "5678", 500.0));

        ATM myAtm = new ATM(myBank);
        
        myAtm.start();
    }
}