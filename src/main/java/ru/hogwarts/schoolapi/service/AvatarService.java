package ru.hogwarts.schoolapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.schoolapi.component.RecordMapper;
import ru.hogwarts.schoolapi.exception.AvatarNotFoundException;
import ru.hogwarts.schoolapi.exception.StudentNotFoundException;
import ru.hogwarts.schoolapi.model.Avatar;
import ru.hogwarts.schoolapi.model.Student;
import ru.hogwarts.schoolapi.record.AvatarRecord;
import ru.hogwarts.schoolapi.repository.AvatarRepository;
import ru.hogwarts.schoolapi.repository.StudentRepository;


import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AvatarService {

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    private final RecordMapper recordMapper;

    private final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${avatar.dir.path}")
    private String avatarsDir;

    public AvatarService(StudentRepository studentRepository,
                         AvatarRepository avatarRepository,
                         RecordMapper recordMapper) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
        this.recordMapper = recordMapper;
    }

    public AvatarRecord uploadAvatar(long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for upload avatar");
        Student student = studentRepository.findById(studentId).orElseThrow(() -> {
            logger.error("There is not student with id = " + studentId);
            return new StudentNotFoundException(studentId);
        });
        byte[] data = avatarFile.getBytes();

        String extension = Optional.ofNullable(avatarFile.getOriginalFilename()).map(fileName -> fileName.substring(avatarFile.getOriginalFilename().lastIndexOf('.')))
                .orElse("");
        Path path = Paths.get(avatarsDir).resolve(studentId + extension);
        Files.write(path, data);

        Avatar avatar = new Avatar();
        avatar.setData(data);
        avatar.setFileSize(data.length);
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setStudent(student);
        avatar.setFilePath(path.toString());

        return recordMapper.toRecord(avatarRepository.save(avatar));
    }

    public Pair<byte[], String> downloadAvatarFromFs(long id) throws IOException {
        logger.info("Was invoked method for download avatar from file system");
        Avatar avatar = avatarRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not avatar with id = " + id);
            return new AvatarNotFoundException(id);
        });
        return Pair.of(Files.readAllBytes(Paths.get(avatar.getFilePath())), avatar.getMediaType());
    }

    public Pair<byte[], String> downloadAvatarFromDb(long id) throws IOException {
        logger.info("Was invoked method for download avatar from database");
        Avatar avatar = avatarRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not avatar with id = " + id);
            return new AvatarNotFoundException(id);
        });
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public List<AvatarRecord> getAvatarsByPage(Integer pageNumber, Integer pageSize) {
        logger.info("Was invoked method for get avatars by page");
        Pageable pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }



//    private String getExtensions(String fileName) {
//        return fileName.substring(fileName.lastIndexOf(".") + 1);
//    }
//
//    public Avatar findAvatar(long id) {
//        return avatarRepository.findByStudentId(id).orElse(new Avatar());
//    }


//    public void uploadAvatar(long studentId, MultipartFile avatarFile) throws IOException {
//        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));
//        Path filePath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
//        Files.createDirectories(filePath.getParent());
//        Files.deleteIfExists(filePath);
//
//        try (
//                InputStream is = avatarFile.getInputStream();
//                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
//                BufferedInputStream bis = new BufferedInputStream(is, 1024);
//                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
//        ) {
//            bis.transferTo(bos);
//        }
//
//        Avatar avatar = findAvatar(studentId);
//        avatar.setStudent(student);
//        avatar.setFilePath(filePath.toString());
//        avatar.setFileSize(avatarFile.getSize());
//        avatar.setMediaType(avatarFile.getContentType());
//        avatar.setData(avatarFile.getBytes());
//        avatarRepository.save(avatar);
//    }
}
