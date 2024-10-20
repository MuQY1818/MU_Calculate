import java.util.Stack;

import javax.swing.JOptionPane;

public class CalculatorLogic {
    public String evaluateExpression(String expression) {
        try {
            expression = expression.replace("×", "*").replace("÷", "/");
            Stack<Double> values = new Stack<>();
            Stack<String> ops = new Stack<>();
            StringBuilder numberBuilder = new StringBuilder();
            boolean lastWasOperator = true;

            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);
                if (Character.isDigit(c) || c == '.') {
                    numberBuilder.append(c);
                    lastWasOperator = false;
                } else {
                    if (numberBuilder.length() > 0) {
                        values.push(Double.parseDouble(numberBuilder.toString()));
                        numberBuilder.setLength(0);
                    }
                    if (c == '-' && lastWasOperator) {
                        numberBuilder.append(c);
                    } else if ("+-*/%^()".indexOf(c) != -1) {
                        if (c == '(') {
                            ops.push(String.valueOf(c));
                        } else if (c == ')') {
                            while (!ops.peek().equals("(")) {
                                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                            }
                            ops.pop();
                        } else {
                            while (!ops.empty() && hasPrecedence(String.valueOf(c), ops.peek())) {
                                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                            }
                            ops.push(String.valueOf(c));
                        }
                        lastWasOperator = true;
                    }
                }
            }

            if (numberBuilder.length() > 0) {
                values.push(Double.parseDouble(numberBuilder.toString()));
            }

            while (!ops.empty()) {
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            }

            double result = values.pop();
            if (Double.isInfinite(result) || Double.isNaN(result)) {
                return "Error: Division by zero";
            }
            
            return formatResult(result);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.valueOf(result);
        }
    }

    private boolean hasPrecedence(String op1, String op2) {
        if (op2.equals("(") || op2.equals(")")) return false;
        if ((op1.equals("*") || op1.equals("/") || op1.equals("%")) && 
            (op2.equals("+") || op2.equals("-"))) return false;
        if (op1.equals("^") && !op2.equals("^")) return false;
        return true;
    }

    private double applyOp(String op, double b, double a) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            case "%":
                if (b == 0) throw new ArithmeticException("Modulo by zero");
                return a % b;
            case "^": return Math.pow(a, b);
        }
        return 0;
    }

    public boolean isAdvancedFunction(String function) {
        return function.matches("sin θ|cos θ|tan θ|ln x|lg x|eˣ|xʸ|1/x");
    }

    public String applyAdvancedFunction(String function, double value) {
        double result;
        switch (function) {
            case "sin θ": result = Math.sin(Math.toRadians(value)); break;
            case "cos θ": result = Math.cos(Math.toRadians(value)); break;
            case "tan θ": result = Math.tan(Math.toRadians(value)); break;
            case "ln x": result = Math.log(value); break;
            case "lg x": result = Math.log10(value); break;
            case "eˣ": result = Math.exp(value); break;
            case "xʸ":
                String secondValue = JOptionPane.showInputDialog("请输入指数值:");
                result = Math.pow(value, Double.parseDouble(secondValue));
                break;
            case "1/x":
                if (value == 0) throw new ArithmeticException("Division by zero");
                result = 1 / value;
                break;
            default: throw new IllegalArgumentException("Unknown function: " + function);
        }
        return formatResult(result);
    }
}
