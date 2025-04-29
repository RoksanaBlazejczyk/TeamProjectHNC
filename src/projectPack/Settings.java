package projectPack;

import javax.swing.*;
import java.awt.*;

public class Settings {

    //Current settings
    private static boolean isDarkMode = false;
    private static int fontSize = 13;
    private static Color fontColor = Color.BLACK;

    /**
     *
     * @param parent
     */
    public static void showSettingsDialog(JFrame parent) {
        //options
        String[] modeOptions = {"Light Mode", "Dark Mode"};
        String[] fontOptions = {"Small", "Medium"};
        String[] colorOptions = {"Black", "Blue", "Red"};

        //User interface elements
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
            //Theme
            isDarkMode = modeBox.getSelectedIndex() == 1;

            //Font Size
            fontSize = fontBox.getSelectedIndex() == 0 ? 12 : 14;

            //Font Color (only for light mode)
            switch (colorBox.getSelectedIndex()) {
                case 1: fontColor = Color.BLUE; break;
                case 2: fontColor = Color.RED; break;
                default: fontColor = Color.BLACK;
            }

            applyTheme(parent);
            applyFontSettings(parent);

            //Refresh user interface
            parent.revalidate();
            parent.repaint();
        }
    }

    /**
     *
     * @param component
     */
    private static void applyTheme(Component component) {
        Color bg = isDarkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fg = isDarkMode ? Color.WHITE : fontColor;

        component.setBackground(bg);
        component.setForeground(fg);

        if (component instanceof JComponent) {
            ((JComponent) component).setOpaque(true);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyTheme(child);
            }
        }
    }

    /**
     *
     * @param component
     */
    private static void applyFontSettings(Component component) {
        if (component instanceof JComponent) {
            Font newFont = new Font("Arial", Font.PLAIN, fontSize);
            component.setFont(newFont);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyFontSettings(child);
            }
        }
    }
}
