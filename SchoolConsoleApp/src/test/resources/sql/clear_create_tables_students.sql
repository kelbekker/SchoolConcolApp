DROP TABLE IF EXISTS students CASCADE;

CREATE TABLE students (
    student_id INT NOT NULL,
    group_id INT NOT NULL,
    first_name VARCHAR(15) NOT NULL,
    last_name VARCHAR(15) NOT NULL,
    PRIMARY KEY (student_id)
);