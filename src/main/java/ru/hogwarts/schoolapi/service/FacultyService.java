package ru.hogwarts.schoolapi.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.schoolapi.model.Faculty;
import ru.hogwarts.schoolapi.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public List<Faculty> getFacultyByColor(String color) {
        return facultyRepository.getFacultyByColor(color);
    }

    public List<Faculty> getAllFaculties() {
        return new ArrayList<>(facultyRepository.findAll());
    }
}
