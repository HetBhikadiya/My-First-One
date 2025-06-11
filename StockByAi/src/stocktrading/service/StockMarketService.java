package stocktrading.service;

import stocktrading.model.Stock;
import stocktrading.model.Transaction;
import stocktrading.exception.InsufficientFundsException;
import stocktrading.exception.InvalidSharesException;
import java.util.*;

public class StockMarketService {
    private List<Stock> availableStocks = new ArrayList<>();
    private List<StockPriceRecord> priceRecords = new ArrayList<>();
    private List<Transaction> transactionHistory = new ArrayList<>();
    private double marketBalance = 0;

    // Inner class to store price information
    private class StockPriceRecord {
        String symbol;
        double previousClose;
        double todayOpen;
        double currentPrice;

        public StockPriceRecord(String symbol, double previousClose, double todayOpen, double currentPrice) {
            this.symbol = symbol;
            this.previousClose = previousClose;
            this.todayOpen = todayOpen;
            this.currentPrice = currentPrice;
        }
    }

    public StockMarketService() {
        initializeStocks();
        initializePrices();
    }

    private void initializeStocks() {
        availableStocks.add(new Stock("REL", "Reliance Industries Limited"));
        availableStocks.add(new Stock("TCS", "Tata Consultancy Services"));
        availableStocks.add(new Stock("HDFC", "HDFC Bank"));
        availableStocks.add(new Stock("ICICI", "ICICI Bank"));
        availableStocks.add(new Stock("INFY", "Infosys Limited"));
        availableStocks.add(new Stock("HUL", "Hindustan Unilever Limited"));
        availableStocks.add(new Stock("SBI", "State Bank of India"));
        availableStocks.add(new Stock("BRTI", "Bharti Airtel"));
        availableStocks.add(new Stock("ADAN", "Adani Enterprises"));
        availableStocks.add(new Stock("WPRO", "Wipro Limited"));
    }

    private void initializePrices() {
        // Initialize with realistic prices
        priceRecords.add(new StockPriceRecord("REL", 2635.85, 0, 0));
        priceRecords.add(new StockPriceRecord("TCS", 3415.20, 0, 0));
        priceRecords.add(new StockPriceRecord("HDFC", 1432.75, 0, 0));
        priceRecords.add(new StockPriceRecord("ICICI", 901.60, 0, 0));
        priceRecords.add(new StockPriceRecord("INFY", 1492.30, 0, 0));
        priceRecords.add(new StockPriceRecord("HUL", 2537.90, 0, 0));
        priceRecords.add(new StockPriceRecord("SBI", 582.45, 0, 0));
        priceRecords.add(new StockPriceRecord("BRTI", 815.20, 0, 0));
        priceRecords.add(new StockPriceRecord("ADAN", 2417.50, 0, 0));
        priceRecords.add(new StockPriceRecord("WPRO", 412.85, 0, 0));

        // Set today's opening prices (random variation from previous close)
        for (StockPriceRecord record : priceRecords) {
            record.todayOpen = (record.previousClose - 20) + (Math.random() * 40);
            record.todayOpen = Math.round(record.todayOpen * 100.0) / 100.0;
        }

        // Initialize current prices
        updateAllPrices();
    }

    public void updateAllPrices() {
        for (StockPriceRecord record : priceRecords) {
            record.currentPrice = (record.previousClose - 50) + (Math.random() * 100);
            record.currentPrice = Math.round(record.currentPrice * 100.0) / 100.0;
        }
    }

    public List<Stock> getAllStocks() {
        return Collections.unmodifiableList(availableStocks);
    }

    public String displayStockData(String symbol) {
        StockPriceRecord record = getPriceRecord(symbol);
        if (record == null) {
            System.out.println("Invalid stock symbol");
            return null;
        }

        System.out.println("\n+------------------------------------------------+");
        System.out.println("|       Stock: " + getCompanyName(symbol) + "       |");
        System.out.println("|       Symbol: " + symbol + "                      |");
        System.out.println("+------------------------------------------------+");
        System.out.println("|       Previous Close: " + record.previousClose + "          |");
        System.out.println("|       Today's Open: " + record.todayOpen + "             |");
        System.out.println("|       Current Price: " + record.currentPrice + "             |");
        System.out.println("+------------------------------------------------+\n");
        return symbol;
    }

    public double getCurrentPrice(String symbol) {
        StockPriceRecord record = getPriceRecord(symbol);
        return record != null ? record.currentPrice : 0.0;
    }

    public double getPreviousClose(String symbol) {
        StockPriceRecord record = getPriceRecord(symbol);
        return record != null ? record.previousClose : 0.0;
    }

    public double getTodayOpen(String symbol) {
        StockPriceRecord record = getPriceRecord(symbol);
        return record != null ? record.todayOpen : 0.0;
    }

    private StockPriceRecord getPriceRecord(String symbol) {
        for (StockPriceRecord record : priceRecords) {
            if (record.symbol.equals(symbol)) {
                return record;
            }
        }
        return null;
    }

    public String getCompanyName(String symbol) {
        for (Stock stock : availableStocks) {
            if (stock.getSymbol().equals(symbol)) {
                return stock.getCompanyName();
            }
        }
        return "Unknown Company";
    }

    public boolean isValidSymbol(String symbol) {
        for (Stock stock : availableStocks) {
            if (stock.getSymbol().equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    public void buyStock(String userId, String symbol, int shares, double userBalance)
            throws InsufficientFundsException, InvalidSharesException {

        if (shares <= 0) {
            throw new InvalidSharesException("Number of shares must be positive");
        }

        double currentPrice = getCurrentPrice(symbol);
        double totalCost = currentPrice * shares;

        if (totalCost > userBalance) {
            throw new InsufficientFundsException(
                    String.format("Not enough balance. Needed: %.2f, Available: %.2f",
                            totalCost, userBalance));
        }

        // Record transaction
        Transaction transaction = new Transaction(
                userId,
                "BUY",
                symbol,
                shares,
                currentPrice
                //LocalDateTime.now()
        );
        transactionHistory.add(transaction);
        marketBalance += totalCost;

        System.out.printf(
                "\nSuccessfully bought %d shares of %s at %.2f per share (Total: %.2f)\n",
                shares, getCompanyName(symbol), currentPrice, totalCost
        );
    }

    public void sellStock(String userId, String symbol, int shares, double averageBuyPrice)
            throws InvalidSharesException {

        if (shares <= 0) {
            throw new InvalidSharesException("Number of shares must be positive");
        }

        double currentPrice = getCurrentPrice(symbol);
        double totalValue = currentPrice * shares;
        double profitLoss = totalValue - (averageBuyPrice * shares);

        // Record transaction
        Transaction transaction = new Transaction(
                userId,
                "SELL",
                symbol,
                shares,
                currentPrice
        );
        transactionHistory.add(transaction);
        marketBalance += totalValue;

        System.out.printf(
                "\nSuccessfully sold %d shares of %s at %.2f per share (Total: %.2f)\n",
                shares, getCompanyName(symbol), currentPrice, totalValue
        );

        System.out.println("+-----------------------------------------+");
        if (profitLoss > 0) {
            System.out.printf("| Profit: %.2f                         |\n", profitLoss);
        } else if (profitLoss < 0) {
            System.out.printf("| Loss: %.2f                           |\n", Math.abs(profitLoss));
        } else {
            System.out.println("| No profit or loss                    |");
        }
        System.out.println("+-----------------------------------------+");
    }

    public List<Transaction> getTransactionsForUser(String userId) {
        List<Transaction> userTransactions = new ArrayList<>();
        for (Transaction t : transactionHistory) {
            if (t.getUserId().equals(userId)) {
                userTransactions.add(t);
            }
        }
        return Collections.unmodifiableList(userTransactions);
    }

    public double getMarketBalance() {
        return marketBalance;
    }

    public void simulateMarketMovement() {
        // Randomly adjust all prices
        for (StockPriceRecord record : priceRecords) {
            double changePercent = (Math.random() * 4) - 2; // -2% to +2%
            record.currentPrice = record.currentPrice * (1 + (changePercent / 100));
            record.currentPrice = Math.round(record.currentPrice * 100.0) / 100.0;
        }
    }
}