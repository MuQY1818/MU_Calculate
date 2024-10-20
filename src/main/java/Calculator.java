import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 计算器主类，继承自JFrame
 */
public class Calculator extends JFrame {
    private final CalculatorUI ui;           // 计算器用户界面
    private final CalculatorLogic logic;     // 计算器逻辑
    private final ThemeManager themeManager; // 主题管理器
    private final HistoryManager historyManager; // 历史记录管理器

    /**
     * 构造函数，初始化计算器
     */
    public Calculator() {
        setTitle("简单计算器");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 添加图标
        ImageIcon icon = new ImageIcon(getClass().getResource("/Calculator_31111.png"));
        setIconImage(icon.getImage());

        // 初始化各个组件
        logic = new CalculatorLogic();
        historyManager = new HistoryManager();
        themeManager = new ThemeManager(this);
        ui = new CalculatorUI(this, logic, historyManager, themeManager);

        add(ui.getDisplayPanel(), BorderLayout.NORTH);
        add(ui.getButtonPanel(), BorderLayout.CENTER);
        setJMenuBar(ui.createMenuBar());

        setSize(400, 600);
        themeManager.updateTheme(); // 初始化主题
    }

    /**
     * 更新UI
     */
    public void updateUI() {
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * 主方法，程序入口
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置Nimbus外观
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                try {
                    // 如果Nimbus不可用，尝试使用系统默认外观
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    // 如果设置外观失败，记录错误但继续执行
                    System.err.println("无法设置外观: " + ex.getMessage());
                }
            }
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}
