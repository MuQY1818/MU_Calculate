import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Calculator extends JFrame {
    private JTextField display; // 显示输入和结果的文本框
    private final String placeholder = "Enter calculation here..."; // 提示词
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color DISPLAY_BACKGROUND = new Color(255, 255, 255);
    private static final Color BUTTON_BACKGROUND = new Color(250, 250, 250);
    private static final Color BUTTON_FOREGROUND = new Color(50, 50, 50);
    private static final Color OPERATOR_BACKGROUND = new Color(255, 149, 0);
    private static final Color SPECIAL_BUTTON_BACKGROUND = new Color(230, 230, 230);
    private static final Color EQUALS_BUTTON_BACKGROUND = new Color(0, 122, 255);

    private ImageIcon icon;
    private ArrayList<String> history = new ArrayList<>();
    private JButton historyButton;
    private ImageIcon historyIcon;

    // 浅色主题颜色
    private static final Color LIGHT_BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color LIGHT_DISPLAY_BACKGROUND = new Color(255, 255, 255);
    private static final Color LIGHT_BUTTON_BACKGROUND = new Color(250, 250, 250);
    private static final Color LIGHT_BUTTON_FOREGROUND = new Color(50, 50, 50);
    private static final Color LIGHT_OPERATOR_BACKGROUND = new Color(255, 149, 0);
    private static final Color LIGHT_SPECIAL_BUTTON_BACKGROUND = new Color(230, 230, 230);
    private static final Color LIGHT_EQUALS_BUTTON_BACKGROUND = new Color(0, 122, 255);

    // 深色主题颜色
    private static final Color DARK_BACKGROUND_COLOR = new Color(50, 50, 50);
    private static final Color DARK_DISPLAY_BACKGROUND = new Color(70, 70, 70);
    private static final Color DARK_BUTTON_BACKGROUND = new Color(80, 80, 80);
    private static final Color DARK_BUTTON_FOREGROUND = new Color(255, 255, 255);
    private static final Color DARK_OPERATOR_BACKGROUND = new Color(255, 149, 0);
    private static final Color DARK_SPECIAL_BUTTON_BACKGROUND = new Color(100, 100, 100);
    private static final Color DARK_EQUALS_BUTTON_BACKGROUND = new Color(0, 122, 255);

    private boolean isDarkTheme = false;

    private JMenuItem lightThemeItem;
    private JMenuItem darkThemeItem;

    private static final Color LIGHT_TEXT_COLOR = new Color(50, 50, 50);
    private static final Color DARK_TEXT_COLOR = new Color(255, 255, 255);

    private Color currentTextColor;

    public Calculator() {
        // 设置系统外观
        try {
            // 设置现代Look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置窗口标题和布局管理器
        setTitle("简单计算器");
        setLayout(new BorderLayout());

        // 创建显示框
        display = new JTextField();
        display.setEditable(true); // 允许用户直接在文本框中输入
        display.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        display.setBackground(DISPLAY_BACKGROUND);
        display.setHorizontalAlignment(JTextField.RIGHT); // 设置文本右对齐
        display.setText(placeholder); // 初始时显示提示词
        currentTextColor = LIGHT_TEXT_COLOR;
        display.setForeground(currentTextColor.brighter());
        // 设置边框样式
        display.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        // 焦点监听器实现提示文字功能
        display.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            // 获得焦点时，清除提示文字
            public void focusGained(java.awt.event.FocusEvent e) {
                if (display.getText().equals(placeholder)) {
                    display.setText("");
                    display.setForeground(Color.BLACK);
                }
            }

            @Override
            // 失去焦点时，如果文本框为空，显示提示文字
            public void focusLost(java.awt.event.FocusEvent e) {
                if (display.getText().trim().isEmpty()) {
                    display.setText(placeholder);
                    display.setForeground(Color.GRAY);
                }
            }
        });

        add(display, BorderLayout.NORTH);

        // 创建按钮面板
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 按钮
        String[] buttons = {
                "7", "8", "9", "÷",
                "4", "5", "6", "×",
                "1", "2", "3", "-",
                "(", "0", ")", "+",
                "√", "%", "1/x", "C"
        };

        // 添加基本按钮
        int index = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 4; col++) {
                JButton button = createStyledButton(buttons[index]);
                gbc.gridx = col;
                gbc.gridy = row;
                gbc.gridwidth = 1;
                gbc.gridheight = 1;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                panel.add(button, gbc);
                index++;
            }
        }

        // 修改高级函数按钮
        String[] advancedButtons = {"sin θ", "cos θ", "tan θ", "ln x", "log x", "eˣ", "xʸ", "←"};
        int row = 5;
        for (int col = 0; col < advancedButtons.length; col++) {
            JButton button = createStyledButton(advancedButtons[col]);
            gbc.gridx = col % 4;
            gbc.gridy = row + col / 4;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            panel.add(button, gbc);
        }

        // 添加等号按钮
        JButton equalsButton = createStyledButton("=");
        styleEqualsButton(equalsButton);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        panel.add(equalsButton, gbc);

        // 修改历史记录按钮
        historyButton = createHistoryButton();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        panel.add(historyButton, gbc);

        // 添加退格按钮
        JButton backspaceButton = createStyledButton("←");
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.weighty = 0.5;
        panel.add(backspaceButton, gbc);

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // 创建设置菜单
        JMenu settingsMenu = new JMenu("设置");
        menuBar.add(settingsMenu);

        // 创建主题子菜单
        JMenu themeMenu = new JMenu("主题");
        settingsMenu.add(themeMenu);

        // 建主题选项
        lightThemeItem = new JMenuItem("浅色主题");
        darkThemeItem = new JMenuItem("深色主题");

        lightThemeItem.addActionListener(e -> setTheme(false));
        darkThemeItem.addActionListener(e -> setTheme(true));

        themeMenu.add(lightThemeItem);
        themeMenu.add(darkThemeItem);

        // 初始化主题
        updateThemeMenuItems();

        add(panel, BorderLayout.CENTER);
        setSize(400, 600);  // 调整为更大的尺寸
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 使用 SwingUtilities.invokeLater 来设置初始状态
        // 加载图标
        try {
            icon = new ImageIcon(getClass().getResource("/Calculator_31111.png"));
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                setIconImage(icon.getImage());
            } else {
                System.out.println("Failed to load icon.");
            }
        } catch (Exception e) {
            System.out.println("Error loading icon: " + e.getMessage());
        }
        SwingUtilities.invokeLater(this::setInitialState);
        // 创建工具菜单
        JMenu toolsMenu = new JMenu("工具");
        JMenuItem loanCalculatorItem = new JMenuItem("贷款计算器");
        loanCalculatorItem.addActionListener(e -> openLoanCalculator());
        toolsMenu.add(loanCalculatorItem);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);

        // 设置窗口背景色
        getContentPane().setBackground(BACKGROUND_COLOR);

        // 加载历史记录图标
        try {
            historyIcon = new ImageIcon(getClass().getResource("/history_icon.png"));
            Image img = historyIcon.getImage();
            Image newImg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            historyIcon = new ImageIcon(newImg);
        } catch (Exception e) {
            System.out.println("Error loading history icon: " + e.getMessage());
        }

        // 添加文档监听器来处理文本变化
        display.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTextColor();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTextColor();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTextColor();
            }
        });
    }

    private void setInitialState() {
        display.setText(placeholder);
        display.setForeground(Color.GRAY);
        display.setFocusable(false);  // 禁用文本框的可聚焦性，避免初始状态时自动获得焦点

        // 将焦点设置到第一个按钮上
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getComponentCount() > 0) {
                    panel.getComponent(0).requestFocusInWindow();
                    break;
                }
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addActionListener(new ButtonClickListener());
        
        // 根据按钮类型设置初始背景色和前景色
        if (text.matches("[+\\-×÷]")) {
            button.setBackground(OPERATOR_BACKGROUND);
            button.setFont(new Font("Segoe UI", Font.BOLD, 22));
            button.setForeground(Color.WHITE);
        } else if (text.matches("[√%1/x]")) {
            button.setBackground(SPECIAL_BUTTON_BACKGROUND);
        } else if (text.equals("=")) {
            button.setBackground(EQUALS_BUTTON_BACKGROUND);
            button.setFont(new Font("Segoe UI", Font.BOLD, 22));
            button.setForeground(Color.WHITE);
        } else if (text.equals("←")) {
            button.setBackground(new Color(255, 59, 48));
            button.setFont(new Font("Segoe UI", Font.BOLD, 24));
            button.setForeground(Color.WHITE);
        } else if (text.matches("sin θ|cos θ|tan θ|ln x|log x|eˣ|xʸ")) {
            button.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            button.setBackground(new Color(200, 200, 255));
            button.setForeground(new Color(50, 50, 150));
        } else {
            button.setBackground(BUTTON_BACKGROUND);
            button.setForeground(BUTTON_FOREGROUND);
        }
        
        // 添加鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }
        });

        return button;
    }

    private void styleEqualsButton(JButton button) {
        button.setBackground(new Color(0, 122, 255));
    }

    private JButton createHistoryButton() {
        JButton button = new JButton("Hist");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBackground(SPECIAL_BUTTON_BACKGROUND);
        button.setForeground(BUTTON_FOREGROUND);
        
        // 只添加显示历史记录的动作
        button.addActionListener(e -> showHistory());
        
        // 添加鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }
        });

        return button;
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!display.isFocusable()) {
                display.setFocusable(true);
            }

            String command = e.getActionCommand();

            // 如果显示的是Error，且输入不是退格或清除，则先清空显示
            if (display.getText().equals("Error") || display.getText().startsWith("Error")) {
                if (!command.equals("←") && !command.equals("C")) {
                    display.setText("");
                }
            }

            if (display.getText().equals(placeholder)) {
                display.setText("");
                display.setForeground(Color.BLACK);
            }

            // 退格按钮
            if (command.equals("←")) {
                String currentText = display.getText();
                if (!currentText.isEmpty() && !currentText.equals(placeholder)) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                    if (display.getText().isEmpty()) {
                        display.setText(placeholder);
                        display.setForeground(Color.GRAY);
                    }
                }
            }

            // 清空按钮
            else if (command.equals("C")) {
                display.setText(placeholder);
                display.setForeground(Color.GRAY);
            }

            // 处理四则运算和括号
            else if (command.equals("=")) {
                String result = evaluateExpression(display.getText());
                display.setText(result);
            }

            // 处理高级函数和特殊操作
            else if (isAdvancedFunction(command) || command.equals("1/x")) {
                handleAdvancedFunction(command);
            }

            // 处理数字和运算符，直接加到算式中即可
            else if (!e.getSource().equals(historyButton)) {  // 使用对象比较而不是文本比较
                if (display.getText().equals(placeholder)) {
                    display.setText("");
                    display.setForeground(Color.BLACK);
                }
                display.setText(display.getText() + command);
            }

            // 在每次操作后更新历史记录
            if (!command.equals("=") && !command.equals("C") && !command.equals("←")) {
                updateHistory(display.getText());
            }
        }
    }

    private boolean isAdvancedFunction(String function) {
        return function.matches("sin θ|cos θ|tan θ|ln x|log x|eˣ|xʸ|1/x");
    }

    private void handleAdvancedFunction(String function) {
        try {
            double value = Double.parseDouble(display.getText());
            double result = 0;
            switch (function) {
                case "sin θ": result = Math.sin(Math.toRadians(value)); break;
                case "cos θ": result = Math.cos(Math.toRadians(value)); break;
                case "tan θ": result = Math.tan(Math.toRadians(value)); break;
                case "ln x": result = Math.log(value); break;
                case "log x": result = Math.log10(value); break;
                case "eˣ": result = Math.exp(value); break;
                case "xʸ":
                    String secondValue = JOptionPane.showInputDialog("请输入指数值:");
                    result = Math.pow(value, Double.parseDouble(secondValue));
                    break;
                case "1/x":
                    if (value != 0) {
                        result = 1 / value;
                    } else {
                        display.setText("Error: Division by zero");
                        return;
                    }
                    break;
            }
            display.setText(String.valueOf(result));
        } catch (NumberFormatException ex) {
            display.setText("Error: Invalid input");
        } catch (ArithmeticException ex) {
            display.setText("Error: " + ex.getMessage());
        }
    }

    private String evaluateExpression(String expression) {
        try {
            expression = expression.replace("×", "*").replace("÷", "/");
            Stack<Double> values = new Stack<>();
            Stack<String> ops = new Stack<>();
            String[] tokens = expression.split("(?<=[-+*/()^])|(?=[-+*/()^])");

            for (String token : tokens) {
                if (token.trim().isEmpty()) continue;
                if (isNumber(token)) {
                    values.push(Double.parseDouble(token));
                } else if ("(".equals(token)) {
                    ops.push(token);
                } else if (")".equals(token)) {
                    while (!ops.peek().equals("(")) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.pop();
                } else if (isOperator(token)) {
                    while (!ops.empty() && hasPrecedence(token, ops.peek())) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.push(token);
                }
            }

            while (!ops.empty()) {
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            }

            return String.valueOf(values.pop());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String token) {
        return "+-*/^".contains(token);
    }

    private boolean hasPrecedence(String op1, String op2) {
        if (op2.equals("(") || op2.equals(")")) return false;
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"))) return false;
        if (op1.equals("^") && !op2.equals("^")) return false;
        return true;
    }

    private double applyOp(String op, double b, double a) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
            case "^": return Math.pow(a, b);
        }
        return 0;
    }

    private void updateHistory(String expression) {
        history.add(expression);
        if (history.size() > 10) {  // 只保留最近的10条记录
            history.remove(0);
        }
    }

    private void showHistory() {
        JDialog historyDialog = new JDialog(this, "计算历史", true);
        historyDialog.setLayout(new BorderLayout());
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String item : history) {
            listModel.addElement(item);
        }
        JList<String> historyList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(historyList);
        
        historyDialog.add(scrollPane, BorderLayout.CENTER);
        historyDialog.setSize(300, 400);
        historyDialog.setLocationRelativeTo(this);
        historyDialog.setVisible(true);
    }

    private void openLoanCalculator() {
        SwingUtilities.invokeLater(() -> {
            LoanCalculator loanCalculator = new LoanCalculator();
            loanCalculator.setVisible(true);
        });
    }

    private void setTheme(boolean isDark) {
        isDarkTheme = isDark;
        updateTheme();
        updateThemeMenuItems();
    }

    private void updateThemeMenuItems() {
        lightThemeItem.setEnabled(isDarkTheme);
        darkThemeItem.setEnabled(!isDarkTheme);
    }

    private void updateTheme() {
        Color backgroundColor = isDarkTheme ? DARK_BACKGROUND_COLOR : LIGHT_BACKGROUND_COLOR;
        Color displayBackground = isDarkTheme ? DARK_DISPLAY_BACKGROUND : LIGHT_DISPLAY_BACKGROUND;
        Color buttonBackground = isDarkTheme ? DARK_BUTTON_BACKGROUND : LIGHT_BUTTON_BACKGROUND;
        currentTextColor = isDarkTheme ? DARK_TEXT_COLOR : LIGHT_TEXT_COLOR;
        Color operatorBackground = isDarkTheme ? DARK_OPERATOR_BACKGROUND : LIGHT_OPERATOR_BACKGROUND;
        Color specialButtonBackground = isDarkTheme ? DARK_SPECIAL_BUTTON_BACKGROUND : LIGHT_SPECIAL_BUTTON_BACKGROUND;
        Color equalsButtonBackground = isDarkTheme ? DARK_EQUALS_BUTTON_BACKGROUND : LIGHT_EQUALS_BUTTON_BACKGROUND;

        // 为运算符和等号按钮设置特定的前景色
        Color operatorForeground = isDarkTheme ? DARK_TEXT_COLOR : LIGHT_BACKGROUND_COLOR;
        Color equalsForeground = isDarkTheme ? DARK_TEXT_COLOR : LIGHT_BACKGROUND_COLOR;

        getContentPane().setBackground(backgroundColor);
        display.setBackground(displayBackground);
        
        // 更新输入框文本颜色
        if (display.getText().equals(placeholder)) {
            display.setForeground(isDarkTheme ? DARK_TEXT_COLOR.darker() : LIGHT_TEXT_COLOR.brighter());
        } else {
            display.setForeground(currentTextColor);
        }

        // 更新焦点监听器
        for (FocusListener fl : display.getFocusListeners()) {
            display.removeFocusListener(fl);
        }
        display.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (display.getText().equals(placeholder)) {
                    display.setText("");
                    display.setForeground(currentTextColor);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (display.getText().trim().isEmpty()) {
                    display.setText(placeholder);
                    display.setForeground(isDarkTheme ? DARK_TEXT_COLOR.darker() : LIGHT_TEXT_COLOR.brighter());
                }
            }
        });

        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                for (Component button : ((JPanel) comp).getComponents()) {
                    if (button instanceof JButton) {
                        JButton jButton = (JButton) button;
                        if (jButton.getText().matches("[+\\-×÷]")) {
                            jButton.setBackground(operatorBackground);
                            jButton.setForeground(operatorForeground);
                        } else if (jButton.getText().equals("=")) {
                            jButton.setBackground(equalsButtonBackground);
                            jButton.setForeground(equalsForeground);
                        } else if (jButton.getText().matches("[√%1/x]")) {
                            jButton.setBackground(specialButtonBackground);
                            jButton.setForeground(currentTextColor);
                        } else {
                            jButton.setBackground(buttonBackground);
                            jButton.setForeground(currentTextColor);
                        }
                    }
                }
            }
        }

        // 更新菜单栏的背景颜色，但保持字体颜色不变
        JMenuBar menuBar = getJMenuBar();
        if (menuBar != null) {
            menuBar.setBackground(backgroundColor);
            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                JMenu menu = menuBar.getMenu(i);
                updateMenuColors(menu);
            }
        }

        updateTextColor(); // 确保当前文本颜色正确更新
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void updateMenuColors(JMenu menu) {
        menu.setBackground(isDarkTheme ? DARK_BACKGROUND_COLOR : LIGHT_BACKGROUND_COLOR);
        // 保持菜单字体颜色不变
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setBackground(isDarkTheme ? DARK_BACKGROUND_COLOR : LIGHT_BACKGROUND_COLOR);
                // 保持菜单项字体颜色不变
            }
        }
    }

    private void updateTextColor() {
        if (!display.getText().equals(placeholder)) {
            display.setForeground(currentTextColor);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}