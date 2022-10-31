package ru.hogwarts.schoolapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.schoolapi.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
