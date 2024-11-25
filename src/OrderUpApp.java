import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class OrderUpApp extends JFrame {

    private JLabel totalLabel;
    private DefaultListModel<String> orderListModel; // Model untuk daftar pesanan
    private JList<String> orderList;

    private JPanel itemPanel;
    private JPanel categoryPanel;

    private final String[] items = {"Burger", "Pizza", "Fries", "Soda", "Water", "Juice"};
    private final String[] categories = {"Food", "Drinks"};
    private final int[] prices = {50000, 80000, 30000, 20000, 10000, 15000};
    private final String[] imagePaths = {
            "/images/burger.png",
            "/images/pizza.png",
            "/images/fries.png",
            "/images/soda.png",
            "/images/water.png",
            "/images/juice.png"
    };
    private final String[] itemCategories = {"Food", "Food", "Food", "Drinks", "Drinks", "Drinks"};

    private int[] quantities = new int[items.length]; // Array untuk menyimpan kuantitas tiap item
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public OrderUpApp() {
        setTitle("OrderUp");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Panel kategori (tombol kategori)
        categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.X_AXIS));
        categoryPanel.setBackground(new Color(220, 220, 220));
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        for (String category : categories) {
            JButton categoryButton = new JButton(category);
            styleButton(categoryButton, new Color(70, 130, 180), Color.WHITE);
            categoryButton.addActionListener(e -> loadCategoryItems(category));
            categoryPanel.add(Box.createHorizontalStrut(10)); // Spasi antar tombol
            categoryPanel.add(categoryButton);
        }

        // Panel daftar item (menu)
        itemPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        itemPanel.setBorder(BorderFactory.createTitledBorder(null, "Available Items",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14), Color.DARK_GRAY));
        itemPanel.setBackground(Color.WHITE);


        loadCategoryItems("Food"); // Muat kategori pertama secara default

        // Panel ringkasan pesanan
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder(null, "Order Summary",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14), Color.DARK_GRAY));
        orderPanel.setBackground(Color.WHITE);

        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);
        JScrollPane scrollPane = new JScrollPane(orderList);
        orderPanel.add(scrollPane, BorderLayout.CENTER);

        totalLabel = new JLabel("Total: Rp0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLabel.setForeground(new Color(0, 0, 128));
        orderPanel.add(totalLabel, BorderLayout.SOUTH);

        // Tambahkan ke panel utama
        mainPanel.add(categoryPanel, BorderLayout.NORTH);
        mainPanel.add(itemPanel, BorderLayout.CENTER);
        mainPanel.add(orderPanel, BorderLayout.WEST);

        add(mainPanel);
        setVisible(true);
    }

    private void loadCategoryItems(String category) {
        itemPanel.removeAll(); // Bersihkan itemPanel sebelum menambahkan item baru
        for (int i = 0; i < items.length; i++) {
            if (itemCategories[i].equals(category)) {
                JPanel menuItemPanel = createMenuItemPanel(i);
                itemPanel.add(menuItemPanel);
            }
        }
        itemPanel.revalidate();
        itemPanel.repaint();
    }

    private JPanel createMenuItemPanel(int index) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Gambar menu
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePaths[index]));
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Image not found");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        // Nama dan harga item
        JLabel textLabel = new JLabel("<html><center>" + items[index] + "<br>" + currencyFormatter.format(prices[index]) + "</center></html>");
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel tombol minus, plus, delete, dan label kuantitas
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton minusButton = new JButton("-");
        JButton plusButton = new JButton("+");
        JButton deleteButton = new JButton("Delete");
        JLabel quantityLabel = new JLabel("0");

        // Style tombol
        styleButton(minusButton, new Color(255, 165, 0), Color.WHITE);
        styleButton(plusButton, new Color(0, 128, 0), Color.WHITE);
        styleButton(deleteButton, new Color(255, 69, 0), Color.WHITE);

        // Event listener tombol minus
        minusButton.addActionListener(e -> {
            if (quantities[index] > 0) {
                quantities[index]--;
                quantityLabel.setText(String.valueOf(quantities[index]));
                updateOrderList();
            }
        });

        // Event listener tombol plus
        plusButton.addActionListener(e -> {
            quantities[index]++;
            quantityLabel.setText(String.valueOf(quantities[index]));
            updateOrderList();
        });

        // Event listener tombol delete
        deleteButton.addActionListener(e -> {
            quantities[index] = 0;
            quantityLabel.setText("0");
            updateOrderList();
        });

        // Tambahkan tombol dan label ke panel kontrol
        controlPanel.add(minusButton);
        controlPanel.add(quantityLabel);
        controlPanel.add(plusButton);
        controlPanel.add(deleteButton);

        // Tambahkan komponen ke panel utama
        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(textLabel, BorderLayout.NORTH);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateOrderList() {
        orderListModel.clear();
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < items.length; i++) {
            if (quantities[i] > 0) {
                BigDecimal itemTotal = BigDecimal.valueOf(prices[i]).multiply(BigDecimal.valueOf(quantities[i]));
                orderListModel.addElement(items[i] + " x " + quantities[i] + " = " + currencyFormatter.format(itemTotal));
                total = total.add(itemTotal);
            }
        }

        totalLabel.setText("Total: " + currencyFormatter.format(total));
    }

    private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OrderUpApp::new);
    }
}
