SELECT (student.name, age, faculty.name)
FROM student
JOIN faculty ON student.faculty_id = faculty.id;

SELECT (avatar.file_path, student.name)
FROM avatar
JOIN student ON avatar.student_id = student.id;