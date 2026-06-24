import java.io.*;
import java.util.*;
class Stock {
    String name;
    double price;
    public Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
class Transaction {
    String type;
    String stockName;
    int quantity;
    double price;
    public Transaction(String type, String stockName, int quantity, double price) {
        this.type = type;
        this.stockName = stockName;
        this.quantity = quantity;
        this.price = price;
    }
    public String toString() {
        return type + " | " + stockName + " | Qty: " + quantity + " | ₹" + price;
    }
}
class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    List<Transaction> history = new ArrayList<>();
    double balance = 10000;
    public void buy(Stock stock, int qty) {
        double cost = stock.price * qty;
        if (cost > balance) {
            System.out.println("Insufficient balance!");
            return;
        }
        holdings.put(stock.name, holdings.getOrDefault(stock.name, 0) + qty);
        balance -= cost;
        history.add(new Transaction("BUY", stock.name, qty, stock.price));
        System.out.println("Bought successfully!");
    }
    public void sell(Stock stock, int qty) {
        if (!holdings.containsKey(stock.name) || holdings.get(stock.name) < qty) {
            System.out.println("Not enough shares!");
            return;
        }
        holdings.put(stock.name, holdings.get(stock.name) - qty);
        balance += stock.price * qty;

        history.add(new Transaction("SELL", stock.name, qty, stock.price));
        System.out.println("Sold successfully!");
    }
    public void showPortfolio(List<Stock> market) {
        System.out.println("\n Portfolio:");
        double totalValue = balance;
        for (String stockName : holdings.keySet()) {
            int qty = holdings.get(stockName);
            double price = getStockPrice(stockName, market);
            double value = qty * price;
            totalValue += value;
            System.out.println(stockName + " - " + qty + " shares | Value: " + value);
        }
        System.out.println(" Cash Balance: " + balance);
        System.out.println(" Total Value: " + totalValue);
    }
    private double getStockPrice(String name, List<Stock> market) {
        for (Stock s : market) {
            if (s.name.equals(name)) return s.price;
        }
        return 0;
    }
    public void showHistory() {
        System.out.println("\nTransaction History:");
        for (Transaction t : history) {
            System.out.println(t);
        }
    }
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter("portfolio.txt")) {
            writer.println(balance);

            for (String stock : holdings.keySet()) {
                writer.println(stock + "," + holdings.get(stock));
            }

            System.out.println("Data saved!");
        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }
    public void loadFromFile() {
        try (Scanner file = new Scanner(new File("portfolio.txt"))) {
            balance = Double.parseDouble(file.nextLine());

            while (file.hasNextLine()) {
                String[] data = file.nextLine().split(",");
                holdings.put(data[0], Integer.parseInt(data[1]));
            }

            System.out.println("Data loaded!");
        } catch (Exception e)
         {
            System.out.println("No saved data found");
        }
    }
}
class User {
    String name;
    Portfolio portfolio = new Portfolio();
    public User(String name)
     {
        this.name = name;
    }
}
public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Stock> market = new ArrayList<>();
        market.add(new Stock("TCS", 3500));
        market.add(new Stock("INFY", 1500));
        market.add(new Stock("RELIANCE", 2500));
        System.out.print("Enter your name: ");
        User user = new User(sc.nextLine());
        user.portfolio.loadFromFile();
        while (true) {
            System.out.println("\n==== STOCK TRADING PLATFORM ====");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Transaction History");
            System.out.println("6. Save Data");
            System.out.println("7. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("\nMarket:");
                    for (int i = 0; i < market.size(); i++) {
                        System.out.println((i + 1) + ". " + market.get(i).name +
                                " - ₹" + market.get(i).price);
                    }
                    break;

                case 2:
                    System.out.print("Stock number: ");
                    int b = sc.nextInt() - 1;
                    System.out.print("Quantity: ");
                    int bq = sc.nextInt();

                    if (b >= 0 && b < market.size()) {
                        user.portfolio.buy(market.get(b), bq);
                    }
                    break;

                case 3:
                    System.out.print("Stock number: ");
                    int s = sc.nextInt() - 1;
                    System.out.print("Quantity: ");
                    int sq = sc.nextInt();

                    if (s >= 0 && s < market.size()) {
                        user.portfolio.sell(market.get(s), sq);
                    }
                    break;

                case 4:
                    user.portfolio.showPortfolio(market);
                    break;

                case 5:
                    user.portfolio.showHistory();
                    break;

                case 6:
                    user.portfolio.saveToFile();
                    break;

                case 7:
                    System.out.println("Exiting");
                    System.exit(0);
            }
        }
    }
}
