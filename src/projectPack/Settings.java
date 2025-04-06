package projectPack;
import javax.swing.*;
import java.awt.*;

public class Settings {

    // Keep track of the current settings
    private static boolean isDarkMode = false;
    private static int fontSize = 14;
    private static Color fontColor = Color.BLACK;

    public static void showSettingsDialog(JFrame parent) {
        // Mode options
        String[] modeOptions = {"Light Mode", "Dark Mode"};
        String[] fontOptions = {"Small", "Medium"};
        String[] colorOptions = {"Black", "Blue", "Red"};

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JComboBox<String> modeBox = new JComboBox<>(modeOptions);
        JComboBox<String> fontBox = new JComboBox<>(fontOptions);
        JComboBox<String> colorBox = new JComboBox<>(colorOptions);

        panel.add(new JLabel("Theme:"));
        panel.add(modeBox);
        panel.add(new JLabel("Font Size:"));
        panel.add(fontBox);
        panel.add(new JLabel("Font Color:"));
        panel.add(colorBox);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Customize Settings",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            // Apply mode
            isDarkMode = modeBox.getSelectedIndex() == 1;
            applyTheme(parent);

            // Apply font size
            fontSize = fontBox.getSelectedIndex() == 0 ? 12 : 16;

            // Apply font color
            switch (colorBox.getSelectedIndex()) {
                case 1: fontColor = Color.BLUE; break;
                case 2: fontColor = Color.RED; break;
                default: fontColor = Color.BLACK;
            }

            applyFontSettings(parent);
        }
    }

    private static void applyTheme(Component component) {
        if (component instanceof JPanel || component instanceof JFrame) {
            Color bg = isDarkMode ? Color.DARK_GRAY : Color.WHITE;
            Color fg = isDarkMode ? Color.WHITE : Color.BLACK;
            component.setBackground(bg);
            component.setForeground(fg);

            if (component instanceof Container) {
                for (Component child : ((Container) component).getComponents()) {
                    applyTheme(child);
                }
            }
        }
    }

    private static void applyFontSettings(Component component) {
        if (component instanceof JComponent) {
            Font newFont = new Font("Arial", Font.PLAIN, fontSize);
            component.setFont(newFont);
            component.setForeground(fontColor);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyFontSettings(child);
            }
        }
    }
}

