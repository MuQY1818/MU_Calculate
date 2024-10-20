import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class HistoryManager {
    private ArrayList<String> history;

    public HistoryManager() {
        history = new ArrayList<>();
    }

    public void addToHistory(String expression) {
        history.add(expression);
        if (history.size() > 10) {
            history.remove(0);
        }
    }

    public void showHistory(JFrame parent) {
        JDialog historyDialog = new JDialog(parent, "计算历史", true);
        historyDialog.setLayout(new BorderLayout());
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String item : history) {
            listModel.addElement(item);
        }
        JList<String> historyList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(historyList);
        
        historyDialog.add(scrollPane, BorderLayout.CENTER);
        historyDialog.setSize(300, 400);
        historyDialog.setLocationRelativeTo(parent);
        historyDialog.setVisible(true);
    }
}