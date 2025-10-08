-- -----------------------------------------------------
-- Schema university
-- -----------------------------------------------------

 
CREATE DATABASE IF NOT EXISTS university;
USE university;

set foreign_key_checks = 0;

-- -----------------------------------------------------
-- Table university
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS university (
  university_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (university_id));



-- -----------------------------------------------------
-- Table building
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS building (
  building_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  university_id INT NULL,
  PRIMARY KEY (building_id),
    FOREIGN KEY (university_id)
    REFERENCES university (university_id)
);



-- -----------------------------------------------------
-- Table classroom
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS classroom (
  classroom_id INT NOT NULL AUTO_INCREMENT,
  room_number VARCHAR(45) NULL,
  building_id INT NULL,
  capacity INT NULL,
  room_type VARCHAR(45) NULL,
  scheduled_start DATETIME NULL,
  scheduled_end DATETIME NULL,
  university_id INT NULL,
  PRIMARY KEY (classroom_id),
    FOREIGN KEY (building_id)
    REFERENCES building (building_id));



-- -----------------------------------------------------
-- Table person
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS person (
  person_id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  email VARCHAR(45) NOT NULL,
  university_id INT NULL,
  professor_id INT NULL,
  student_id INT NULL,
  staff_id INT NULL,
  PRIMARY KEY (person_id),
    FOREIGN KEY (university_id)
    REFERENCES university (university_id),
    FOREIGN KEY (professor_id)
    REFERENCES professor (professor_id),
    FOREIGN KEY (student_id)
    REFERENCES student (student_id),
    FOREIGN KEY (staff_id)
    REFERENCES staff (staff_id)
   );



-- -----------------------------------------------------
-- Table department
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS department (
  department_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NULL,
  code VARCHAR(45) NULL,
  university_id INT NULL,
  PRIMARY KEY (department_id),
    FOREIGN KEY (university_id)
    REFERENCES university (university_id)
    );


-- -----------------------------------------------------
-- Table staff
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS staff (
  staff_id INT NOT NULL AUTO_INCREMENT,
  department_id INT NULL,
  title VARCHAR(45) NOT NULL,
  person_id INT,
  PRIMARY KEY (staff_id),
    FOREIGN KEY (person_id)
    REFERENCES person (person_id),
    FOREIGN KEY (department_id)
    REFERENCES department (department_id)
    );



-- -----------------------------------------------------
-- Table professor
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS professor (
  professor_id INT NOT NULL AUTO_INCREMENT,
  department_id INT NULL,
  person_id INT,
  PRIMARY KEY (professor_id),
    FOREIGN KEY (department_id)
    REFERENCES department (department_id),
    FOREIGN KEY (person_id)
    REFERENCES person (person_id)
    );



-- -----------------------------------------------------
-- Table course_difficulty
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS course_difficulty (
  course_difficulty_id INT NOT NULL AUTO_INCREMENT,
  display_name VARCHAR(45) NULL,
  lvl INT NULL,
  PRIMARY KEY (course_difficulty_id));



-- -----------------------------------------------------
-- Table course
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS course (
  course_id INT NOT NULL AUTO_INCREMENT,
  course_code VARCHAR(45) NULL,
  course_name VARCHAR(45) NULL,
  credit_hours INT NULL,
  professor_id INT NULL,
  department_id INT NULL,
  start_at DATETIME NULL,
  end_at DATETIME NULL,
  classroom_id INT NULL,
  course_difficulty_id INT NULL,
  university_id INT NULL,
  PRIMARY KEY (course_id),
  
    FOREIGN KEY (classroom_id)
    REFERENCES classroom (classroom_id),
    FOREIGN KEY (department_id)
    REFERENCES department (department_id),
    FOREIGN KEY (course_difficulty_id)
    REFERENCES course_difficulty (course_difficulty_id)
    );


-- -----------------------------------------------------
-- Table course_grade
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS course_grade (
  course_grade_id INT NOT NULL AUTO_INCREMENT,
  course_id INT,
  subject VARCHAR(45) NULL,
  value FLOAT NULL,
  semester INT NULL,
  recorded_at DATETIME NULL,
  PRIMARY KEY (course_grade_id),
    FOREIGN KEY (course_id)
    REFERENCES course (course_id)
    );


-- -----------------------------------------------------
-- Table program
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS program (
  program_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  duration_years INT NULL,
  price FLOAT NULL,
  department_id INT NULL,
  university_id INT NULL,
  PRIMARY KEY (program_id),
    FOREIGN KEY (department_id)
    REFERENCES department (department_id)
    );


-- -----------------------------------------------------
-- Table enrollment_status
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS enrollment_status (
  enrollment_status_id INT NOT NULL AUTO_INCREMENT,
  display_name VARCHAR(45) NULL,
  description VARCHAR(45) NULL,
  PRIMARY KEY (enrollment_status_id));



-- -----------------------------------------------------
-- Table grade_level
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS grade_level (
  grade_level_id INT NOT NULL AUTO_INCREMENT,
  display_name VARCHAR(45) NULL,
  year INT NULL,
  PRIMARY KEY (grade_level_id));


-- -----------------------------------------------------
-- Table student
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS student (
  student_id INT NOT NULL AUTO_INCREMENT,
  age INT NULL,
  student_number INT NULL,
  is_registered TINYINT NULL,
  balance FLOAT NULL,
  enrollment_status_id INT NULL,
  grade_level_id INT NULL,
  person_id INT,
  PRIMARY KEY (student_id),
    FOREIGN KEY (enrollment_status_id)
    REFERENCES enrollment_status (enrollment_status_id),
    FOREIGN KEY (grade_level_id)
    REFERENCES grade_level (grade_level_id),
    FOREIGN KEY (person_id)
    REFERENCES person (person_id)
    );


-- -----------------------------------------------------
-- Table enrollment
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS enrollment (
  enrollment_id INT NOT NULL AUTO_INCREMENT,
  student_id INT,
  program_id INT,
  enrollment_date DATETIME NULL,
  enrollment_status_id INT NULL,
  PRIMARY KEY (enrollment_id),
    FOREIGN KEY (student_id)
    REFERENCES student (student_id),
    FOREIGN KEY (program_id)
    REFERENCES program (program_id),
    FOREIGN KEY (enrollment_status_id)
    REFERENCES enrollment_status (enrollment_status_id)
    
    );


-- -----------------------------------------------------
-- Table student_grade
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS student_grade (
  student_grade_id INT NOT NULL AUTO_INCREMENT,
  student_id INT NULL,
  subject VARCHAR(45) NULL,
  value FLOAT NULL,
  semester INT NULL,
  PRIMARY KEY (student_grade_id),
  
    FOREIGN KEY (student_id)
    REFERENCES student (student_id)
    );
    
set foreign_key_checks = 1;



