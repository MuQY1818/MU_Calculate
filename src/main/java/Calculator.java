import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * 计算器主类，继承自JFrame
 */
public class Calculator extends JFrame {
    private CalculatorUI ui;           // 计算器用户界面
    private CalculatorLogic logic;     // 计算器逻辑
    private ThemeManager themeManager; // 主题管理器
    private HistoryManager historyManager; // 历史记录管理器

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

        initComponents();
        setSize(400, 600);
        themeManager.updateTheme(); // 初始化主题
    }

    /**
     * 初始化界面组件
     */
    private void initComponents() {
        add(ui.getDisplayPanel(), BorderLayout.NORTH);
        add(ui.getButtonPanel(), BorderLayout.CENTER);
        setJMenuBar(ui.createMenuBar());
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}
