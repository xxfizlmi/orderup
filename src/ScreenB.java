import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ScreenB extends JPanel {
    private JPanel itemPanel;
    private JTextArea orderSummaryArea; // Untuk daftar pesanan
    private JLabel totalLabel;         // Untuk total harga

    private final String[] items = {"Burger", "Pizza", "Fries", "Soda", "Water", "Juice"};
    private final int[] prices = {50000, 80000, 30000, 20000, 10000, 15000};
    private final String[] imagePaths = {
            "/images/burger.png",
            "/images/pizza.png",
            "/images/fries.png",
            "/images/soda.png",
            "/images/water.png",
            "/images/juice.png"
    };
    private final String[] categories = {"Makanan", "Minuman"};
    private final String[] itemCategories = {"Makanan", "Makanan", "Makanan", "Minuman", "Minuman", "Minuman"};

    public ScreenB(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Panel kategori
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.X_AXIS));
        categoryPanel.setBackground(new Color(220, 220, 220));
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tambahkan tombol kategori
        for (String category : categories) {
            JButton categoryButton = new JButton(category);
            categoryButton.setFont(new Font("Arial", Font.BOLD, 14));
            categoryButton.setBackground(new Color(70, 130, 180));
            categoryButton.setForeground(Color.WHITE);
            categoryButton.addActionListener(e -> loadCategoryItems(category));
            categoryPanel.add(categoryButton);
            categoryPanel.add(Box.createHorizontalStrut(10)); // Spasi antar tombol
        }

        // Panel item
        itemPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        itemPanel.setBackground(Color.WHITE);

        // Panel pesanan
        JPanel orderPanel = createOrderPanel();

        // Tombol kembali
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(255, 69, 0));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            frame.setContentPane(new ScreenA(frame));
            frame.revalidate();
        });

        // Tombol ulang (reset pesanan)
        JButton resetButton = new JButton("Ulang");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBackground(new Color(70, 130, 180));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(e -> {
            // Reset pesanan di OrderManager
            OrderManager.getInstance().clearOrders();

            // Reset semua komponen kuantitas
            for (Component component : itemPanel.getComponents()) {
                if (component instanceof JPanel) {
                    JPanel menuItemPanel = (JPanel) component;
                    for (Component child : menuItemPanel.getComponents()) {
                        if (child instanceof JPanel) {
                            JPanel controlPanel = (JPanel) child;
                            for (Component controlChild : controlPanel.getComponents()) {
                                if (controlChild instanceof JLabel) {
                                    JLabel quantityLabel = (JLabel) controlChild;
                                    if (quantityLabel.getText().matches("\\d+")) { // Jika teks adalah angka
                                        quantityLabel.setText("0"); // Reset ke 0
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Perbarui ringkasan pesanan
            updateOrderSummary();
        });


        JPanel backButtonPanel = new JPanel();
        backButtonPanel.add(resetButton); // Tambahkan tombol ulang
        backButtonPanel.add(backButton);  // Tambahkan tombol kembali

        // Tambahkan panel kategori, item, pesanan, dan tombol kembali
        add(categoryPanel, BorderLayout.NORTH);
        add(itemPanel, BorderLayout.CENTER);
        add(orderPanel, BorderLayout.EAST);
        add(backButtonPanel, BorderLayout.SOUTH);

        // Muat kategori pertama secara default
        loadCategoryItems("Makanan");
    }

    private void loadCategoryItems(String category) {
        itemPanel.removeAll(); // Bersihkan itemPanel sebelum menambahkan item baru
        for (int i = 0; i < items.length; i++) {
            if (itemCategories[i].equals(category)) {
                JPanel menuItemPanel = MenuItemFactory.createMenuItem(items[i], prices[i], imagePaths[i], this::updateOrderSummary);
                itemPanel.add(menuItemPanel);
            }
        }
        itemPanel.revalidate();
        itemPanel.repaint();
    }

    private JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder(null, "Order Summary",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14), Color.DARK_GRAY));
        orderPanel.setBackground(Color.WHITE);

        orderSummaryArea = new JTextArea(10, 20);
        orderSummaryArea.setEditable(false);
        orderSummaryArea.setFont(new Font("Arial", Font.PLAIN, 14));

        totalLabel = new JLabel("Total: Rp0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        orderPanel.add(new JScrollPane(orderSummaryArea), BorderLayout.CENTER);
        orderPanel.add(totalLabel, BorderLayout.SOUTH);

        return orderPanel;
    }

    public void updateOrderSummary() {
        OrderManager orderManager = OrderManager.getInstance();
        StringBuilder foodSummary = new StringBuilder("Makanan:\n");
        StringBuilder drinkSummary = new StringBuilder("Minuman:\n");
        int[] total = {0}; // Gunakan array untuk menyimpan nilai total

        // Pemformat angka untuk mata uang Indonesia
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        // Loop melalui pesanan dan hitung total langsung dari kuantitas
        orderManager.getOrders().forEach((item, quantity) -> {
            if (quantity > 0) {
                final int index = getItemIndex(item); // Simpan index sebagai final
                if (index != -1) { // Cek jika index valid
                    final int price = prices[index]; // Harga harus final
                    int subtotal = quantity * price; // Subtotal dihitung berdasarkan kuantitas
                    String formattedLine = item + " x " + quantity + " = " + currencyFormat.format(subtotal) + "\n";
                    total[0] += subtotal; // Tambahkan ke total keseluruhan

                    // Tambahkan ke kategori yang sesuai
                    if (itemCategories[index].equals("Makanan")) {
                        foodSummary.append(formattedLine);
                    } else if (itemCategories[index].equals("Minuman")) {
                        drinkSummary.append(formattedLine);
                    }
                }
            }
        });

        // Gabungkan ringkasan makanan dan minuman
        StringBuilder summary = new StringBuilder();
        if (foodSummary.length() > "Makanan:\n".length()) {
            summary.append(foodSummary).append("\n");
        }
        if (drinkSummary.length() > "Minuman:\n".length()) {
            summary.append(drinkSummary);
        }

        // Update UI
        orderSummaryArea.setText(summary.toString());
        totalLabel.setText("Total: " + currencyFormat.format(total[0]));
    }

    private int getItemIndex(String itemName) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(itemName)) {
                return i;
            }
        }
        return -1; // Item tidak ditemukan
    }
}
