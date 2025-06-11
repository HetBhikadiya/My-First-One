package stocktrading.model;

public class Transaction {
    private String userId;
    private String type;
    private String stockSymbol;
    private int shares;
    private double price;

    public Transaction(String userId, String type, String stockSymbol, int shares, double price) {
        this.type = type;
        this.stockSymbol = stockSymbol;
        this.shares = shares;
        this.price = price;
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
