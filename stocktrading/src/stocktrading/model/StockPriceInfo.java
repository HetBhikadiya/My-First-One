package stocktrading.model;

public class StockPriceInfo {
    private String symbol;
    private double previousClose;
    private double todayOpen;
    private double currentPrice;

    public StockPriceInfo(String symbol, double previousClose, double todayOpen, double currentPrice) {
        this.symbol = symbol;
        this.previousClose = previousClose;
        this.todayOpen = todayOpen;
        this.currentPrice = currentPrice;
    }

    public String getSymbol() { return symbol; }
    public double getPreviousClose() { return previousClose; }
    public double getTodayOpen() { return todayOpen; }
    public double getCurrentPrice() { return currentPrice; }

    public void setPreviousClose(double previousClose) { this.previousClose = previousClose; }
    public void setTodayOpen(double todayOpen) { this.todayOpen = todayOpen; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
}
