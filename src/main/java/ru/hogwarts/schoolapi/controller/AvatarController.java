package ru.hogwarts.schoolapi.controller;

import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.schoolapi.record.AvatarRecord;
import ru.hogwarts.schoolapi.service.AvatarService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AvatarRecord uploadAvatar(@PathVariable long studentId,
                                     @RequestParam MultipartFile avatar) throws IOException {
        return avatarService.uploadAvatar(studentId, avatar);
    }


    @GetMapping(value = "/{id}/avatar-from-file")
    public ResponseEntity<byte[]> downloadAvatarFromFs(@PathVariable long id) throws IOException {
        Pair<byte[], String> pair = avatarService.downloadAvatarFromFs(id);
        return read(pair);
    }

    @GetMapping(value = "/{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatarFromDb(@PathVariable long id) throws IOException {
        Pair<byte[], String> pair = avatarService.downloadAvatarFromDb(id);
        return read(pair);
    }

    @GetMapping("/page")
    public ResponseEntity<List<AvatarRecord>> getAvatarsByPage(@RequestParam Integer pageNumber,
                                               @RequestParam Integer pageSize) {
        List<AvatarRecord> listByPage = avatarService.getAvatarsByPage(pageNumber, pageSize);

        return ResponseEntity.ok(listByPage);
    }

    private ResponseEntity<byte[]> read(Pair<byte[], String> pair) {
        return ResponseEntity.ok()
                .contentLength(pair.getFirst().length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(pair.getFirst());
    }


//    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> uploadAvatar(@PathVariable long studentId,
//                                               @RequestParam MultipartFile avatar) throws IOException {
//        avatarService.uploadAvatar(studentId, avatar);
//        return ResponseEntity.ok().build();
//    }
//
//
//    @GetMapping(value = "/{id}/avatar-from-file")
//    public void downloadAvatar(@PathVariable long id, HttpServletResponse response) throws IOException {
//        Avatar avatar = avatarService.findAvatar(id);
//        Path path = Path.of(avatar.getFilePath());
//
//        try (InputStream is = Files.newInputStream(path);
//             OutputStream os = response.getOutputStream()) {
//            response.setStatus(200);
//            response.setContentType(avatar.getMediaType());
//            response.setContentLength((int) avatar.getFileSize());
//            is.transferTo(os);
//        }
//    }

//    @GetMapping(value = "/{id}/avatar-from-db")
//    public ResponseEntity<byte[]> downloadAvatar(@PathVariable long id) {
//        Avatar avatar = avatarService.findAvatar(id);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.parseMediaType((avatar.getMediaType())));
//        httpHeaders.setContentLength(avatar.getData().length);
//        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(avatar.getData());
//    }
}
