package ru.hogwarts.schoolapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.schoolapi.model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
