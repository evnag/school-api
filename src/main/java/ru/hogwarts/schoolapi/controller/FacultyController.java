package ru.hogwarts.schoolapi.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.schoolapi.record.FacultyRecord;
import ru.hogwarts.schoolapi.record.StudentRecord;
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

    @GetMapping(value = "/faculty-by-color", params = "!colorOrName")
    public List<FacultyRecord> findFacultyByColor(@RequestParam String color) {
        return facultyService.getFacultyByColor(color);
    }

    @GetMapping(value = "/by-name-or-color", params = "colorOrName")
    public FacultyRecord getFacultyByNameOrColor(@RequestParam String nameOrColor) {
        return facultyService.getFacultyByNameContainsIgnoreCaseOrColorContainsIgnoreCase(nameOrColor);
    }

    @GetMapping("/longest-name")
    public FacultyRecord findFacultyWithLongestName() {
        return facultyService.findFacultyWithLongestName();
    }

    @GetMapping
    public List<FacultyRecord> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @GetMapping("/{id}/students")
    public List<StudentRecord> getStudentsByFacultyId(@PathVariable long id) {
        return facultyService.getStudentsByFacultyId(id);
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
