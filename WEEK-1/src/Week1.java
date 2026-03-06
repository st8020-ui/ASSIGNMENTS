import java.util.*;

public class Week1 {

    private HashMap<String, Integer> stockMap;
    private LinkedHashMap<Integer, String> waitingList;

    public Week1() {
        stockMap = new HashMap<>();
        waitingList = new LinkedHashMap<>();
    }

    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
    }

    public String checkStock(String productId) {
        if (stockMap.containsKey(productId)) {
            return stockMap.get(productId) + " units available";
        }
        return "Product not found";
    }

    public synchronized String purchaseItem(String productId, int userId) {

        if (!stockMap.containsKey(productId)) {
            return "Product not found";
        }

        int stock = stockMap.get(productId);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        }
        else {
            waitingList.put(userId, productId);
            return "Added to waiting list, position #" + waitingList.size();
        }
    }

    public static void main(String[] args) {

        Week1 system = new Week1();

        system.addProduct("IPHONE15_256GB", 100);

        System.out.println(system.checkStock("IPHONE15_256GB"));

        System.out.println(system.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(system.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 100; i++) {
            system.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(system.purchaseItem("IPHONE15_256GB", 99999));
    }
}