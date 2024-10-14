import javax.swing.*;
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
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);  // 增加组件间距

        // 设置更大的字体
        Font largerFont = new Font("SimSun", Font.PLAIN, 30);

        // 贷款金额
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel loanAmountLabel = new JLabel("贷款金额:");
        loanAmountLabel.setFont(largerFont);
        add(loanAmountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        loanAmountField = new JTextField(15);
        loanAmountField.setFont(largerFont);
        add(loanAmountField, gbc);

        // 年利率
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel annualRateLabel = new JLabel("年利率 (%):");
        annualRateLabel.setFont(largerFont);
        add(annualRateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        annualRateField = new JTextField(15);
        annualRateField.setFont(largerFont);
        add(annualRateField, gbc);

        // 贷款年限
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel loanYearsLabel = new JLabel("贷款年限:");
        loanYearsLabel.setFont(largerFont);
        add(loanYearsLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loanYearsField = new JTextField(15);
        loanYearsField.setFont(largerFont);
        add(loanYearsField, gbc);

        // 还款方式选择
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        ButtonGroup group = new ButtonGroup();
        equalPrincipalInterestButton = new JRadioButton("等额本息");
        equalPrincipalButton = new JRadioButton("等额本金");
        equalPrincipalInterestButton.setFont(largerFont);
        equalPrincipalButton.setFont(largerFont);
        group.add(equalPrincipalInterestButton);
        group.add(equalPrincipalButton);
        JPanel radioPanel = new JPanel();
        radioPanel.add(equalPrincipalInterestButton);
        radioPanel.add(equalPrincipalButton);
        add(radioPanel, gbc);

        // 计算按钮
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton calculateButton = new JButton("计算");
        calculateButton.setFont(largerFont);
        calculateButton.addActionListener(new CalculateButtonListener());
        add(calculateButton, gbc);

        // 结果标签
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        monthlyPaymentLabel = new JLabel("月均还款: ");
        monthlyPaymentLabel.setFont(largerFont);
        add(monthlyPaymentLabel, gbc);

        gbc.gridy = 6;
        totalInterestLabel = new JLabel("利息总额: ");
        totalInterestLabel.setFont(largerFont);
        add(totalInterestLabel, gbc);

        gbc.gridy = 7;
        totalPaymentLabel = new JLabel("还款总额: ");
        totalPaymentLabel.setFont(largerFont);
        add(totalPaymentLabel, gbc);

        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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