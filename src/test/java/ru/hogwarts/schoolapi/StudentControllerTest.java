package ru.hogwarts.schoolapi;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.schoolapi.record.FacultyRecord;
import ru.hogwarts.schoolapi.record.StudentRecord;
import ru.hogwarts.schoolapi.repository.FacultyRepository;
import ru.hogwarts.schoolapi.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private final Faker faker = new Faker();

    @AfterEach
    public void afterEach() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    public void createStudentTest() {
        addStudent(generateStudent(addFaculty(generateFaculty())));
    }

    private FacultyRecord addFaculty(FacultyRecord facultyRecord) {
        ResponseEntity<FacultyRecord> facultyRecordResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/faculty", facultyRecord, FacultyRecord.class);
        assertThat(facultyRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyRecordResponseEntity.getBody()).isNotNull();
        assertThat(facultyRecordResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(facultyRecord);
        assertThat(facultyRecordResponseEntity.getBody().getId()).isNotNull();

        return facultyRecordResponseEntity.getBody();
    }

    private StudentRecord addStudent(StudentRecord studentRecord) {
        ResponseEntity<StudentRecord> studentRecordResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/student", studentRecord, StudentRecord.class);
        assertThat(studentRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentRecordResponseEntity.getBody()).isNotNull();
        assertThat(studentRecordResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(studentRecord);
        assertThat(studentRecordResponseEntity.getBody().getId()).isNotNull();

        return studentRecordResponseEntity.getBody();
    }

    @Test
    public void findByAgeBetweenTest() {
        List<FacultyRecord> facultyRecords = Stream.generate(this::generateFaculty)
                .limit(5)
                .map(this::addFaculty)
                .collect(Collectors.toList());

        List<StudentRecord> studentRecords = Stream.generate(() -> generateStudent(facultyRecords.get(faker.random().nextInt(facultyRecords.size()))))
                .limit(30)
                .map(this::addStudent)
                .collect(Collectors.toList());

        int minAge = 14;
        int maxAge = 17;

        List<StudentRecord> expectedStudents = studentRecords.stream()
                .filter(studentRecord -> studentRecord.getAge() >= minAge && studentRecord.getAge() <= maxAge)
                .collect(Collectors.toList());

        ResponseEntity<List<StudentRecord>> getForEntityResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/student-by-range?minAge={minAge}&maxAge={maxAge}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                },
                minAge,
                maxAge
        );
        assertThat(getForEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getForEntityResponse.getBody())
                .hasSize(expectedStudents.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedStudents);
    }

    @Test
    public void getStudentTest() {
        StudentRecord expectedStudent = addStudent(generateStudent(addFaculty(generateFaculty())));

        ResponseEntity<StudentRecord> studentRecord = testRestTemplate.getForEntity("http://localhost:" + port + "/student/" + expectedStudent.getId(), StudentRecord.class);
        assertThat(studentRecord.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentRecord.getBody()).usingRecursiveComparison().isEqualTo(expectedStudent);
    }

    @Test
    public void findByAgeTest() {
        List<FacultyRecord> facultyRecords = Stream.generate(this::generateFaculty)
                .limit(5)
                .map(this::addFaculty)
                .collect(Collectors.toList());

        List<StudentRecord> studentRecords = Stream.generate(() -> generateStudent(facultyRecords.get(faker.random().nextInt(facultyRecords.size()))))
                .limit(50)
                .map(this::addStudent)
                .collect(Collectors.toList());

        int age = 19;

        List<StudentRecord> expectedStudents = studentRecords.stream()
                .filter(studentRecord -> studentRecord.getAge() == age)
                .collect(Collectors.toList());

        ResponseEntity<List<StudentRecord>> getForEntityResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/student-by-age?age={age}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                },
                age
        );
        assertThat(getForEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getForEntityResponse.getBody())
                .hasSize(expectedStudents.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedStudents);
    }

    @Test
    public void getFacultyByStudentIdTest() {
        StudentRecord expectedStudent = addStudent(generateStudent(addFaculty(generateFaculty())));
        long id = expectedStudent.getId();
        FacultyRecord expectedFaculty = expectedStudent.getFaculty();

        ResponseEntity<FacultyRecord> facultyRecord = testRestTemplate.getForEntity("http://localhost:" + port + "/student/{id}/faculty", FacultyRecord.class, id);

        assertThat(facultyRecord.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyRecord.getBody()).usingRecursiveComparison().isEqualTo(expectedFaculty);
    }

    @Test
    public void getAllStudentsTest() {
        List<FacultyRecord> facultyRecords = Stream.generate(this::generateFaculty)
                .limit(5)
                .map(this::addFaculty)
                .collect(Collectors.toList());

        List<StudentRecord> studentRecords = Stream.generate(() -> generateStudent(facultyRecords.get(faker.random().nextInt(facultyRecords.size()))))
                .limit(30)
                .map(this::addStudent)
                .collect(Collectors.toList());

        ResponseEntity<List<StudentRecord>> getForEntityResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(getForEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getForEntityResponse.getBody())
                .hasSize(studentRecords.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(studentRecords);
    }

    @Test
    public void editStudentTest() {
        FacultyRecord facultyRecord1 = addFaculty(generateFaculty());
        FacultyRecord facultyRecord2 = addFaculty(generateFaculty());
        StudentRecord studentRecord = addStudent(generateStudent(facultyRecord1));

        ResponseEntity<StudentRecord> getForEntityResponse = testRestTemplate.getForEntity("http://localhost:" + port + "/student/{id}", StudentRecord.class, studentRecord.getId());
        assertThat(getForEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getForEntityResponse.getBody()).isNotNull();
        assertThat(getForEntityResponse.getBody()).usingRecursiveComparison().isEqualTo(studentRecord);
        assertThat(getForEntityResponse.getBody().getFaculty()).usingRecursiveComparison().isEqualTo(facultyRecord1);

        studentRecord.setFaculty(facultyRecord2);

        ResponseEntity<StudentRecord> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/" + studentRecord.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(studentRecord),
                StudentRecord.class
        );
        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).isNotNull();
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(studentRecord);
        assertThat(recordResponseEntity.getBody().getFaculty()).usingRecursiveComparison().isEqualTo(facultyRecord2);
    }

    @Test
    public void deleteStudentTest() {
        FacultyRecord facultyRecord1 = addFaculty(generateFaculty());
        StudentRecord studentRecord1 = addStudent(generateStudent(facultyRecord1));

        ResponseEntity<StudentRecord> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/" + studentRecord1.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                StudentRecord.class
        );

        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(studentRecord1);
    }

    private StudentRecord generateStudent(FacultyRecord facultyRecord) {
        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setName(faker.harryPotter().character());
        studentRecord.setAge(faker.random().nextInt(17, 33));
        if (facultyRecord != null) {
            studentRecord.setFaculty(facultyRecord);
        }
        return studentRecord;
    }

    private FacultyRecord generateFaculty() {
        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setName(faker.harryPotter().house());
        facultyRecord.setColor(faker.color().name());
        return facultyRecord;
    }
}
