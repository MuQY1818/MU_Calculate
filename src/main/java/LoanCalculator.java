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

    public LoanCalculator() {
        setTitle("贷款计算器");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 贷款金额
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("贷款金额:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        loanAmountField = new JTextField(10);
        add(loanAmountField, gbc);

        // 年利率
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("年利率 (%):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        annualRateField = new JTextField(10);
        add(annualRateField, gbc);

        // 贷款年限
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("贷款年限:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loanYearsField = new JTextField(10);
        add(loanYearsField, gbc);

        // 计算按钮
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton calculateButton = new JButton("计算");
        calculateButton.addActionListener(new CalculateButtonListener());
        add(calculateButton, gbc);

        // 结果标签
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        monthlyPaymentLabel = new JLabel("月均还款: ");
        add(monthlyPaymentLabel, gbc);

        gbc.gridy = 5;
        totalInterestLabel = new JLabel("利息总额: ");
        add(totalInterestLabel, gbc);

        gbc.gridy = 6;
        totalPaymentLabel = new JLabel("还款总额: ");
        add(totalPaymentLabel, gbc);

        setSize(300, 300);
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
                double monthlyPayment = loanAmount * (monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1);
                double totalPayment = monthlyPayment * months;
                double totalInterest = totalPayment - loanAmount;

                monthlyPaymentLabel.setText(String.format("月均还款: %.2f", monthlyPayment));
                totalInterestLabel.setText(String.format("利息总额: %.2f", totalInterest));
                totalPaymentLabel.setText(String.format("还款总额: %.2f", totalPayment));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LoanCalculator.this, "输入错误，请检查输入值", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
