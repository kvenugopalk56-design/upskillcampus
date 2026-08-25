import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Account {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private ArrayList<String> transactionHistory;

    public Account(String accountNumber, String accountHolderName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        this.transactionHistory.add("Account created with initial deposit: $" + String.format("%.2f", initialDeposit));
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add("Deposited: $" + String.format("%.2f", amount) + " | Balance: $" + String.format("%.2f", balance));
            System.out.println("Successfully deposited $" + String.format("%.2f", amount));
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
            return false;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds. Available balance: $" + String.format("%.2f", balance));
            return false;
        }
        balance -= amount;
        transactionHistory.add("Withdrew: $" + String.format("%.2f", amount) + " | Balance: $" + String.format("%.2f", balance));
        System.out.println("Successfully withdrew $" + String.format("%.2f", amount));
        return true;
    }

    public void printStatement() {
        System.out.println("\n--- Account Statement ---");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Holder Name   : " + accountHolderName);
        System.out.println("Current Balance: $" + String.format("%.2f", balance));
        System.out.println("--- Transaction History ---");
        for (String record : transactionHistory) {
            System.out.println(record);
        }
    }
}

class Bank {
    private Map<String, Account> accounts = new HashMap<>();
    private int accountCounter = 1001;

    public String createAccount(String name, double initialDeposit) {
        if (initialDeposit < 0) {
            System.out.println("Initial deposit cannot be negative.");
            return null;
        }
        String accNum = "ACC" + accountCounter++;
        Account newAccount = new Account(accNum, name, initialDeposit);
        accounts.put(accNum, newAccount);
        return accNum;
    }

    public Account findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean transferFunds(String fromAccNum, String toAccNum, double amount) {
        Account fromAcc = findAccount(fromAccNum);
        Account toAcc = findAccount(toAccNum);

        if (fromAcc == null || toAcc == null) {
            System.out.println("Error: One or both account numbers do not exist.");
            return false;
        }
        if (fromAccNum.equals(toAccNum)) {
            System.out.println("Error: Source and destination accounts cannot be identical.");
            return false;
        }

        if (fromAcc.withdraw(amount)) {
            toAcc.deposit(amount);
            System.out.println("Transfer of $" + String.format("%.2f", amount) + " completed successfully.");
            return true;
        }
        return false;
    }
}

public class BankingSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== BANKING INFORMATION SYSTEM ===");
            System.out.println("1. Open New Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Funds");
            System.out.println("5. Check Balance & Mini-Statement");
            System.out.println("6. Exit");
            System.out.print("Select an option (1-6): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter Account Holder Full Name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Enter Initial Deposit: $");
                    double deposit = parseAmount(scanner.nextLine());
                    String accNum = bank.createAccount(name, deposit);
                    if (accNum != null) {
                        System.out.println("Account created successfully! Your Account Number is: " + accNum);
                    }
                    break;

                case "2":
                    System.out.print("Enter Account Number: ");
                    String depAcc = scanner.nextLine().trim();
                    Account targetDep = bank.findAccount(depAcc);
                    if (targetDep != null) {
                        System.out.print("Enter Deposit Amount: $");
                        targetDep.deposit(parseAmount(scanner.nextLine()));
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case "3":
                    System.out.print("Enter Account Number: ");
                    String withAcc = scanner.nextLine().trim();
                    Account targetWith = bank.findAccount(withAcc);
                    if (targetWith != null) {
                        System.out.print("Enter Withdrawal Amount: $");
                        targetWith.withdraw(parseAmount(scanner.nextLine()));
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case "4":
                    System.out.print("Enter Source Account Number: ");
                    String src = scanner.nextLine().trim();
                    System.out.print("Enter Destination Account Number: ");
                    String dest = scanner.nextLine().trim();
                    System.out.print("Enter Amount to Transfer: $");
                    double transferAmt = parseAmount(scanner.nextLine());
                    bank.transferFunds(src, dest, transferAmt);
                    break;

                case "5":
                    System.out.print("Enter Account Number: ");
                    String stmtAcc = scanner.nextLine().trim();
                    Account targetStmt = bank.findAccount(stmtAcc);
                    if (targetStmt != null) {
                        targetStmt.printStatement();
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case "6":
                    System.out.println("Thank you for using the Banking Information System. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid selection. Please enter a digit between 1 and 6.");
            }
        }
        scanner.close();
    }

    private static double parseAmount(String input) {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
}
