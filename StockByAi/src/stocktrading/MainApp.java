package stocktrading;

import stocktrading.service.*;
import stocktrading.model.*;
import stocktrading.exception.*;
import stocktrading.util.*;

import java.util.*;
public class MainApp {
    private static UserService userService = new UserService();
    private static StockMarketService stockService = new StockMarketService();
    private static PortfolioService portfolioService = new PortfolioService();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;

    public static void main(String[] args) {
        try {
            showWelcomeScreen();
            boolean exit = false;

            while (!exit) {
                if (currentUser == null) {
                    exit = showAuthMenu();
                } else {
                    exit = showMainMenu();
                }
            }

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            System.out.println("\nThank you for using Stock Trading App!");
        }
    }

    private static void showWelcomeScreen() {
        StockPrinter.print("STOCK TRADING PLATFORM");
        System.out.println("A secure platform for stock market trading\n");
    }
    
    private static boolean showAuthMenu() {
        System.out.println("\n1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                registerUser();
                return false;
            case 2:
                loginUser();
                return false;
            case 3:
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
    }

    private static void registerUser() {
        try {
            StockPrinter.print("REGISTRATION");
            currentUser = userService.signUp();
            System.out.println("\nRegistration successful! Your user ID: " + currentUser.getUserId());
            userService.login(currentUser.getEmail(), currentUser.getPassword());
            System.out.println("Welcome, " + currentUser.getName() + "!");

            // Initialize user's portfolio
            portfolioService.initializePortfolio(currentUser.getUserId());

            // Add initial balance
            addInitialBalance();
        } catch (InvalidDateException | InvalidMonthException | InvalidPasswordException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void loginUser() {
        StockPrinter.print("LOGIN");

        // Check if any users exist
        if (!userService.hasRegisteredUsers()) {
            System.out.println("\nNo users registered yet. Please register first.");
            System.out.println("Press 1 to register or any other key to go back");
            String input = scanner.nextLine();
            if (input.equals("1")) {
                registerUser();
            }
            return;
        }

        // Ask for credentials only once
        String email=userService.validateEmail();
        String password=null;
        try {
             password = userService.validatePassword();
        } catch (InvalidPasswordException e) {
            System.out.println("Login failed: Invalid credentials");
        }

        try {
            User user = userService.login(email, password);
            currentUser = user;
            System.out.println("\nLogin successful! Your user ID: " + currentUser.getUserId());
            System.out.println("Welcome back, " + currentUser.getName() + "!");
        } catch (InvalidPasswordException e) {
            System.out.println("Login failed: Invalid credentials");
        }
    }

    private static void addInitialBalance() {
        boolean validAmount = false;

        while (!validAmount) {
            StockPrinter.print("INITIAL DEPOSIT");
            System.out.println("To start trading, please deposit funds into your account.");
            System.out.print("Enter amount to deposit (minimum ₹1000): ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            if (amount < 1000) {
                System.out.println("Minimum deposit amount is ₹1000");
            } else {
                portfolioService.deposit(currentUser.getUserId(), amount);
                System.out.printf("\nSuccessfully deposited ₹%.2f\n", amount);
                System.out.printf("Current balance: ₹%.2f\n", portfolioService.getBalance(currentUser.getUserId()));
                validAmount = true;
            }
        }
    }

    private static boolean showMainMenu() {
        StockPrinter.print("MAIN MENU");
        System.out.printf("Welcome, %s! (Balance: ₹%.2f)\n",
                currentUser.getName(),
                portfolioService.getBalance(currentUser.getUserId()));

        System.out.println("\n1. View Available Stocks");
        System.out.println("2. Buy Stocks");
        System.out.println("3. Sell Stocks");
        System.out.println("4. View Portfolio");
        System.out.println("5. View Transaction History");
        System.out.println("6. Deposit Funds");
        System.out.println("7. Logout");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewAvailableStocks();
                return false;
            case 2:
                buyStocks();
                return false;
            case 3:
                sellStocks();
                return false;
            case 4:
                viewPortfolio();
                return false;
            case 5:
                viewTransactionHistory();
                return false;
            case 6:
                depositFunds();
                return false;
            case 7:
                logout();
                return false;
            case 8:
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
    }

    private static String viewAvailableStocks() {
        StockPrinter.print("AVAILABLE STOCKS");
        System.out.printf("%-6s %-30s %-10s %-10s %-10s\n",
                "Symbol", "Company", "Shares", "Avg Price", "Current");
         System.out.println("----------------------------------------------------------------");

        stockService.getAllStocks().forEach(stock -> {
            String symbol = stock.getSymbol();
            System.out.printf("%-6s %-30s ₹%-9.2f ₹%-9.2f ₹%-9.2f\n",
                    symbol,
                    stock.getCompanyName(),
                    stockService.getPreviousClose(symbol),
                    stockService.getTodayOpen(symbol),
                    stockService.getCurrentPrice(symbol));
        });

        System.out.println("\nEnter any stock symbol to view details (or 'back' to return): ");
        String input = scanner.nextLine();

        if (!input.equalsIgnoreCase("back")) {
            String s=stockService.displayStockData(input.toUpperCase());
            return s;
        }else{
            showMainMenu();
        }
        return null;
    }

    private static void buyStocks() {
        StockPrinter.print("BUY STOCKS");
        String s=viewAvailableStocks();

        double currentPrice = stockService.getCurrentPrice(s);
        double balance = portfolioService.getBalance(currentUser.getUserId());
        int maxShares = (int)(balance / currentPrice);

        System.out.printf("\nCurrent price of %s: ₹%.2f\n",
                stockService.getCompanyName(s), currentPrice);
        System.out.printf("Your balance: ₹%.2f (Can buy max %d shares)\n",
                balance, maxShares);

        if (maxShares == 0) {
            System.out.println("Insufficient funds to buy this stock");
            return;
        }

        System.out.print("Enter number of shares to buy: ");
        try {
            int shares = scanner.nextInt();
            scanner.nextLine();

            stockService.buyStock(
                    currentUser.getUserId(),
                    s,
                    shares,
                    portfolioService.getBalance(currentUser.getUserId())
            );

            portfolioService.addToPortfolio(
                    currentUser.getUserId(),
                    s,
                    shares,
                    currentPrice
            );
            portfolioService.withdraw(
                    currentUser.getUserId(),
                    shares * currentPrice
            );

            System.out.printf("\nSuccessfully bought %d shares of %s at ₹%.2f\n",
                    shares, stockService.getCompanyName(s), currentPrice);

        } catch (InsufficientFundsException | InvalidSharesException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }

    private static void sellStocks() {
        StockPrinter.print("SELL STOCKS");

        // Get user's portfolio
        List<PortfolioItem> portfolio = portfolioService.getPortfolio(currentUser.getUserId());
        if (portfolio.isEmpty()) {
            System.out.println("You don't own any stocks to sell.");
            return;
        }

        // Display portfolio
        System.out.printf("%-6s %-30s %-10s %-10s %-10s\n",
                "Symbol", "Company", "Shares", "Avg Price", "Current");
        System.out.println("----------------------------------------------------------------");

        portfolio.forEach(item -> {
            String symbol = item.getStockSymbol();
            System.out.printf("%-6s %-30s %-10d ₹%-9.2f ₹%-9.2f\n",
                    symbol,
                    stockService.getCompanyName(symbol),
                    item.getShares(),
                    item.getAveragePrice(),
                    stockService.getCurrentPrice(symbol));
        });

        System.out.print("\nEnter stock symbol you want to sell: ");
        String symbol = scanner.nextLine().toUpperCase();

        int sharesOwned = portfolioService.getSharesOwned(currentUser.getUserId(), symbol);
        if (sharesOwned == 0) {
            System.out.println("You don't own any shares of " + symbol);
            return;
        }

        System.out.printf("\nYou own %d shares of %s (Average price: ₹%.2f)\n",
                sharesOwned, stockService.getCompanyName(symbol),
                portfolioService.getAveragePurchasePrice(currentUser.getUserId(), symbol));
        System.out.printf("Current price: ₹%.2f\n", stockService.getCurrentPrice(symbol));

        System.out.print("Enter number of shares to sell: ");
        try {
            int shares = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (shares <= 0) {
                System.out.println("Number of shares must be positive");
                return;
            }

            if (shares > sharesOwned) {
                System.out.println("You don't own that many shares");
                return;
            }

            double currentPrice = stockService.getCurrentPrice(symbol);
            double avgPrice = portfolioService.getAveragePurchasePrice(currentUser.getUserId(), symbol);

            // Execute sale
            stockService.sellStock(
                    currentUser.getUserId(),
                    symbol,
                    shares,
                    avgPrice
            );

            // Update portfolio
            portfolioService.removeFromPortfolio(currentUser.getUserId(), symbol, shares);

            // Add to balance
            portfolioService.deposit(
                    currentUser.getUserId(),
                    shares * currentPrice
            );

        } catch (InvalidSharesException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }

    private static void viewPortfolio() {
        StockPrinter.print("YOUR PORTFOLIO");

        List<PortfolioItem> portfolio = portfolioService.getPortfolio(currentUser.getUserId());
        double totalValue = 0;
        double totalInvestment = 0;

        if (portfolio.isEmpty()) {
            System.out.println("You don't own any stocks yet.");
            return;
        }

        System.out.println("Symbol Company                        Shares     Avg Price  Current");
        System.out.println("----------------------------------------------------------------");
        for (PortfolioItem item : portfolio) {
            String symbol = item.getStockSymbol();
            double currentPrice = stockService.getCurrentPrice(symbol);
            double value = item.getShares() * currentPrice;
            double investment = item.getShares() * item.getAveragePrice();
            double profitLoss = value - investment;

            System.out.printf("%-6s %-30s %-10d ₹%-9.2f ₹%-9.2f ₹%-9.2f\n",
                    symbol,
                    stockService.getCompanyName(symbol),
                    item.getShares(),
                    item.getAveragePrice(),
                    currentPrice,
                    profitLoss);

            totalValue += value;
            totalInvestment += investment;
        }

        double totalProfitLoss = totalValue - totalInvestment;
        System.out.println("\n+-----------------------------------------+");
        System.out.printf("| Total Investment: ₹%.2f               |\n", totalInvestment);
        System.out.printf("| Current Value:    ₹%.2f               |\n", totalValue);
        System.out.printf("| Total P/L:        ₹%.2f               |\n", totalProfitLoss);
        System.out.println("+-----------------------------------------+");
        System.out.printf("\nAvailable Balance: ₹%.2f\n", portfolioService.getBalance(currentUser.getUserId()));
    }

    private static void viewTransactionHistory() {
        StockPrinter.print("TRANSACTION HISTORY");

        List<Transaction> transactions = stockService.getTransactionsForUser(currentUser.getUserId());

        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        System.out.printf("%-10s %-6s %-5s %-10s %-10s %-20s\n",
                "Type", "Symbol", "Qty", "Price", "Amount", "Date/Time");
        System.out.println("----------------------------------------------------------------");

        for (Transaction t : transactions) {
            System.out.printf("%-10s %-6s %-5d ₹%-9.2f ₹%-9.2f %-20s\n",
                    t.getType(),
                    t.getStockSymbol(),
                    t.getShares(),
                    t.getPrice(),
                    t.getShares() * t.getPrice());
        }
    }

    private static void depositFunds() {
        StockPrinter.print("DEPOSIT FUNDS");
        System.out.printf("Current balance: ₹%.2f\n", portfolioService.getBalance(currentUser.getUserId()));

        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (amount <= 0) {
            System.out.println("Amount must be positive");
            return;
        }

        portfolioService.deposit(currentUser.getUserId(), amount);
        System.out.printf("\nSuccessfully deposited ₹%.2f\n", amount);
        System.out.printf("New balance: ₹%.2f\n", portfolioService.getBalance(currentUser.getUserId()));

    }

    private static void logout() {
        currentUser = null;
        System.out.println("\nYou have been logged out successfully.");
    }
}