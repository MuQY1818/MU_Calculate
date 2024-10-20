import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class CalculatorUI {
    private Calculator calculator;
    private CalculatorLogic logic;
    private HistoryManager historyManager;
    private ThemeManager themeManager;
    private JTextField display;
    private JPanel buttonPanel;
    private final String placeholder = "Enter calculation here...";

    public CalculatorUI(Calculator calculator, CalculatorLogic logic, HistoryManager historyManager, ThemeManager themeManager) {
        this.calculator = calculator;
        this.logic = logic;
        this.historyManager = historyManager;
        this.themeManager = themeManager;
        initComponents();
    }

    private void initComponents() {
        createDisplay();
        createButtonPanel();
        
        // 设置焦点到第一个按钮
        SwingUtilities.invokeLater(() -> {
            Component[] components = buttonPanel.getComponents();
            if (components.length > 0 && components[0] instanceof JButton) {
                components[0].requestFocusInWindow();
            }
        });
    }

    private void createDisplay() {
        display = new JTextField();
        display.setEditable(true);
        display.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setText(placeholder);
        display.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        addDisplayListeners();
    }

    private void addDisplayListeners() {
        display.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (display.getText().equals(placeholder)) {
                    display.setText("");
                    display.setForeground(themeManager.getCurrentTextColor());
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (display.getText().trim().isEmpty()) {
                    display.setText(placeholder);
                    display.setForeground(themeManager.getPlaceholderColor());
                }
            }
        });
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 添加边缘空间
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        String[] buttons = {
                "7", "8", "9", "÷",
                "4", "5", "6", "×",
                "1", "2", "3", "-",
                "(", "0", ")", "+",
                ".", "%", "1/x", "C",
                "sin θ", "cos θ", "tan θ", "ln x",
                "lg x", "eˣ", "xʸ", "←",
                "=", "Hist"
        };

        for (int i = 0; i < buttons.length; i++) {
            JButton button = createStyledButton(buttons[i]);
            if (i == buttons.length - 2) {  // "=" 按钮
                gbc.gridx = 0;
                gbc.gridy = 7;
                gbc.gridwidth = 2;
            } else if (i == buttons.length - 1) {  // "Hist" 按钮
                gbc.gridx = 2;
                gbc.gridy = 7;
                gbc.gridwidth = 2;
            } else {
                gbc.gridx = i % 4;
                gbc.gridy = i / 4;
                gbc.gridwidth = 1;
            }
            buttonPanel.add(button, gbc);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 22)); 
        button.setPreferredSize(new Dimension(70, 50)); 
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addActionListener(createButtonListener(text));
        themeManager.styleButton(button, text);
        return button;
    }

    private ActionListener createButtonListener(String buttonText) {
        return e -> {
            if (!display.isFocusable()) {
                display.setFocusable(true);
            }

            String currentText = display.getText();
            if (currentText.equals(placeholder) || currentText.startsWith("Error")) {
                currentText = "";
            }

            switch (buttonText) {
                case "=":
                    String result = logic.evaluateExpression(currentText);
                    display.setText(result);
                    historyManager.addToHistory(currentText + " = " + result);
                    break;
                case "C":
                    display.setText(placeholder);
                    display.setForeground(themeManager.getPlaceholderColor());
                    break;
                case "←":
                    if (!currentText.isEmpty() && !currentText.equals(placeholder)) {
                        display.setText(currentText.substring(0, currentText.length() - 1));
                    }
                    break;
                case "Hist":
                    historyManager.showHistory(calculator);
                    break;
                default:
                    if (logic.isAdvancedFunction(buttonText)) {
                        handleAdvancedFunction(buttonText);
                    } else {
                        display.setText(currentText + buttonText);
                    }
                    break;
            }

            if (!display.getText().equals(placeholder)) {
                display.setForeground(themeManager.getCurrentTextColor());
            }
        };
    }

    private void handleAdvancedFunction(String function) {
        try {
            double value = Double.parseDouble(display.getText());
            String result = logic.applyAdvancedFunction(function, value);
            display.setText(result);
        } catch (NumberFormatException ex) {
            display.setText("Error: Invalid input");
        } catch (ArithmeticException ex) {
            display.setText("Error: " + ex.getMessage());
        }
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu settingsMenu = new JMenu("设置");
        JMenu themeMenu = new JMenu("主题");
        
        JMenuItem lightThemeItem = new JMenuItem("浅色主题");
        JMenuItem darkThemeItem = new JMenuItem("深色主题");

        lightThemeItem.addActionListener(e -> themeManager.setTheme(false));
        darkThemeItem.addActionListener(e -> themeManager.setTheme(true));

        themeMenu.add(lightThemeItem);
        themeMenu.add(darkThemeItem);
        settingsMenu.add(themeMenu);

        JMenu toolsMenu = new JMenu("工具");
        JMenuItem loanCalculatorItem = new JMenuItem("贷款计算器");
        loanCalculatorItem.addActionListener(e -> openLoanCalculator());
        toolsMenu.add(loanCalculatorItem);

        menuBar.add(settingsMenu);
        menuBar.add(toolsMenu);

        return menuBar;
    }

    private void openLoanCalculator() {
        SwingUtilities.invokeLater(() -> {
            LoanCalculator loanCalculator = new LoanCalculator();
            loanCalculator.setVisible(true);
        });
    }

    public JTextField getDisplay() {
        return display;
    }

    public JPanel getDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); // 添加边缘空间
        panel.add(display, BorderLayout.CENTER);
        return panel;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }
}
