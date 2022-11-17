SELECT (student.name, age)
FROM student
         LEFT JOIN faculty f ON student.faculty_id = f.id;

SELECT (student.name, age)
FROM student
         INNER JOIN avatar a on student.id = a.student_id;