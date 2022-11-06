package ru.hogwarts.schoolapi.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.schoolapi.record.FacultyRecord;
import ru.hogwarts.schoolapi.service.FacultyService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}")
    public FacultyRecord getFaculty(@PathVariable long id) {
        return facultyService.findFaculty(id);
    }

    @GetMapping
    public List<FacultyRecord> findFacultyByColor(@RequestParam String color) {
        return facultyService.getFacultyByColor(color);
    }

    @GetMapping("/faculty-by-color")
    public List<FacultyRecord> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @PostMapping
    public FacultyRecord createFaculty(@RequestBody @Valid FacultyRecord facultyRecord) {
        return facultyService.createFaculty(facultyRecord);
    }

    @PutMapping("/{id}")
    public FacultyRecord editFaculty(@PathVariable long id,
                                     @RequestBody @Valid FacultyRecord facultyRecord) {
        return facultyService.editFaculty(id, facultyRecord);
    }

    @DeleteMapping("/{id}")
    public FacultyRecord deleteFaculty(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }
}
