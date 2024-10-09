USE db;

CREATE TABLE stud_table (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    mail VARCHAR(50) NOT NULL,
    room_no INT NOT NULL
);

CREATE TABLE room_table (
    student_id INT,
    student_name VARCHAR(50),
    room_no INT
);
