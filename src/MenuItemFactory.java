import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class MenuItemFactory {

    public static JPanel createMenuItem(String name, int price, String imagePath, Runnable updateCallback) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Pemformat mata uang Indonesia
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(MenuItemFactory.class.getResource(imagePath));
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Image not found");
        }

        JLabel textLabel = new JLabel("<html><center>" + name + "<br>" + currencyFormat.format(price) + "</center></html>");
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Kontrol jumlah pesanan
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel quantityLabel = new JLabel("0");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Tombol Plus
        JButton plusButton = new JButton("+");
        plusButton.setBackground(new Color(0, 128, 0));
        plusButton.setForeground(Color.WHITE);
        plusButton.setFont(new Font("Arial", Font.BOLD, 14));
        plusButton.addActionListener(e -> {
            OrderManager.getInstance().addItem(name, price);
            quantityLabel.setText(String.valueOf(OrderManager.getInstance().getOrders().getOrDefault(name, 0)));
            updateCallback.run();
        });

        // Tombol Minus
        JButton minusButton = new JButton("-");
        minusButton.setBackground(new Color(255, 165, 0));
        minusButton.setForeground(Color.WHITE);
        minusButton.setFont(new Font("Arial", Font.BOLD, 14));
        minusButton.addActionListener(e -> {
            OrderManager.getInstance().removeItem(name, price);
            quantityLabel.setText(String.valueOf(OrderManager.getInstance().getOrders().getOrDefault(name, 0)));
            updateCallback.run();
        });

        // Tombol Delete
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(255, 69, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.addActionListener(e -> {
            OrderManager.getInstance().removeItemCompletely(name, price);
            quantityLabel.setText("0");
            updateCallback.run();
        });

        // Tambahkan tombol ke kontrol panel
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
}
