package ru.hogwarts.schoolapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.schoolapi.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> getStudentByAge(int age);
}
