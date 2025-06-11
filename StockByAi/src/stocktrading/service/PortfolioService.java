package stocktrading.service;

import stocktrading.exception.InsufficientFundsException;
import stocktrading.model.*;
import java.util.*;

public class PortfolioService {
    private Map<String, List<PortfolioItem>> userPortfolios = new HashMap<>();
    private Map<String, Double> userBalances = new HashMap<>();

    public void initializePortfolio(String userId) {
        userPortfolios.putIfAbsent(userId, new ArrayList<>());
        userBalances.putIfAbsent(userId, 0.0);
    }

    public void deposit(String userId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        double currentBalance = userBalances.getOrDefault(userId, 0.0);
        userBalances.put(userId, currentBalance + amount);
    }

    public void withdraw(String userId, double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        double currentBalance = getBalance(userId);
        if (currentBalance < amount) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds. Requested: %.2f, Available: %.2f",
                            amount, currentBalance));
        }

        userBalances.put(userId, currentBalance - amount);
    }

    public double getBalance(String userId) {
        return userBalances.getOrDefault(userId, 0.0);
    }

    public void addToPortfolio(String userId, String stockSymbol, int shares, double purchasePrice) {
        List<PortfolioItem> portfolio = userPortfolios.computeIfAbsent(userId, k -> new ArrayList<>());

        // Check if user already owns this stock
        Optional<PortfolioItem> existingItem = portfolio.stream()
                .filter(item -> item.getStockSymbol().equals(stockSymbol))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update existing holding
            PortfolioItem item = existingItem.get();
            int newShares = item.getShares() + shares;
            double newAvgPrice = ((item.getAveragePrice() * item.getShares()) +
                    (purchasePrice * shares)) / newShares;
            item.setShares(newShares);
            item.setAveragePrice(newAvgPrice);
        } else {
            // Add new holding
            portfolio.add(new PortfolioItem(stockSymbol, shares, purchasePrice));
        }
    }

    public void removeFromPortfolio(String userId, String stockSymbol, int shares) {
        List<PortfolioItem> portfolio = userPortfolios.get(userId);
        if (portfolio == null) return;

        portfolio.stream()
                .filter(item -> item.getStockSymbol().equals(stockSymbol))
                .findFirst()
                .ifPresent(item -> {
                    if (item.getShares() <= shares) {
                        portfolio.remove(item);
                    } else {
                        item.setShares(item.getShares() - shares);
                    }
                });
    }

    public List<PortfolioItem> getPortfolio(String userId) {
        return Collections.unmodifiableList(
                userPortfolios.getOrDefault(userId, new ArrayList<>())
        );
    }

    public int getSharesOwned(String userId, String stockSymbol) {
        return userPortfolios.getOrDefault(userId, new ArrayList<>()).stream()
                .filter(item -> item.getStockSymbol().equals(stockSymbol))
                .findFirst()
                .map(PortfolioItem::getShares)
                .orElse(0);
    }

    public double getAveragePurchasePrice(String userId, String stockSymbol) {
        return userPortfolios.getOrDefault(userId, new ArrayList<>()).stream()
                .filter(item -> item.getStockSymbol().equals(stockSymbol))
                .findFirst()
                .map(PortfolioItem::getAveragePrice)
                .orElse(0.0);
    }
}