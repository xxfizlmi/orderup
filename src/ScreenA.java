import javax.swing.*;
import java.awt.*;

public class ScreenA extends JPanel {
    public ScreenA(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/orderup_logo.png"));
            Image logoImage = logoIcon.getImage().getScaledInstance(300, 100, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(logoImage));
        } catch (Exception e) {
            logoLabel.setText("OrderUp");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 36));
            logoLabel.setForeground(new Color(0, 0, 128));
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton openButton = new JButton("Open Menu");
        openButton.setFont(new Font("Arial", Font.BOLD, 16));
        openButton.setBackground(new Color(0, 128, 0));
        openButton.setForeground(Color.WHITE);
        openButton.addActionListener(e -> {
            frame.setContentPane(new ScreenB(frame));
            frame.revalidate();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);

        add(logoLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
