package ru.hogwarts.schoolapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.schoolapi.model.Faculty;

import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> getFacultyByColor(String color);

    Faculty getFacultyByNameContainsIgnoreCaseOrColorContainsIgnoreCase(String name, String color);
}
