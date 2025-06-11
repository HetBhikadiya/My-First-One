package stocktrading.model;

public class PortfolioItem {
    private String stockSymbol;
    private int shares;
    private double averagePrice;

    public PortfolioItem(String stockSymbol, int shares, double averagePrice) {
        this.stockSymbol = stockSymbol;
        this.shares = shares;
        this.averagePrice = averagePrice;
    }

    // Getters and Setters
    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }
}