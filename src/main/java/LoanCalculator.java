import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoanCalculator extends JFrame {
    private JTextField loanAmountField;
    private JTextField annualRateField;
    private JTextField loanYearsField;
    private JLabel monthlyPaymentLabel;
    private JLabel totalInterestLabel;
    private JLabel totalPaymentLabel;
    private JRadioButton equalPrincipalInterestButton;
    private JRadioButton equalPrincipalButton;

    public LoanCalculator() {
        setTitle("贷款计算器");
        setLayout(new BorderLayout());
        
        // 设置现代look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 设置字体
        Font labelFont = new Font("Microsoft YaHei", Font.PLAIN, 16);
        Font inputFont = new Font("Microsoft YaHei", Font.PLAIN, 14);

        // 贷款金额
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel loanAmountLabel = new JLabel("贷款金额:");
        loanAmountLabel.setFont(labelFont);
        mainPanel.add(loanAmountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        loanAmountField = new JTextField(15);
        loanAmountField.setFont(inputFont);
        mainPanel.add(loanAmountField, gbc);

        // 年利率
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel annualRateLabel = new JLabel("年利率 (%):");
        annualRateLabel.setFont(labelFont);
        mainPanel.add(annualRateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        annualRateField = new JTextField(15);
        annualRateField.setFont(inputFont);
        mainPanel.add(annualRateField, gbc);

        // 贷款年限
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel loanYearsLabel = new JLabel("贷款年限:");
        loanYearsLabel.setFont(labelFont);
        mainPanel.add(loanYearsLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loanYearsField = new JTextField(15);
        loanYearsField.setFont(inputFont);
        mainPanel.add(loanYearsField, gbc);

        // 还款方式选择
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        ButtonGroup group = new ButtonGroup();
        equalPrincipalInterestButton = new JRadioButton("等额本息");
        equalPrincipalButton = new JRadioButton("等额本金");
        equalPrincipalInterestButton.setFont(labelFont);
        equalPrincipalButton.setFont(labelFont);
        group.add(equalPrincipalInterestButton);
        group.add(equalPrincipalButton);
        JPanel radioPanel = new JPanel();
        radioPanel.add(equalPrincipalInterestButton);
        radioPanel.add(equalPrincipalButton);
        mainPanel.add(radioPanel, gbc);

        // 计算按钮
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton calculateButton = new JButton("计算");
        calculateButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        calculateButton.setBackground(new Color(70, 130, 180));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        calculateButton.addActionListener(new CalculateButtonListener());
        mainPanel.add(calculateButton, gbc);

        // 结果标签
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        monthlyPaymentLabel = new JLabel("月均还款: ");
        monthlyPaymentLabel.setFont(labelFont);
        mainPanel.add(monthlyPaymentLabel, gbc);

        gbc.gridy = 6;
        totalInterestLabel = new JLabel("利息总额: ");
        totalInterestLabel.setFont(labelFont);
        mainPanel.add(totalInterestLabel, gbc);

        gbc.gridy = 7;
        totalPaymentLabel = new JLabel("还款总额: ");
        totalPaymentLabel.setFont(labelFont);
        mainPanel.add(totalPaymentLabel, gbc);

        // 将主面板添加到框架
        add(mainPanel, BorderLayout.CENTER);

        // 添加页脚
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(70, 130, 180));
        JLabel footerLabel = new JLabel("© 2024 贷款计算器 by 卜炜珏");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        setSize(450, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);  // 居中显示
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double loanAmount = Double.parseDouble(loanAmountField.getText());
                double annualRate = Double.parseDouble(annualRateField.getText()) / 100;
                int loanYears = Integer.parseInt(loanYearsField.getText());

                double monthlyRate = annualRate / 12;
                int months = loanYears * 12;

                double monthlyPayment, totalInterest, totalPayment;

                if (equalPrincipalInterestButton.isSelected()) {
                    // 等额本息
                    monthlyPayment = loanAmount * (monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1);
                    totalPayment = monthlyPayment * months;
                    totalInterest = totalPayment - loanAmount;

                    monthlyPaymentLabel.setText(String.format("月均还款: %.2f", monthlyPayment));
                } else if (equalPrincipalButton.isSelected()) {
                    // 等额本金
                    double monthlyPrincipal = loanAmount / months;
                    double firstMonthPayment = monthlyPrincipal + loanAmount * monthlyRate;
                    double lastMonthPayment = monthlyPrincipal + monthlyPrincipal * monthlyRate;

                    totalPayment = (firstMonthPayment + lastMonthPayment) * months / 2;
                    totalInterest = totalPayment - loanAmount;

                    monthlyPaymentLabel.setText(String.format("首月还款: %.2f, 末月还款: %.2f", firstMonthPayment, lastMonthPayment));
                } else {
                    JOptionPane.showMessageDialog(LoanCalculator.this, "请选择还款方式", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                totalInterestLabel.setText(String.format("利息总额: %.2f", totalInterest));
                totalPaymentLabel.setText(String.format("还款总额: %.2f", totalPayment));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LoanCalculator.this, "输入错误，请检查输入值", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoanCalculator().setVisible(true);
        });
    }
}
