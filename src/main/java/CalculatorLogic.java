import java.util.Stack;

import javax.swing.JOptionPane;

/**
 * 计算器逻辑类，处理数学表达式的计算
 */
public class CalculatorLogic {
    /**
     * 评估并计算数学表达式
     * @param expression 要计算的数学表达式
     * @return 计算结果的字符串表示
     */
    public String evaluateExpression(String expression) {
        try {
            // 将乘除符号替换为程序可识别的符号
            expression = expression.replace("×", "*").replace("÷", "/");
            Stack<Double> values = new Stack<>();
            Stack<String> ops = new Stack<>();
            StringBuilder numberBuilder = new StringBuilder();
            boolean lastWasOperator = true;

            // 遍历表达式的每个字符
            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);
                if (Character.isDigit(c) || c == '.') {
                    // 如果是数字或小数点，添加到numberBuilder
                    numberBuilder.append(c);
                    lastWasOperator = false;
                } else {
                    // 如果不是数字，处理之前累积的数字
                    if (numberBuilder.length() > 0) {
                        values.push(Double.parseDouble(numberBuilder.toString()));
                        numberBuilder.setLength(0);
                    }
                    if (c == '-' && lastWasOperator) {
                        // 处理负数
                        numberBuilder.append(c);
                    } else if ("+-*/%^()".indexOf(c) != -1) {
                        // 处理运算符和括号
                        if (c == '(') {
                            ops.push(String.valueOf(c));
                        } else if (c == ')') {
                            // 处理右括号，计算括号内的表达式
                            while (!ops.peek().equals("(")) {
                                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                            }
                            ops.pop();
                        } else {
                            // 处理其他运算符
                            while (!ops.empty() && hasPrecedence(String.valueOf(c), ops.peek())) {
                                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                            }
                            ops.push(String.valueOf(c));
                        }
                        lastWasOperator = true;
                    }
                }
            }

            // 处理最后剩余的数字
            if (numberBuilder.length() > 0) {
                values.push(Double.parseDouble(numberBuilder.toString()));
            }

            // 计算剩余的运算
            while (!ops.empty()) {
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            }

            // 获取最终结果
            double result = values.pop();
            if (Double.isInfinite(result) || Double.isNaN(result)) {
                return "Error, division by zero";
            }
            
            return formatResult(result);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 格式化计算结果
     * @param result 计算结果
     * @return 格式化后的结果字符串
     */
    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.valueOf(result);
        }
    }

    /**
     * 判断运算符优先级
     * @param op1 运算符1
     * @param op2 运算符2
     * @return 如果op1优先级低于或等于op2，返回true；否则返回false
     */
    private boolean hasPrecedence(String op1, String op2) {
        if (op2.equals("(") || op2.equals(")")) return false;
        if ((op1.equals("*") || op1.equals("/") || op1.equals("%")) && 
            (op2.equals("+") || op2.equals("-"))) return false;
        if (op1.equals("^") && !op2.equals("^")) return false;
        return true;
    }

    /**
     * 执行运算
     * @param op 运算符
     * @param b 第二个操作数
     * @param a 第一个操作数
     * @return 运算结果
     */
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

    /**
     * 判断是否为高级函数
     * @param function 函数名
     * @return 如果是高级函数返回true，否则返回false
     */
    public boolean isAdvancedFunction(String function) {
        return function.matches("sin θ|cos θ|tan θ|ln x|lg x|eˣ|xʸ|1/x");
    }

    /**
     * 应用高级函数
     * @param function 函数名
     * @param value 函数参数
     * @return 计算结果的字符串表示
     */
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
            default: throw new IllegalArgumentException("Error: Unknown function: " + function);
        }
        return formatResult(result);
    }
}
