package ru.hogwarts.schoolapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.schoolapi.component.RecordMapper;
import ru.hogwarts.schoolapi.exception.FacultyNotFoundException;
import ru.hogwarts.schoolapi.model.Faculty;
import ru.hogwarts.schoolapi.record.FacultyRecord;
import ru.hogwarts.schoolapi.record.StudentRecord;
import ru.hogwarts.schoolapi.repository.FacultyRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final RecordMapper recordMapper;

    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);


    public FacultyService(FacultyRepository facultyRepository,
                          RecordMapper recordMapper) {
        this.facultyRepository = facultyRepository;
        this.recordMapper = recordMapper;
    }

    public FacultyRecord createFaculty(FacultyRecord facultyRecord) {
        logger.info("Was invoked method for create faculty");
        return recordMapper.toRecord(facultyRepository.save(recordMapper.toEntity(facultyRecord)));
    }

    public FacultyRecord findFaculty(long id) {
        logger.info("Was invoked method for find faculty by id");
        return recordMapper.toRecord(facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return  new FacultyNotFoundException(id);
        }));
    }

    public FacultyRecord findFacultyWithLongestName() {
        logger.info("Was invoked method for find faculty with longest name");
        return recordMapper.toRecord(Objects.requireNonNull(facultyRepository.findAll().stream()
                .max(Comparator.comparingInt(faculty -> faculty.getName().length()))
                .orElse(null)));
    }

    public FacultyRecord editFaculty(long id, FacultyRecord facultyRecord) {
        logger.info("Was invoked method for edit faculty");
        Faculty oldFaculty = facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return  new FacultyNotFoundException(id);
        });
        oldFaculty.setName(facultyRecord.getName());
        oldFaculty.setColor(facultyRecord.getColor());
        return recordMapper.toRecord(facultyRepository.save(oldFaculty));
    }

    public FacultyRecord deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty");
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return  new FacultyNotFoundException(id);
        });
        facultyRepository.delete(faculty);
        return recordMapper.toRecord(faculty);
    }

    public List<FacultyRecord> getFacultyByColor(String color) {
        logger.info("Was invoked method for find faculty by color");
        return facultyRepository.getFacultyByColor(color).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public List<FacultyRecord> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public FacultyRecord getFacultyByNameContainsIgnoreCaseOrColorContainsIgnoreCase(String nameOrColor) {
        logger.info("Was invoked method for find faculty by name or color");
        return recordMapper.toRecord(facultyRepository.getFacultyByNameContainsIgnoreCaseOrColorContainsIgnoreCase(nameOrColor, nameOrColor));
    }

    public List<StudentRecord> getStudentsByFacultyId(long id) {
        logger.info("Was invoked method for get students by faculty id");
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
        return faculty.getStudents().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }
}
