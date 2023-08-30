DROP TABLE IF EXISTS student_course CASCADE;
DROP TABLE IF EXISTS courses CASCADE;

CREATE TABLE courses (
    course_id INT NOT NULL,
    course_name VARCHAR(20) NOT NULL,
    course_description VARCHAR(20) NOT NULL,
    PRIMARY KEY (course_id)
);

CREATE TABLE student_course (
    student_id INT,
    course_id INT,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);
