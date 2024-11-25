import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class Task {
    private int taskId;
    private String title;
    private String description;
    private String assignee;
    private String priority;
    private String dueDate;
    private String status;
    private int progress;

    public Task(int taskId, String title, String description, String assignee, String priority, String dueDate) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = "Not Started";
        this.progress = 0;
    }

    public int getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAssignee() { return assignee; }
    public String getPriority() { return priority; }
    public String getDueDate() { return dueDate; }
    public String getStatus() { return status; }
    public int getProgress() { return progress; }

    public void updateStatus(String newStatus) { this.status = newStatus; }
    public void updateProgress(int progress) { this.progress = progress; }
}

public class TaskDelegationSystemGUI {
    private JFrame frame;
    private DefaultTableModel taskTableModel;
    private Map<Integer, Task> tasks;
    private Map<String, String> users;
    private int taskCounter;

    public TaskDelegationSystemGUI() {
        tasks = new HashMap<>();
        users = new HashMap<>();
        taskCounter = 1;

        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Task Delegation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // User Management Tab
        JPanel userPanel = createUserManagementTab();
        tabbedPane.addTab("Manage Users", userPanel);

        // Task Management Tab
        JPanel taskPanel = createTaskManagementTab();
        tabbedPane.addTab("Manage Tasks", taskPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createUserManagementTab() {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // User Form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField userNameField = new JTextField();
        JTextField userRoleField = new JTextField();

        formPanel.add(new JLabel("User Name:"));
        formPanel.add(userNameField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(userRoleField);

        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> {
            String name = userNameField.getText().trim();
            String role = userRoleField.getText().trim();
            if (!name.isEmpty() && !role.isEmpty()) {
                users.put(name, role);
                JOptionPane.showMessageDialog(frame, "User '" + name + "' added successfully!");
                userNameField.setText("");
                userRoleField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill out all fields.");
            }
        });

        panel.add(formPanel);
        panel.add(addUserButton);

        return panel;
    }

    private JPanel createTaskManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Task Table
        String[] columns = {"Task ID", "Title", "Assignee", "Priority", "Due Date", "Status", "Progress"};
        taskTableModel = new DefaultTableModel(columns, 0);
        JTable taskTable = new JTable(taskTableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);

        // Task Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        JTextField taskTitleField = new JTextField();
        JTextField taskDescriptionField = new JTextField();
        JTextField taskAssigneeField = new JTextField();
        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        JTextField dueDateField = new JTextField();

        formPanel.add(new JLabel("Title:"));
        formPanel.add(taskTitleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(taskDescriptionField);
        formPanel.add(new JLabel("Assignee:"));
        formPanel.add(taskAssigneeField);
        formPanel.add(new JLabel("Priority:"));
        formPanel.add(priorityComboBox);
        formPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        formPanel.add(dueDateField);

        // Create Task Button
        JPanel buttonPanel = new JPanel();
        JButton createTaskButton = new JButton("Create Task");
        createTaskButton.addActionListener(e -> {
            String title = taskTitleField.getText().trim();
            String description = taskDescriptionField.getText().trim();
            String assignee = taskAssigneeField.getText().trim();
            String priority = (String) priorityComboBox.getSelectedItem();
            String dueDate = dueDateField.getText().trim();

            if (users.containsKey(assignee)) {
                Task task = new Task(taskCounter, title, description, assignee, priority, dueDate);
                tasks.put(taskCounter, task);
                taskTableModel.addRow(new Object[]{taskCounter, title, assignee, priority, dueDate, "Not Started", "0%"});
                taskCounter++;
                JOptionPane.showMessageDialog(frame, "Task '" + title + "' created successfully!");
                taskTitleField.setText("");
                taskDescriptionField.setText("");
                taskAssigneeField.setText("");
                dueDateField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Assignee does not exist.");
            }
        });

        // Mark Task as Completed Button
        JButton markCompletedButton = new JButton("Mark as Completed");
        markCompletedButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow >= 0) {
                int taskId = (int) taskTable.getValueAt(selectedRow, 0);
                Task task = tasks.get(taskId);
                task.updateStatus("Completed");
                task.updateProgress(100);
                taskTable.setValueAt("Completed", selectedRow, 5);
                taskTable.setValueAt("100%", selectedRow, 6);
                JOptionPane.showMessageDialog(frame, "Task marked as completed.");
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a task to mark as completed.");
            }
        });

        // Add Components
        buttonPanel.add(createTaskButton);
        buttonPanel.add(markCompletedButton);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskDelegationSystemGUI::new);
    }
}
