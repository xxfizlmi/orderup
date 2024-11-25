import javax.swing.*;

public class    Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("OrderUp - MultiScreen");
            frame.setSize(900, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            // Start with Screen A
            frame.setContentPane(new ScreenA(frame));
            frame.setVisible(true);
        });
    }
}
