import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ThemeManager {
    private Calculator calculator;
    private boolean isDarkTheme;

    // 浅色主题颜色
    private static final Color LIGHT_BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color LIGHT_DISPLAY_BACKGROUND = new Color(255, 255, 255);
    private static final Color LIGHT_BUTTON_BACKGROUND = new Color(250, 250, 250);
    private static final Color LIGHT_BUTTON_FOREGROUND = new Color(50, 50, 50);
    private static final Color LIGHT_OPERATOR_BACKGROUND = new Color(255, 149, 0);
    private static final Color LIGHT_SPECIAL_BUTTON_BACKGROUND = new Color(230, 230, 230);
    private static final Color LIGHT_EQUALS_BUTTON_BACKGROUND = new Color(0, 122, 255);
    private static final Color LIGHT_TEXT_COLOR = new Color(50, 50, 50);
    private static final Color LIGHT_SCIENTIFIC_BUTTON_BACKGROUND = new Color(200, 200, 255);
    private static final Color LIGHT_SCIENTIFIC_BUTTON_FOREGROUND = new Color(50, 50, 150);
    private static final Color LIGHT_BACKSPACE_BACKGROUND = new Color(255, 200, 200);
    private static final Color LIGHT_BACKSPACE_FOREGROUND = new Color(139, 0, 0);

    // 深色主题颜色
    private static final Color DARK_BACKGROUND_COLOR = new Color(50, 50, 50);
    private static final Color DARK_DISPLAY_BACKGROUND = new Color(70, 70, 70);
    private static final Color DARK_BUTTON_BACKGROUND = new Color(80, 80, 80);
    private static final Color DARK_BUTTON_FOREGROUND = new Color(255, 255, 255);
    private static final Color DARK_OPERATOR_BACKGROUND = new Color(255, 149, 0);
    private static final Color DARK_SPECIAL_BUTTON_BACKGROUND = new Color(100, 100, 100);
    private static final Color DARK_EQUALS_BUTTON_BACKGROUND = new Color(0, 122, 255);
    private static final Color DARK_TEXT_COLOR = new Color(255, 255, 255);
    private static final Color DARK_SCIENTIFIC_BUTTON_BACKGROUND = new Color(100, 100, 200);
    private static final Color DARK_SCIENTIFIC_BUTTON_FOREGROUND = new Color(220, 220, 255);
    private static final Color DARK_BACKSPACE_BACKGROUND = new Color(180, 60, 60); // 更浅的红色
    private static final Color DARK_BACKSPACE_FOREGROUND = new Color(255, 220, 220); // 更浅的文字颜色

    public ThemeManager(Calculator calculator) {
        this.calculator = calculator;
        this.isDarkTheme = false;
    }

    public void setTheme(boolean isDark) {
        isDarkTheme = isDark;
        updateTheme();
    }

    public void updateTheme() {
        updateComponentColors(calculator.getContentPane());
        updateMenuBarColors(calculator.getJMenuBar());
        calculator.updateUI();
    }

    private void updateComponentColors(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(getBackgroundColor());
                updateComponentColors((Container) comp);
            } else if (comp instanceof JButton) {
                styleButton((JButton) comp, ((JButton) comp).getText());
            } else if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                textField.setBackground(getDisplayBackgroundColor());
                textField.setForeground(getCurrentTextColor());
                textField.setCaretColor(getCurrentTextColor());
            }
        }
    }

    private void updateMenuBarColors(JMenuBar menuBar) {
        if (menuBar != null) {
            menuBar.setBackground(getBackgroundColor());
            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                JMenu menu = menuBar.getMenu(i);
                updateMenuColors(menu);
            }
        }
    }

    private void updateMenuColors(JMenu menu) {
        menu.setBackground(getBackgroundColor());
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setBackground(getBackgroundColor());
            }
        }
    }

    public void styleButton(JButton button, String text) {
        if (text.equals("←")) {
            button.setBackground(getBackspaceBackgroundColor());
            button.setForeground(getBackspaceForegroundColor());
            button.setFont(new Font("Segoe UI", Font.BOLD, 24));
        } else if (text.matches("[+\\-×÷]")) {
            button.setBackground(getOperatorBackgroundColor());
            button.setFont(new Font("Segoe UI", Font.BOLD, 22));
            button.setForeground(Color.WHITE);
        } else if (text.matches("[%1/x]")) {
            button.setBackground(getSpecialButtonBackgroundColor());
            button.setForeground(getCurrentTextColor());
        } else if (text.equals("=")) {
            button.setBackground(getEqualsButtonBackgroundColor());
            button.setFont(new Font("Segoe UI", Font.BOLD, 22));
            button.setForeground(Color.WHITE);
        } else if (text.matches("sin θ|cos θ|tan θ|ln x|lg x|eˣ|xʸ")) {
            button.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            button.setBackground(getScientificButtonBackgroundColor());
            button.setForeground(getScientificButtonForegroundColor());
        } else {
            button.setBackground(getButtonBackgroundColor());
            button.setForeground(getCurrentTextColor());
        }
    }

    public Color getBackgroundColor() {
        return isDarkTheme ? DARK_BACKGROUND_COLOR : LIGHT_BACKGROUND_COLOR;
    }

    public Color getDisplayBackgroundColor() {
        return isDarkTheme ? DARK_DISPLAY_BACKGROUND : LIGHT_DISPLAY_BACKGROUND;
    }

    public Color getButtonBackgroundColor() {
        return isDarkTheme ? DARK_BUTTON_BACKGROUND : LIGHT_BUTTON_BACKGROUND;
    }

    public Color getCurrentTextColor() {
        return isDarkTheme ? DARK_TEXT_COLOR : LIGHT_TEXT_COLOR;
    }

    public Color getOperatorBackgroundColor() {
        return isDarkTheme ? DARK_OPERATOR_BACKGROUND : LIGHT_OPERATOR_BACKGROUND;
    }

    public Color getSpecialButtonBackgroundColor() {
        return isDarkTheme ? DARK_SPECIAL_BUTTON_BACKGROUND : LIGHT_SPECIAL_BUTTON_BACKGROUND;
    }

    public Color getEqualsButtonBackgroundColor() {
        return isDarkTheme ? DARK_EQUALS_BUTTON_BACKGROUND : LIGHT_EQUALS_BUTTON_BACKGROUND;
    }

    public Color getScientificButtonBackgroundColor() {
        return isDarkTheme ? DARK_SCIENTIFIC_BUTTON_BACKGROUND : LIGHT_SCIENTIFIC_BUTTON_BACKGROUND;
    }

    public Color getScientificButtonForegroundColor() {
        return isDarkTheme ? DARK_SCIENTIFIC_BUTTON_FOREGROUND : LIGHT_SCIENTIFIC_BUTTON_FOREGROUND;
    }

    public Color getBackspaceBackgroundColor() {
        return isDarkTheme ? DARK_BACKSPACE_BACKGROUND : LIGHT_BACKSPACE_BACKGROUND;
    }

    public Color getBackspaceForegroundColor() {
        return isDarkTheme ? DARK_BACKSPACE_FOREGROUND : LIGHT_BACKSPACE_FOREGROUND;
    }

    public Color getPlaceholderColor() {
        return isDarkTheme ? DARK_TEXT_COLOR.darker() : LIGHT_TEXT_COLOR.brighter();
    }
}
