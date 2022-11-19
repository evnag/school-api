package ru.hogwarts.schoolapi.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.schoolapi.record.FacultyRecord;
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

    @GetMapping("/name-starts-with-letter")
    public List<StudentRecord> getNamesStartsWithLetter(@RequestParam String letter) {
        return studentService.getNamesStartsWithLetter(letter);
    }

    @GetMapping
    public List<StudentRecord> getAllStudents() {
        return studentService.getAllStudent();
    }

    @GetMapping("/amount")
    public Integer countStudents() {
        return studentService.countStudents();
    }

    @GetMapping("/average-age")
    public Double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/average-age-stream")
    public Double getAverageAgeStream() {
        return studentService.getAverageAgeStream();
    }

    @GetMapping("/max-id-limit")
    public List<StudentRecord> findFiveStudentWithMaxId() {
        return studentService.findFiveStudentWithMaxId();
    }

    @GetMapping("/student-by-range")
    public List<StudentRecord> findByAgeBetween(@RequestParam int minAge,
                                                @RequestParam int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public FacultyRecord getFacultyByStudentId(@PathVariable long id) {
        return studentService.getFacultyByStudentId(id);
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

    @GetMapping("/parallelSum")
    public int parallelSum() {
        return studentService.parallelSum();
    }
}
