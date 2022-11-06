package ru.hogwarts.schoolapi.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.schoolapi.record.StudentRecord;
import ru.hogwarts.schoolapi.service.StudentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public StudentRecord getStudent(@PathVariable long id) {
        return studentService.findStudent(id);
    }

    @GetMapping("/student-by-age")
    public List<StudentRecord> findStudentByAge(@RequestParam int age) {
        return studentService.getStudentByAge(age);
    }

    @GetMapping
    public List<StudentRecord> getAllStudents() {
        return studentService.getAllStudent();
    }

    @PostMapping
    public StudentRecord createStudent(@RequestBody @Valid StudentRecord studentRecord) {
        return studentService.createStudent(studentRecord);
    }

    @PutMapping("/{id}")
    public StudentRecord editStudent(@PathVariable long id,
                                     @RequestBody StudentRecord studentRecord) {
        return studentService.editStudent(id, studentRecord);
    }

    @DeleteMapping("/{id}")
    public StudentRecord deleteStudent(@PathVariable long id) {
        return studentService.deleteStudent(id);
    }
}
