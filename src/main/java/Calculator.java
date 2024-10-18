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
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置窗口标题和布局
        setTitle("My Calculator");
        setLayout(new BorderLayout());

        // 创建显示框
        display = new JTextField();
        display.setEditable(true); // 允许用户直接在文本框中输入
        display.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        display.setForeground(new Color(80, 80, 80));
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
            // 获得焦点时，清楚提示文字
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
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "(", "0", ")", "+",
                "√", "%", "1/x", "Loan"
        };

        int index = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 4; col++) {
                JButton button = createStyledButton(buttons[index]);
                if (buttons[index].matches("[+\\-*/]")) {
                    styleOperatorButton(button);
                } else if (buttons[index].matches("[√%1/xLoan]")) {
                    styleSpecialButton(button);
                }
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

        // 修改特殊按钮样式
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

        add(panel, BorderLayout.CENTER);
        setSize(350, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 添加退格按钮
        gbc.gridx = 0;
        gbc.gridy = 6; 
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        panel.add(backspaceButton, gbc);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setBackground(BUTTON_BACKGROUND);
        button.setForeground(BUTTON_FOREGROUND);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addActionListener(new ButtonClickListener());
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_BACKGROUND);
            }
        });

        return button;
    }

    private void styleOperatorButton(JButton button) {
        button.setBackground(OPERATOR_BACKGROUND);
    }

    private void styleSpecialButton(JButton button) {
        button.setBackground(SPECIAL_BUTTON_BACKGROUND);
    }

    private void styleEqualsButton(JButton button) {
        button.setBackground(new Color(0, 122, 255));
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            // 清除提示文字
            if (display.getText().equals(placeholder)) {
                display.setText("");
                display.setForeground(Color.BLACK);
            }

            // 退格按钮
            if (command.equals("Backspace")) {
                String currentText = display.getText();
                if (!currentText.equals(placeholder) && currentText.length() > 0) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                }
            }

            // 清空按钮
            else if (command.equals("C")) {
                display.setText("");
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
                // 直接将 % 添加到显示内容中，作为一个运算符
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

            // 处理贷款计算 Loan
            else if (command.equals("Loan")) {
                LoanCalculator loanCalculator = new LoanCalculator();
                loanCalculator.setVisible(true);
            }

            // 处理数字和运算符
            else {
                display.setText(display.getText() + command);
            }
        }
    }


    // 解析和计算表达式的方法
    private String evaluateExpression(String expression) {
        try {
            // 将表达式转换为后缀表达式
            String postfix = infixToPostfix(expression);

            // 计算后缀表达式的结果
            double result = evaluatePostfix(postfix);

            return String.valueOf(result);
        } catch (ArithmeticException e) {
            return "Math Error: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return "Invalid Expression";
        } catch (Exception e) {
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
                        stack.pop();
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

        while (!stack.isEmpty()) {
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
        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else if (token.length() == 1 && isOperator(token.charAt(0))) {
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
                        if (b == 0) throw new ArithmeticException("Division by zero");
                        stack.push(a / b);
                        break;
                    case '%':
                        if (b == 0) throw new ArithmeticException("Modulo by zero");
                        stack.push(a % b);
                        break;
                }
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
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
        Calculator calculator = new Calculator();
        calculator.setVisible(true);
    }
}
