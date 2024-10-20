import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Calculator extends JFrame {
    private CalculatorUI ui;
    private CalculatorLogic logic;
    private ThemeManager themeManager;
    private HistoryManager historyManager;

    public Calculator() {
        setTitle("简单计算器");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 添加图标
        ImageIcon icon = new ImageIcon(getClass().getResource("/Calculator_31111.png"));
        setIconImage(icon.getImage());

        logic = new CalculatorLogic();
        historyManager = new HistoryManager();
        themeManager = new ThemeManager(this);
        ui = new CalculatorUI(this, logic, historyManager, themeManager);

        initComponents();
        setSize(400, 600);
        themeManager.updateTheme(); // 初始化主题
    }

    private void initComponents() {
        add(ui.getDisplayPanel(), BorderLayout.NORTH);
        add(ui.getButtonPanel(), BorderLayout.CENTER);
        setJMenuBar(ui.createMenuBar());
    }

    public void updateUI() {
        SwingUtilities.updateComponentTreeUI(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}
