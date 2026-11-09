USE online_exam_db;

-- Disable foreign key checks temporarily for clean, repeatable resets
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE exam_results;
TRUNCATE TABLE attempt_answers;
TRUNCATE TABLE exam_attempts;
TRUNCATE TABLE exam_questions;
TRUNCATE TABLE questions;
TRUNCATE TABLE exams;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- 1. SEED USERS (Admins, Active Students, and 1 Deactivated Student for testing)
-- Note: Run DatabaseSeeder.java after executing seed.sql to populate live
-- Argon2 hashes for these accounts, or use the pre-encoded Argon2id PHC strings.
-- Default Password for all accounts via DatabaseSeeder: Password@123
-- ============================================================================
INSERT INTO users (id, username, password_hash, display_name, role, is_active) VALUES
(1, 'admin01',   '$argon2id$v=19$m=65536,t=3,p=2$cXZldG9zYWx0MjAyNg$placeholder_run_DatabaseSeeder_first', 'Dr. Rajesh Iyer (Admin)', 'ADMIN',   TRUE),
(2, 'student01', '$argon2id$v=19$m=65536,t=3,p=2$cXZldG9zYWx0MjAyNg$placeholder_run_DatabaseSeeder_first', 'Aarav Sharma',            'STUDENT', TRUE),
(3, 'student02', '$argon2id$v=19$m=65536,t=3,p=2$cXZldG9zYWx0MjAyNg$placeholder_run_DatabaseSeeder_first', 'Priya Nair',              'STUDENT', TRUE),
(4, 'student03', '$argon2id$v=19$m=65536,t=3,p=2$cXZldG9zYWx0MjAyNg$placeholder_run_DatabaseSeeder_first', 'Rohan Deshmukh (Blocked)','STUDENT', FALSE);

-- ============================================================================
-- 2. SEED REUSABLE QUESTION BANK (10 Questions across Java, SQL & DBMS, DSA)
-- Includes 1 archived question (id=10) to test filter exclusion
-- ============================================================================
INSERT INTO questions (id, subject_tag, question_text, option_a, option_b, option_c, option_d, correct_option, default_marks, is_archived, created_by) VALUES
(1, 'Core Java',
 'What is the time complexity of retrieving a value by key from a java.util.HashMap in the worst-case scenario in Java 8+ when hash collisions exceed the treeify threshold (8) and capacity is at least 64?',
 'O(1)', 'O(log n)', 'O(n)', 'O(n log n)',
 'B', 2.00, FALSE, 1),

(2, 'Core Java',
 'Which statement accurately describes the execution order during Java class initialization and object instantiation?',
 'Child static blocks -> Parent static blocks -> Parent constructor -> Child constructor',
 'Parent constructor -> Child constructor -> Parent static blocks -> Child static blocks',
 'Parent static blocks -> Child static blocks -> Parent instance initializers & constructor -> Child instance initializers & constructor',
 'Parent instance initializers -> Parent static blocks -> Child static blocks -> Child constructor',
 'C', 2.00, FALSE, 1),

(3, 'Core Java',
 'In Java 21+, what happens when a Virtual Thread executes a blocking I/O operation via java.net.Socket?',
 'It blocks the underlying OS carrier platform thread until I/O completes',
 'It unmounts from the carrier platform thread, freeing the carrier to run other virtual threads',
 'It throws an IllegalMonitorStateException if not inside a synchronized block',
 'It automatically spawns a new OS kernel thread to handle the blocking call',
 'B', 2.00, FALSE, 1),

(4, 'Core Java',
 'Why is char[] preferred over String for handling raw passwords in memory before hashing?',
 'char[] automatically encrypts its contents in the CPU L1 cache',
 'String objects are immutable and may reside in the heap/String pool until garbage collection, whereas char[] can be explicitly zeroed out immediately after use',
 'PreparedStatement only accepts char[] for password columns',
 'String cannot represent special Unicode characters used in modern passwords',
 'B', 2.00, FALSE, 1),

(5, 'SQL & DBMS',
 'Which index data structure is used by default in MySQL InnoDB tables to support both exact-match lookups and range queries efficiently?',
 'Hash Index', 'R-Tree Index', 'B+ Tree Index', 'Bitmap Index',
 'C', 2.00, FALSE, 1),

(6, 'SQL & DBMS',
 'In MySQL InnoDB, what is the default transaction isolation level?',
 'READ UNCOMMITTED', 'READ COMMITTED', 'REPEATABLE READ', 'SERIALIZABLE',
 'C', 2.00, FALSE, 1),

(7, 'SQL & DBMS',
 'Why should PreparedStatement always be used instead of dynamic String concatenation when executing parameterized SQL queries in JDBC?',
 'It compiles SQL directly into JVM bytecode',
 'It separates query structure from data parameters, preventing SQL injection and enabling server/driver prepared statement caching',
 'It automatically commits transactions in parallel without locks',
 'It bypasses foreign key constraints for faster inserts',
 'B', 2.00, FALSE, 1),

(8, 'Data Structures',
 'Which auxiliary data structure allows computing the Next Greater Element for all n elements of an array in O(n) worst-case time complexity?',
 'Min-Heap (PriorityQueue)', 'Balanced Binary Search Tree (TreeSet)', 'Monotonic Stack', 'Disjoint Set Union (Union-Find)',
 'C', 2.00, FALSE, 1),

(9, 'Data Structures',
 'In a probabilistic Skip List with n elements, what is the expected average time complexity for search, insertion, and deletion operations?',
 'O(1)', 'O(log n)', 'O(sqrt(n))', 'O(n)',
 'B', 2.00, FALSE, 1),

(10, 'Legacy Java',
 'Which method was deprecated and removed for stopping threads because it is inherently unsafe and unlocks all monitors?',
 'Thread.interrupt()', 'Thread.stop()', 'Thread.yield()', 'Thread.join()',
 'B', 1.00, TRUE, 1);

-- ============================================================================
-- 3. SEED EXAMS (2 Published Exams + 1 Draft Unpublished Exam)
-- ============================================================================
INSERT INTO exams (id, title, description, duration_minutes, total_questions, total_marks, is_published, created_by) VALUES
(1, 'Java Backend & JVM Internals Assessment',
 'Timed assessment covering Java memory semantics, class loading, concurrency, and JDBC fundamentals.',
 15, 5, 10.00, TRUE, 1),

(2, 'Database Systems & Algorithmic Problem Solving',
 'Evaluates relational database indexing, transaction isolation, SQL security, and core data structures ( demonstrates Question Reuse across exams ).',
 20, 5, 10.00, TRUE, 1),

(3, 'Advanced System Design Mock (Draft)',
 'Unpublished draft exam — should NOT be visible on the Student Dashboard.',
 30, 3, 6.00, FALSE, 1);

-- ============================================================================
-- 4. SEED EXAM-QUESTIONS MAPPING (Demonstrates Question Bank Reuse)
-- Notice Question 4 and Question 7 are reused across both Exam 1 and Exam 2
-- ============================================================================
-- Exam 1 Questions (5 questions x 2.00 marks = 10.00 total marks)
INSERT INTO exam_questions (exam_id, question_id, question_order, marks_allocated) VALUES
(1, 1, 1, 2.00),
(1, 2, 2, 2.00),
(1, 3, 3, 2.00),
(1, 4, 4, 2.00),
(1, 7, 5, 2.00);

-- Exam 2 Questions (Reuses Q4 and Q7 + adds Q5, Q6, Q8 = 10.00 total marks)
INSERT INTO exam_questions (exam_id, question_id, question_order, marks_allocated) VALUES
(2, 5, 1, 2.00),
(2, 6, 2, 2.00),
(2, 7, 3, 2.00), -- Reused from Question Bank!
(2, 8, 4, 2.00),
(2, 4, 5, 2.00); -- Reused from Question Bank!

-- ============================================================================
-- 5. SEED EXAM ATTEMPTS (Historical & Edge-Case States for Testing)
-- ============================================================================
-- Attempt 1: student01 manually submitted Exam 1 (Score: 8/10 -> 80.00%)
-- Attempt 2: student02 auto-submitted Exam 1 on timeout (Score: 6/10 -> 60.00%)
INSERT INTO exam_attempts (id, exam_id, student_id, status, started_at, expires_at, submitted_at) VALUES
(1, 1, 2, 'SUBMITTED_MANUAL',
 DATE_SUB(UTC_TIMESTAMP(), INTERVAL 2 HOUR),
 DATE_SUB(UTC_TIMESTAMP(), INTERVAL 105 MINUTE),
 DATE_SUB(UTC_TIMESTAMP(), INTERVAL 108 MINUTE)),

(2, 1, 3, 'SUBMITTED_AUTO',
 DATE_SUB(UTC_TIMESTAMP(), INTERVAL 1 HOUR),
 DATE_SUB(UTC_TIMESTAMP(), INTERVAL 45 MINUTE),
 DATE_SUB(UTC_TIMESTAMP(), INTERVAL 45 MINUTE));

-- ============================================================================
-- 6. SEED ATTEMPT ANSWERS
-- ============================================================================
-- Attempt 1 Answers (student01: 4 Correct [Q1,Q2,Q3,Q7], 1 Incorrect [Q4])
INSERT INTO attempt_answers (attempt_id, question_id, selected_option) VALUES
(1, 1, 'B'), -- Correct
(1, 2, 'C'), -- Correct
(1, 3, 'B'), -- Correct
(1, 4, 'A'), -- Incorrect (Correct is B)
(1, 7, 'B'); -- Correct

-- Attempt 2 Answers (student02: 3 Correct [Q1,Q2,Q7], 1 Incorrect [Q3], 1 Unanswered [Q4])
INSERT INTO attempt_answers (attempt_id, question_id, selected_option) VALUES
(2, 1, 'B'), -- Correct
(2, 2, 'C'), -- Correct
(2, 3, 'A'), -- Incorrect (Correct is B)
(2, 4, NULL),-- Visited but left unanswered
(2, 7, 'B'); -- Correct

-- ============================================================================
-- 7. SEED IMMUTABLE EXAM RESULTS
-- ============================================================================
INSERT INTO exam_results (
    attempt_id, total_questions, attempted_questions,
    correct_answers, incorrect_answers,
    score_obtained, total_marks, percentage
) VALUES
(1, 5, 5, 4, 1, 8.00, 10.00, 80.00),
(2, 5, 4, 3, 1, 6.00, 10.00, 60.00);