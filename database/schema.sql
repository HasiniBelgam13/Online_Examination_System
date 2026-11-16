CREATE DATABASE IF NOT EXISTS online_exam_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE online_exam_db;

-- 1. Users Table (Admins & Students)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_role (role)
) ENGINE=InnoDB;

-- 2. Exams Table
CREATE TABLE exams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    duration_minutes INT NOT NULL CHECK (duration_minutes > 0),
    total_questions INT NOT NULL CHECK (total_questions > 0),
    total_marks DECIMAL(6,2) NOT NULL CHECK (total_marks > 0),
    is_published BOOLEAN NOT NULL DEFAULT FALSE,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_exams_created_by FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB;

-- 3. Reusable Question Bank Table
CREATE TABLE questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_tag VARCHAR(80) NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(500) NOT NULL,
    option_b VARCHAR(500) NOT NULL,
    option_c VARCHAR(500) NOT NULL,
    option_d VARCHAR(500) NOT NULL,
    correct_option CHAR(1) NOT NULL CHECK (correct_option IN ('A', 'B', 'C', 'D')),
    default_marks DECIMAL(5,2) NOT NULL DEFAULT 1.00 CHECK (default_marks > 0),
    is_archived BOOLEAN NOT NULL DEFAULT FALSE,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_questions_created_by FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_questions_subject (subject_tag)
) ENGINE=InnoDB;

-- 4. Exam-Questions Mapping (Many-to-Many for Question Reuse)
CREATE TABLE exam_questions (
    exam_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    question_order INT NOT NULL,
    marks_allocated DECIMAL(5,2) NOT NULL CHECK (marks_allocated > 0),
    PRIMARY KEY (exam_id, question_id),
    UNIQUE KEY uk_exam_question_order (exam_id, question_order),
    CONSTRAINT fk_eq_exam FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE,
    CONSTRAINT fk_eq_question FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 5. Exam Attempts (Session State & Timing)
CREATE TABLE exam_attempts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    status ENUM('IN_PROGRESS', 'SUBMITTED_MANUAL', 'SUBMITTED_AUTO', 'ABANDONED') NOT NULL DEFAULT 'IN_PROGRESS',
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    submitted_at TIMESTAMP NULL,
    CONSTRAINT fk_attempts_exam FOREIGN KEY (exam_id) REFERENCES exams(id),
    CONSTRAINT fk_attempts_student FOREIGN KEY (student_id) REFERENCES users(id),
    INDEX idx_attempts_student_exam (student_id, exam_id),
    INDEX idx_attempts_status (status)
) ENGINE=InnoDB;

-- 6. Attempt Answers (Persisted during Next/Previous navigation)
CREATE TABLE attempt_answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attempt_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option CHAR(1) NULL CHECK (selected_option IN ('A', 'B', 'C', 'D')),
    saved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_attempt_question (attempt_id, question_id),
    CONSTRAINT fk_aa_attempt FOREIGN KEY (attempt_id) REFERENCES exam_attempts(id) ON DELETE CASCADE,
    CONSTRAINT fk_aa_question FOREIGN KEY (question_id) REFERENCES questions(id)
) ENGINE=InnoDB;

-- 7. Exam Results (Immutable Post-Submission Record)
CREATE TABLE exam_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attempt_id BIGINT NOT NULL UNIQUE,
    total_questions INT NOT NULL,
    attempted_questions INT NOT NULL,
    correct_answers INT NOT NULL,
    incorrect_answers INT NOT NULL,
    score_obtained DECIMAL(6,2) NOT NULL,
    total_marks DECIMAL(6,2) NOT NULL,
    percentage DECIMAL(5,2) NOT NULL,
    calculated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_results_attempt FOREIGN KEY (attempt_id) REFERENCES exam_attempts(id) ON DELETE CASCADE
) ENGINE=InnoDB;