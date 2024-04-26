SELECT student.name, student.age, faculty.name
FROM student
JOIN faculty ON student.faculty_id = faculty.id

SELECT student.name, student.age, avatar.file_path
FROM student
JOIN avatar ON student.id = avatar.student_id;