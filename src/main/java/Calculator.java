import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class Calculator extends JFrame {
    private JTextField display; // 显示输入和结果的文本框
    private final String placeholder = "Enter calculation here..."; // 提示词
    private static final Color BUTTON_BACKGROUND = new Color(240, 240, 240);
    private static final Color BUTTON_FOREGROUND = new Color(50, 50, 50);
    private static final Color OPERATOR_BACKGROUND = new Color(252, 152, 3);
    private static final Color SPECIAL_BUTTON_BACKGROUND = new Color(220, 220, 220);

    public Calculator() {
        // 设置系统外观
        try {
            // 设置现代Look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置窗口标题和布局管理器
        setTitle("My Calculator");
        setLayout(new BorderLayout());

        // 创建显示框
        display = new JTextField();
        display.setEditable(true); // 允许用户直接在文本框中输入
        display.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        display.setForeground(Color.GRAY);  // 初始颜色设为灰色
        display.setBackground(new Color(250, 250, 250));
        display.setHorizontalAlignment(JTextField.RIGHT); // 设置文本右对齐
        display.setText(placeholder); // 初始时显示提示词
        // 设置边框样式
        display.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
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
                "√", "%", "1/x", "Loan"
        };

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

        // 修改按钮样式
        JButton decimalButton = createStyledButton(".");
        JButton equalsButton = createStyledButton("=");
        styleEqualsButton(equalsButton);
        JButton clearButton = createStyledButton("C");
        JButton backspaceButton = createStyledButton("Backspace");

        // 添加等号按钮
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(equalsButton, gbc);

        // 添加小数点按钮
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(decimalButton, gbc);

        // 添加清空按钮
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(clearButton, gbc);

        // 添加退格按钮
        gbc.gridx = 0;
        gbc.gridy = 6; 
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        panel.add(backspaceButton, gbc);

        add(panel, BorderLayout.CENTER);
        setSize(350, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 使用 SwingUtilities.invokeLater 来设置初始状态
        SwingUtilities.invokeLater(this::setInitialState);
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
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addActionListener(new ButtonClickListener());
        
        // 根据按钮类型设置初始背景色和前景色
        if (text.matches("[+\\-×÷]")) {
            button.setBackground(OPERATOR_BACKGROUND);
            button.setFont(new Font("Segoe UI", Font.BOLD, 18));
            button.setForeground(Color.WHITE);
        } else if (text.matches("[√%1/xLoan]")) {
            button.setBackground(SPECIAL_BUTTON_BACKGROUND);
        } else if (text.equals("=")) {
            button.setBackground(new Color(0, 122, 255));
            button.setFont(new Font("Segoe UI", Font.BOLD, 18));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(BUTTON_BACKGROUND);
            button.setForeground(BUTTON_FOREGROUND);
        }
        
        button.addMouseListener(new MouseAdapter() {
            private Color originalBackground;
            private Color originalForeground;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                originalBackground = button.getBackground();
                originalForeground = button.getForeground();
                button.setBackground(button.getBackground().darker());
                // 如果原来是白色字体，鼠标悬停时保持白色
                if (!originalForeground.equals(BUTTON_FOREGROUND)) {
                    button.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBackground);
                button.setForeground(originalForeground);
            }
        });

        return button;
    }

    private void styleEqualsButton(JButton button) {
        button.setBackground(new Color(0, 122, 255));
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!display.isFocusable()) {
                display.setFocusable(true);
            }

            String command = e.getActionCommand();

            if (display.getText().equals(placeholder)) {
                display.setText("");
                display.setForeground(Color.BLACK);
            }

            // 退格按钮
            if (command.equals("Backspace")) {
                String currentText = display.getText();
                if (!currentText.isEmpty()) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                }
                if (display.getText().isEmpty()) {
                    display.setText(placeholder);
                    display.setForeground(Color.GRAY);
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

            // 拓展功能
            // 处理平方根 √
            else if (command.equals("√")) {
                try {
                    double value = Double.parseDouble(display.getText());
                    display.setText(String.valueOf(Math.sqrt(value)));  // 计算平方根
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                }
            }

            // 处理模运算 %
            else if (command.equals("%")) {
                // 直接将 % 添加到算式中，作为一个运算符
                display.setText(display.getText() + " % ");
            }

            // 处理倒数 1/x
            else if (command.equals("1/x")) {
                try {
                    double value = Double.parseDouble(display.getText());
                    if (value != 0) {
                        display.setText(String.valueOf(1 / value));  // 计算倒数
                    } else {
                        display.setText("Error");
                    }
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                }
            }

            // 打开贷款计算器
            else if (command.equals("Loan")) {
                LoanCalculator loanCalculator = new LoanCalculator();
                loanCalculator.setVisible(true);
            }

            // 处理数字和运算符，直接加到算式中即可
            else {
                if (display.getText().equals(placeholder)) {
                    display.setText("");
                    display.setForeground(Color.BLACK);
                }
                display.setText(display.getText() + command);
            }
        }
    }


    // 计算表达式
    private String evaluateExpression(String expression) {
        try {
            // 将显示用的符号转换为计算用的符号
            expression = expression.replace("×", "*").replace("÷", "/");

            // 将表达式转换为后缀表达式
            String postfix = infixToPostfix(expression);

            // 计算后缀表达式的结果
            double result = evaluatePostfix(postfix);

            return String.valueOf(result);
        } catch (ArithmeticException e) { // 除零错误
            return "Math Error: " + e.getMessage();
        } catch (IllegalArgumentException e) { // 表达式错误
            return "Invalid Expression";
        } catch (Exception e) { // 其他错误
            return "Error";
        }
    }

    // 将中缀表达式转换为后缀表达式
    private String infixToPostfix(String expression) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        StringBuilder number = new StringBuilder();
        boolean expectOperand = true;  // 用于区分一元负号和减法运算符

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            // 如果当前字符是数字或小数点，则将其添加到数字中
            if (Character.isDigit(c) || c == '.') {
                number.append(c);
                expectOperand = false; 
            } else if (c == '-' && expectOperand) {
                // 处理负数
                number.append(c);
            } else {
                if (number.length() > 0) {
                    postfix.append(number).append(" ");
                    number.setLength(0);
                }
                if (c == '(') {
                    stack.push(c);
                    expectOperand = true;
                } else if (c == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        postfix.append(stack.pop()).append(" ");
                    }
                    if (!stack.isEmpty() && stack.peek() == '(') {
                        stack.pop(); // 弹出左括号
                    }
                    expectOperand = false;
                } else if (isOperator(c)) {
                    while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                        postfix.append(stack.pop()).append(" ");
                    }
                    stack.push(c);
                    expectOperand = true;
                }
            }
        }

        if (number.length() > 0) {
            postfix.append(number).append(" ");
        }

        while (!stack.isEmpty()) { // 将栈中剩余的运算符添加到后缀表达式中
            if (stack.peek() == '(') {
                return "Invalid Expression";
            }
            postfix.append(stack.pop()).append(" ");
        }

        return postfix.toString().trim();
    }

    // 计算后缀表达式的值
    private double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        // 将后缀表达式分割为单个元素
        String[] tokens = postfix.split("\\s+"); // 以空格或者多个空格为分隔符

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) { // 匹配数字
                stack.push(Double.parseDouble(token));
            } else if (token.length() == 1 && isOperator(token.charAt(0))) { // 匹配运算符
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                    case '*': stack.push(a * b); break;
                    case '/':
                        if (b == 0) throw new ArithmeticException("Division by zero"); // 除零错误
                        stack.push(a / b);
                        break;
                    case '%':
                        if (b == 0) throw new ArithmeticException("Modulo by zero"); // 除零错误
                        stack.push(a % b);
                        break;
                }
            } else {
                throw new IllegalArgumentException("Invalid token: " + token); // 表达式错误
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        return stack.pop();
    }

    // 检查是否为运算符
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }

    // 确定运算符优先级
    private int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            default:
                return -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}
