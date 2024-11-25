import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderManager {
    private static OrderManager instance;

    private final Map<String, Integer> orders;
    private BigDecimal total;

    private OrderManager() {
        orders = new HashMap<>();
        total = BigDecimal.ZERO;
    }

    // Singleton Instance
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    // Tambahkan item ke pesanan
    public void addItem(String item, int price) {
        orders.put(item, orders.getOrDefault(item, 0) + 1);
        total = total.add(BigDecimal.valueOf(price));
    }

    // Kurangi item dari pesanan
    public void removeItem(String item, int price) {
        if (orders.containsKey(item) && orders.get(item) > 0) {
            orders.put(item, orders.get(item) - 1);
            total = total.subtract(BigDecimal.valueOf(price));
            // Hapus item jika kuantitasnya 0
            if (orders.get(item) == 0) {
                orders.remove(item);
            }
        }
    }

    // Hapus seluruh item dari pesanan
    public void removeItemCompletely(String item, int price) {
        if (orders.containsKey(item)) {
            int quantity = orders.get(item);
            total = total.subtract(BigDecimal.valueOf(price * quantity));
            orders.remove(item);
        }
    }

    // Bersihkan semua pesanan
    public void clearOrders() {
        orders.clear();
        total = BigDecimal.ZERO;
    }

    // Dapatkan daftar pesanan
    public Map<String, Integer> getOrders() {
        return new HashMap<>(orders); // Return a copy to prevent external modification
    }

    // Dapatkan total harga
    public BigDecimal getTotal() {
        return total;
    }

    // Dapatkan total dalam format mata uang IDR
    public String getFormattedTotal() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(total);
    }
}
