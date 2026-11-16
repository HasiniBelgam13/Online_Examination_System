
package com.example.ui;

import com.example.model.User;
import com.example.model.Exam;
import com.example.model.Result;
import com.example.model.Question;

import com.example.service.ExamService;
import com.example.service.ResultService;
import com.example.service.ResultService.ResultSummary;
import com.example.service.QuestionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {

    private final User student;

    private final ExamService examService =
            new ExamService();

    private final ResultService resultService =
            new ResultService();

    private final QuestionService questionService =
            new QuestionService();

    private JTable examTable;
    private JTable resultTable;

    public StudentDashboard(User student) {

        this.student = student;

        setTitle("Online Examination System - Student Dashboard");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createDashboard();

        loadExams();
        loadResults();

        setVisible(true);
    }

    private void createDashboard() {

        setLayout(new BorderLayout(10, 10));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(new Color(35, 55, 90));

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + student.getName()
        );

        welcomeLabel.setForeground(Color.WHITE);

        welcomeLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

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

        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(logoutButton, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // TABS
        JTabbedPane tabs = new JTabbedPane();

        // AVAILABLE EXAMS
        JPanel examPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        examTable = new JTable();

        JScrollPane examScroll =
                new JScrollPane(examTable);

        JButton refreshExamsButton =
                new JButton("Refresh Exams");

        JButton startExamButton =
                new JButton("Start Exam");

        refreshExamsButton.addActionListener(
                e -> loadExams()
        );

        startExamButton.addActionListener(e -> {

            int row = examTable.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select an exam first."
                );

                return;
            }

            int modelRow =
                    examTable.convertRowIndexToModel(row);

            int examId = (int)
                    examTable.getModel().getValueAt(
                            modelRow, 0
                    );

            Exam selectedExam =
                    examService.getExamById(examId);

            if (selectedExam == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Exam not found."
                );

                return;
            }

            List<Question> questions =
                    questionService.getQuestionsByExamId(
                            examId
                    );

            if (questions == null || questions.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No questions available for this exam."
                );

                return;
            }

            // Check previous submission
            List<Result> existingResults =
                    resultService.getStudentResults(
                            student.getUserId()
                    );

            if (existingResults != null) {

                for (Result result : existingResults) {

                    if (result.getExamId() == examId) {

                        JOptionPane.showMessageDialog(
                                this,
                                "You have already completed this exam."
                        );

                        return;
                    }
                }
            }

            // Exam instructions
            int choice = JOptionPane.showConfirmDialog(
                    this,

                    "Exam: " + selectedExam.getTitle()
                            + "\nDuration: "
                            + selectedExam.getDurationMinutes()
                            + " minutes"
                            + "\nTotal Marks: "
                            + selectedExam.getTotalMarks()
                            + "\nTotal Questions: "
                            + questions.size()
                            + "\n\nAre you ready to start?",

                    "Exam Instructions",

                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                new ExamScreen(
                        student,
                        selectedExam,
                        questions
                );
            }
        });

        JPanel examButtons = new JPanel();

        examButtons.add(refreshExamsButton);
        examButtons.add(startExamButton);

        examPanel.add(
                examScroll,
                BorderLayout.CENTER
        );

        examPanel.add(
                examButtons,
                BorderLayout.SOUTH
        );

        tabs.addTab(
                "Available Exams",
                examPanel
        );

        // MY RESULTS
        JPanel resultPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        resultTable = new JTable();

        resultTable.setRowHeight(28);
        resultTable.setAutoCreateRowSorter(true);

        JScrollPane resultScroll =
                new JScrollPane(resultTable);

        JButton refreshResultsButton =
                new JButton("Refresh Results");

        refreshResultsButton.addActionListener(
                e -> loadResults()
        );

        resultPanel.add(
                resultScroll,
                BorderLayout.CENTER
        );

        resultPanel.add(
                refreshResultsButton,
                BorderLayout.SOUTH
        );

        tabs.addTab(
                "My Results",
                resultPanel
        );

        add(tabs, BorderLayout.CENTER);
    }

    // LOAD AVAILABLE EXAMS
    private void loadExams() {

        List<Exam> exams =
                examService.getAllExams();

        String[] columns = {
                "Exam ID",
                "Exam Title",
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

    // LOAD STUDENT RESULTS WITH COMPLETE DETAILS
    private void loadResults() {

        List<Result> results =
                resultService.getStudentResults(
                        student.getUserId()
                );

        String[] columns = {
                "Exam ID",
                "Score Obtained",
                "Total Marks",
                "Total Questions",
                "Correct Answers",
                "Incorrect Answers",
                "Percentage",
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

                ResultSummary summary =
                        resultService.getResultSummary(
                                student.getUserId(),
                                result.getExamId()
                        );

                if (summary == null) {
                    continue;
                }

                model.addRow(new Object[]{

                        result.getExamId(),

                        result.getScore(),

                        summary.getTotalMarks(),

                        summary.getTotalQuestions(),

                        summary.getCorrectAnswers(),

                        summary.getIncorrectAnswers(),

                        String.format(
                                "%.2f%%",
                                summary.getPercentage()
                        ),

                        result.getSubmittedAt()

                });
            }
        }

        resultTable.setModel(model);
    }
}
