
package com.example.ui;

import com.example.model.Exam;
import com.example.model.Question;
import com.example.model.Result;

import com.example.service.ExamService;
import com.example.service.QuestionService;
import com.example.service.ResultService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final ExamService examService = new ExamService();
    private final QuestionService questionService =
            new QuestionService();
    private final ResultService resultService =
            new ResultService();

    private JTable examTable;
    private JTable questionTable;
    private JTable resultTable;

    private JComboBox<Exam> examCombo;

    public AdminDashboard() {

        setTitle("Online Examination System - Admin Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createDashboard();

        loadExams();
        loadQuestionExams();
        loadResults();

        setVisible(true);
    }

    // ==========================================
    // MAIN DASHBOARD
    // ==========================================

    private void createDashboard() {

        setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(new Color(35, 55, 90));

        header.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel title =
                new JLabel("Administrator Dashboard");

        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 23));

        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        header.add(title, BorderLayout.WEST);
        header.add(logoutButton, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Manage Exams", createExamPanel());
        tabs.addTab("Add / View Questions", createQuestionPanel());
        tabs.addTab("Student Results", createResultPanel());

        add(tabs, BorderLayout.CENTER);
    }

    // ==========================================
    // MANAGE EXAMS
    // ==========================================

    private JPanel createExamPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        examTable = new JTable();

        JScrollPane scrollPane =
                new JScrollPane(examTable);

        JButton addButton = new JButton("Create Exam");
        JButton deleteButton = new JButton("Delete Exam");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> createExam());
        deleteButton.addActionListener(e -> deleteExam());

        refreshButton.addActionListener(e -> {
            loadExams();
            loadQuestionExams();
        });

        JPanel buttons = new JPanel();

        buttons.add(addButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // CREATE EXAM
    private void createExam() {

        JTextField titleField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField marksField = new JTextField();

        Object[] fields = {
                "Exam Title:", titleField,
                "Duration (minutes):", durationField,
                "Total Marks:", marksField
        };

        int choice = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Create New Exam",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            String title = titleField.getText().trim();

            int duration = Integer.parseInt(
                    durationField.getText().trim()
            );

            int marks = Integer.parseInt(
                    marksField.getText().trim()
            );

            if (title.isEmpty() || duration <= 0 || marks <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Enter valid exam details."
                );

                return;
            }

            boolean created = examService.createExam(
                    title,
                    duration,
                    marks
            );

            if (created) {

                JOptionPane.showMessageDialog(
                        this,
                        "Exam created successfully!"
                );

                loadExams();
                loadQuestionExams();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to create exam."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Duration and marks must be valid numbers."
            );
        }
    }

    // DELETE EXAM
    private void deleteExam() {

        int row = examTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an exam."
            );

            return;
        }

        int modelRow = examTable.convertRowIndexToModel(row);

        int examId = (int)
                examTable.getModel().getValueAt(modelRow, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this exam?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            boolean deleted = examService.deleteExam(examId);

            if (deleted) {

                JOptionPane.showMessageDialog(
                        this,
                        "Exam deleted successfully."
                );

                loadExams();
                loadQuestionExams();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to delete exam. "
                                + "It may have related questions or results."
                );
            }
        }
    }

    // LOAD EXAMS
    private void loadExams() {

        List<Exam> exams = examService.getAllExams();

        String[] columns = {
                "Exam ID",
                "Title",
                "Duration (Minutes)",
                "Total Marks"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row, int column) {
                        return false;
                    }
                };

        if (exams != null) {

            for (Exam exam : exams) {

                model.addRow(new Object[]{
                        exam.getExamId(),
                        exam.getTitle(),
                        exam.getDurationMinutes(),
                        exam.getTotalMarks()
                });
            }
        }

        examTable.setModel(model);
    }

    // ==========================================
    // ADD / VIEW / EDIT / DELETE QUESTIONS
    // ==========================================

    private JPanel createQuestionPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
        );

        JLabel examLabel = new JLabel("Select Exam:");

        examCombo = new JComboBox<>();

        examCombo.setPreferredSize(new Dimension(230, 30));

        examCombo.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                if (value instanceof Exam) {

                    Exam exam = (Exam) value;

                    setText(
                            exam.getExamId() + " - " + exam.getTitle()
                    );
                }

                return this;
            }
        });

        JButton refreshButton =
                new JButton("Refresh Questions");

        JButton addQuestionButton =
                new JButton("Add MCQ Question");

        JButton editQuestionButton =
                new JButton("Edit Selected");

        JButton deleteQuestionButton =
                new JButton("Delete Selected");

        topPanel.add(examLabel);
        topPanel.add(examCombo);
        topPanel.add(refreshButton);
        topPanel.add(addQuestionButton);
        topPanel.add(editQuestionButton);
        topPanel.add(deleteQuestionButton);

        questionTable = new JTable();

        questionTable.setRowHeight(28);
        questionTable.setAutoCreateRowSorter(true);

        questionTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(questionTable);

        examCombo.addActionListener(e -> loadQuestions());

        refreshButton.addActionListener(e -> {
            loadQuestionExams();
            loadQuestions();
        });

        addQuestionButton.addActionListener(e -> addQuestion());

        editQuestionButton.addActionListener(
                e -> editSelectedQuestion()
        );

        deleteQuestionButton.addActionListener(
                e -> deleteSelectedQuestion()
        );

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // LOAD EXAMS INTO DROPDOWN
    private void loadQuestionExams() {

        if (examCombo == null) {
            return;
        }

        int previousExamId = -1;

        Object previousSelection = examCombo.getSelectedItem();

        if (previousSelection instanceof Exam) {

            previousExamId =
                    ((Exam) previousSelection).getExamId();
        }

        examCombo.removeAllItems();

        List<Exam> exams = examService.getAllExams();

        if (exams != null) {

            for (Exam exam : exams) {
                examCombo.addItem(exam);
            }
        }

        boolean restored = false;

        for (int i = 0; i < examCombo.getItemCount(); i++) {

            Exam exam = examCombo.getItemAt(i);

            if (exam.getExamId() == previousExamId) {

                examCombo.setSelectedIndex(i);
                restored = true;
                break;
            }
        }

        if (!restored && examCombo.getItemCount() > 0) {
            examCombo.setSelectedIndex(0);
        }

        if (examCombo.getItemCount() == 0) {
            loadQuestions();
        }
    }

    // DISPLAY QUESTIONS
    private void loadQuestions() {

        if (questionTable == null) {
            return;
        }

        String[] columns = {
                "Question ID",
                "Question",
                "Option A",
                "Option B",
                "Option C",
                "Option D",
                "Correct Answer"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row, int column) {
                        return false;
                    }
                };

        Exam selectedExam =
                (Exam) examCombo.getSelectedItem();

        if (selectedExam != null) {

            List<Question> questions =
                    questionService.getQuestionsByExamId(
                            selectedExam.getExamId()
                    );

            if (questions != null) {

                for (Question question : questions) {

                    model.addRow(new Object[]{
                            question.getQuestionId(),
                            question.getQuestionText(),
                            question.getOptionA(),
                            question.getOptionB(),
                            question.getOptionC(),
                            question.getOptionD(),
                            question.getCorrectOption()
                    });
                }
            }
        }

        questionTable.setModel(model);
    }

    // ADD QUESTION
    private void addQuestion() {

        Exam selectedExam =
                (Exam) examCombo.getSelectedItem();

        if (selectedExam == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please create and select an exam first."
            );

            return;
        }

        JTextField questionField = new JTextField();
        JTextField optionAField = new JTextField();
        JTextField optionBField = new JTextField();
        JTextField optionCField = new JTextField();
        JTextField optionDField = new JTextField();

        JComboBox<String> correctOptionCombo =
                new JComboBox<>(
                        new String[]{"A", "B", "C", "D"}
                );

        Object[] fields = {
                "Exam:", selectedExam.getTitle(),
                "Question:", questionField,
                "Option A:", optionAField,
                "Option B:", optionBField,
                "Option C:", optionCField,
                "Option D:", optionDField,
                "Correct Option:", correctOptionCombo
        };

        int choice = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add MCQ Question",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        Question question = new Question();

        question.setExamId(selectedExam.getExamId());
        question.setQuestionText(questionField.getText().trim());
        question.setOptionA(optionAField.getText().trim());
        question.setOptionB(optionBField.getText().trim());
        question.setOptionC(optionCField.getText().trim());
        question.setOptionD(optionDField.getText().trim());

        question.setCorrectOption(
                ((String) correctOptionCombo.getSelectedItem()).charAt(0)
        );

        boolean added = questionService.addQuestion(question);

        if (added) {

            JOptionPane.showMessageDialog(
                    this,
                    "Question added successfully!"
            );

            loadQuestions();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to add question. "
                            + "Please fill in all fields correctly."
            );
        }
    }

    // EDIT SELECTED QUESTION
    private void editSelectedQuestion() {

        int row = questionTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a question to edit."
            );

            return;
        }

        int modelRow =
                questionTable.convertRowIndexToModel(row);

        int questionId = (int)
                questionTable.getModel().getValueAt(modelRow, 0);

        Question question =
                questionService.getQuestionById(questionId);

        if (question == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Question not found."
            );

            return;
        }

        JTextField questionField =
                new JTextField(question.getQuestionText());

        JTextField optionAField =
                new JTextField(question.getOptionA());

        JTextField optionBField =
                new JTextField(question.getOptionB());

        JTextField optionCField =
                new JTextField(question.getOptionC());

        JTextField optionDField =
                new JTextField(question.getOptionD());

        JComboBox<String> correctCombo =
                new JComboBox<>(
                        new String[]{"A", "B", "C", "D"}
                );

        correctCombo.setSelectedItem(
                String.valueOf(question.getCorrectOption())
        );

        Object[] fields = {
                "Question:", questionField,
                "Option A:", optionAField,
                "Option B:", optionBField,
                "Option C:", optionCField,
                "Option D:", optionDField,
                "Correct Option:", correctCombo
        };

        int choice = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Edit MCQ Question",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        question.setQuestionText(questionField.getText().trim());
        question.setOptionA(optionAField.getText().trim());
        question.setOptionB(optionBField.getText().trim());
        question.setOptionC(optionCField.getText().trim());
        question.setOptionD(optionDField.getText().trim());

        question.setCorrectOption(
                ((String) correctCombo.getSelectedItem()).charAt(0)
        );

        boolean updated =
                questionService.updateQuestion(question);

        if (updated) {

            JOptionPane.showMessageDialog(
                    this,
                    "Question updated successfully!"
            );

            loadQuestions();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update question. "
                            + "Check all fields."
            );
        }
    }

    // DELETE SELECTED QUESTION
    private void deleteSelectedQuestion() {

        int row = questionTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a question to delete."
            );

            return;
        }

        int modelRow =
                questionTable.convertRowIndexToModel(row);

        int questionId = (int)
                questionTable.getModel().getValueAt(modelRow, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this question?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        boolean deleted =
                questionService.deleteQuestion(questionId);

        if (deleted) {

            JOptionPane.showMessageDialog(
                    this,
                    "Question deleted successfully!"
            );

            loadQuestions();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete question. "
                            + "It may have student answers linked to it."
            );
        }
    }

    // ==========================================
    // STUDENT RESULTS
    // ==========================================

    private JPanel createResultPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        resultTable = new JTable();

        JScrollPane scrollPane =
                new JScrollPane(resultTable);

        JButton refreshButton =
                new JButton("Refresh Results");

        refreshButton.addActionListener(e -> loadResults());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    // LOAD ALL STUDENT RESULTS
    private void loadResults() {

        List<Result> results =
                resultService.getAllResults();

        String[] columns = {
                "Result ID",
                "Student ID",
                "Exam ID",
                "Score",
                "Submitted At"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row, int column) {
                        return false;
                    }
                };

        if (results != null) {

            for (Result result : results) {

                model.addRow(new Object[]{
                        result.getResultId(),
                        result.getUserId(),
                        result.getExamId(),
                        result.getScore(),
                        result.getSubmittedAt()
                });
            }
        }

        resultTable.setModel(model);
    }
}
