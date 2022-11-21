package ru.hogwarts.schoolapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.schoolapi.component.RecordMapper;
import ru.hogwarts.schoolapi.exception.StudentNotFoundException;
import ru.hogwarts.schoolapi.model.Faculty;
import ru.hogwarts.schoolapi.model.Student;
import ru.hogwarts.schoolapi.record.FacultyRecord;
import ru.hogwarts.schoolapi.record.StudentRecord;
import ru.hogwarts.schoolapi.repository.FacultyRepository;
import ru.hogwarts.schoolapi.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final RecordMapper recordMapper;

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository,
                          RecordMapper recordMapper) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.recordMapper = recordMapper;
    }

    public StudentRecord createStudent(StudentRecord studentRecord) {
        logger.info("Was invoked method for create student");
        Student student = recordMapper.toEntity(studentRecord);
        Faculty faculty = Optional.ofNullable(studentRecord.getFaculty())
                .map(FacultyRecord::getId)
                .flatMap(facultyRepository::findById)
                .orElse(null);
        student.setFaculty(faculty);
        return recordMapper.toRecord(studentRepository.save(student));
    }

    public StudentRecord findStudent(long id) {
        logger.info("Was invoked method for find student");
        return recordMapper.toRecord(studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            return new StudentNotFoundException(id);
        }));
    }

    public StudentRecord editStudent(long id, StudentRecord studentRecord) {
        logger.info("Was invoked method for edit student");
        Student oldStudent = studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            return new StudentNotFoundException(id);
        });
        oldStudent.setName(studentRecord.getName());
        oldStudent.setAge(studentRecord.getAge());
        oldStudent.setFaculty(
                Optional.ofNullable(studentRecord.getFaculty())
                        .map(FacultyRecord::getId)
                        .flatMap(facultyRepository::findById)
                        .orElse(null)
        );
        return recordMapper.toRecord(studentRepository.save(oldStudent));
    }

    public StudentRecord deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            return new StudentNotFoundException(id);
        });
        studentRepository.delete(student);
        return recordMapper.toRecord(student);
    }

    public List<StudentRecord> getStudentByAge(int age) {
        logger.info("Was invoked method for find students by age");
        return studentRepository.getStudentByAge(age).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public List<StudentRecord> getNamesStartsWithLetter(String letter) {
        logger.info("Was invoked method for get names starts with the letter");
        return studentRepository.findAll().stream()
                .filter(student -> student.getName().startsWith(letter))
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public List<StudentRecord> getAllStudent() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Integer countStudents() {
        logger.info("Was invoked method for count students");
        return studentRepository.countStudents();
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get average age");
        return studentRepository.getAverageAge();
    }

    public Double getAverageAgeStream() {
        logger.info("Was invoked method for get average age");
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(Double.NaN);
    }

    public List<StudentRecord> findFiveStudentWithMaxId() {
        logger.info("Was invoked method for find 5 students with max id");
        return studentRepository.findFiveStudentWithMaxId().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public List<StudentRecord> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for find students in range");
        return studentRepository.findByAgeBetween(minAge, maxAge).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public FacultyRecord getFacultyByStudentId(long id) {
        logger.info("Was invoked method for get faculty by student id");
        return findStudent(id).getFaculty();
    }

    public long parallelSum() {
        logger.info("Was invoked method for get parallel sum");
        long time = System.currentTimeMillis();
        long sum = LongStream.rangeClosed(1, 1_000_000).parallel().reduce(0L, Long::sum);
        logger.warn("The method was executed in: {} ms", (System.currentTimeMillis() - time));
        return sum;
    }

    public void printStudents() {
        logger.info("Was invoked method for get students async");
        List<Student> students = studentRepository.findAll(PageRequest.of(0, 6)).getContent();

        printStudents(students.subList(0, 2));
        new Thread(() -> printStudents(students.subList(2,4))).start();
        new Thread(() -> printStudents(students.subList(4,6))).start();
    }

    private void printStudents(List<Student> students) {
        for (Student student : students) {
            logger.info(student.getName());
        }
    }

    public void printStudentsSync() {
        logger.info("Was invoked method for print students sync");
        List<Student> students = studentRepository.findAll(PageRequest.of(0, 6)).getContent();

        printStudentsSync(students.subList(0, 2));
        new Thread(() -> printStudentsSync(students.subList(2,4))).start();
        new Thread(() -> printStudentsSync(students.subList(4,6))).start();
    }

    private synchronized void printStudentsSync(List<Student> students) {
        for (Student student : students) {
            logger.info(student.getName());
        }
    }
}
