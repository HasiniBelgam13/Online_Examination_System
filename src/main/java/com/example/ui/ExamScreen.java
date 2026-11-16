
package com.example.ui;

import com.example.model.User;
import com.example.model.Exam;
import com.example.model.Question;
import com.example.model.StudentAnswer;
import com.example.service.StudentAnswerService;
import com.example.service.ResultService;
import com.example.timer.ExamTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ExamScreen extends JFrame {

    private final User student;
    private final Exam exam;
    private final List<Question> questions;

    private final StudentAnswerService answerService =
            new StudentAnswerService();

    private final ResultService resultService =
            new ResultService();

    private int currentIndex = 0;

    private JLabel questionLabel;
    private JLabel timerLabel;

    private JRadioButton optionA;
    private JRadioButton optionB;
    private JRadioButton optionC;
    private JRadioButton optionD;

    private ButtonGroup optionGroup;

    private JButton previousButton;
    private JButton nextButton;
    private JButton submitButton;

    private ExamTimer examTimer;

    private boolean submitted = false;
    private boolean submitting = false;

    public ExamScreen(User student, Exam exam,
                      List<Question> questions) {

        this.student = student;
        this.exam = exam;
        this.questions = questions;

        if (questions == null || questions.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No questions available for this exam.",
                    "Exam",
                    JOptionPane.WARNING_MESSAGE
            );
            dispose();
            return;
        }

        setTitle("Online Examination - " + exam.getTitle());
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        displayQuestion();

        examTimer = new ExamTimer(
                exam.getDurationMinutes(),
                seconds -> SwingUtilities.invokeLater(
                        () -> updateTimer(seconds)
                ),
                () -> SwingUtilities.invokeLater(
                        this::submitExam
                )
        );

        timerLabel.setText(examTimer.getFormattedTime());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });

        examTimer.start();
        setVisible(true);
    }

    private void createUI() {

        setLayout(new BorderLayout(15, 15));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(35, 55, 90));
        header.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        JLabel titleLabel = new JLabel(exam.getTitle());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        timerLabel = new JLabel("00:00");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        header.add(titleLabel, BorderLayout.WEST);
        header.add(timerLabel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Question and options
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(
                new BoxLayout(centerPanel, BoxLayout.Y_AXIS)
        );

        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );

        questionLabel = new JLabel();
        questionLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(30));

        optionA = new JRadioButton();
        optionB = new JRadioButton();
        optionC = new JRadioButton();
        optionD = new JRadioButton();

        optionGroup = new ButtonGroup();

        optionGroup.add(optionA);
        optionGroup.add(optionB);
        optionGroup.add(optionC);
        optionGroup.add(optionD);

        Font optionFont = new Font("Arial", Font.PLAIN, 16);

        optionA.setFont(optionFont);
        optionB.setFont(optionFont);
        optionC.setFont(optionFont);
        optionD.setFont(optionFont);

        centerPanel.add(optionA);
        centerPanel.add(Box.createVerticalStrut(15));

        centerPanel.add(optionB);
        centerPanel.add(Box.createVerticalStrut(15));

        centerPanel.add(optionC);
        centerPanel.add(Box.createVerticalStrut(15));

        centerPanel.add(optionD);

        add(centerPanel, BorderLayout.CENTER);

        // Save answer immediately after selection
        optionA.addActionListener(e -> saveCurrentAnswer());
        optionB.addActionListener(e -> saveCurrentAnswer());
        optionC.addActionListener(e -> saveCurrentAnswer());
        optionD.addActionListener(e -> saveCurrentAnswer());

        // Navigation buttons
        JPanel bottomPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 20, 15)
        );

        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit Exam");

        previousButton.addActionListener(e -> {

            if (submitted || submitting) {
                return;
            }

            saveCurrentAnswer();

            if (currentIndex > 0) {
                currentIndex--;
                displayQuestion();
            }
        });

        nextButton.addActionListener(e -> {

            if (submitted || submitting) {
                return;
            }

            saveCurrentAnswer();

            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                displayQuestion();
            }
        });

        submitButton.addActionListener(e -> {

            if (submitted || submitting) {
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to submit the exam?",
                    "Confirm Submission",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                submitExam();
            }
        });

        bottomPanel.add(previousButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Display one question at a time
    private void displayQuestion() {

        if (currentIndex < 0 || currentIndex >= questions.size()) {
            return;
        }

        Question question = questions.get(currentIndex);

        questionLabel.setText(
                "<html><b>Question "
                        + (currentIndex + 1)
                        + " of "
                        + questions.size()
                        + "</b><br><br>"
                        + question.getQuestionText()
                        + "</html>"
        );

        optionA.setText("A. " + question.getOptionA());
        optionB.setText("B. " + question.getOptionB());
        optionC.setText("C. " + question.getOptionC());
        optionD.setText("D. " + question.getOptionD());

        optionGroup.clearSelection();

        // Restore previously saved answer
        List<StudentAnswer> savedAnswers =
                answerService.getStudentAnswers(
                        student.getUserId(),
                        exam.getExamId()
                );

        if (savedAnswers != null) {

            for (StudentAnswer answer : savedAnswers) {

                if (answer.getQuestionId()
                        == question.getQuestionId()) {

                    selectOption(answer.getSelectedOption());
                    break;
                }
            }
        }

        previousButton.setEnabled(currentIndex > 0);

        nextButton.setEnabled(
                currentIndex < questions.size() - 1
        );
    }

    // Select saved option
    private void selectOption(char selected) {

        switch (Character.toUpperCase(selected)) {

            case 'A':
                optionA.setSelected(true);
                break;

            case 'B':
                optionB.setSelected(true);
                break;

            case 'C':
                optionC.setSelected(true);
                break;

            case 'D':
                optionD.setSelected(true);
                break;

            default:
                break;
        }
    }

    // Save current answer
    private void saveCurrentAnswer() {

        if (submitted || submitting) {
            return;
        }

        char selected = '\0';

        if (optionA.isSelected()) {
            selected = 'A';
        } else if (optionB.isSelected()) {
            selected = 'B';
        } else if (optionC.isSelected()) {
            selected = 'C';
        } else if (optionD.isSelected()) {
            selected = 'D';
        }

        if (selected == '\0') {
            return;
        }

        Question question = questions.get(currentIndex);

        StudentAnswer answer = new StudentAnswer();

        answer.setUserId(student.getUserId());
        answer.setExamId(exam.getExamId());
        answer.setQuestionId(question.getQuestionId());
        answer.setSelectedOption(selected);

        answerService.saveAnswer(answer);
    }

    // Update countdown
    private void updateTimer(int seconds) {

        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        timerLabel.setText(
                String.format(
                        "%02d:%02d",
                        minutes,
                        remainingSeconds
                )
        );

        if (seconds <= 60) {
            timerLabel.setForeground(Color.RED);
        }
    }

    // Handle window close
    private void handleWindowClosing() {

        if (submitted || submitting) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Closing this window will submit your examination. "
                        + "Do you want to continue?",
                "Exit Examination",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            submitExam();
        }
    }

    // Submit exam
    private void submitExam() {

        if (submitted || submitting) {
            return;
        }

        // IMPORTANT: Save the current answer BEFORE locking the screen.
        saveCurrentAnswer();

        submitting = true;

        submitButton.setEnabled(false);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);

        optionA.setEnabled(false);
        optionB.setEnabled(false);
        optionC.setEnabled(false);
        optionD.setEnabled(false);

        examTimer.stop();

        boolean saved = resultService.calculateAndSaveResult(
                student.getUserId(),
                exam.getExamId()
        );

        if (saved) {

            submitted = true;

            JOptionPane.showMessageDialog(
                    this,
                    "Exam submitted successfully!",
                    "Submission Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } else {

            submitting = false;

            JOptionPane.showMessageDialog(
                    this,
                    "Could not save the result. Please try again.",
                    "Submission Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            // If time remains, allow the student to continue.
            if (examTimer.getRemainingSeconds() > 0) {

                examTimer.resume();

                submitButton.setEnabled(true);
                previousButton.setEnabled(currentIndex > 0);

                nextButton.setEnabled(
                        currentIndex < questions.size() - 1
                );

                optionA.setEnabled(true);
                optionB.setEnabled(true);
                optionC.setEnabled(true);
                optionD.setEnabled(true);

            } else {

                // Time expired: allow submission retry,
                // but do not allow answer changes.
                submitButton.setEnabled(true);
            }
        }
    }
}
