package ru.hogwarts.schoolapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.schoolapi.component.RecordMapper;
import ru.hogwarts.schoolapi.controller.FacultyController;
import ru.hogwarts.schoolapi.model.Faculty;
import ru.hogwarts.schoolapi.record.FacultyRecord;
import ru.hogwarts.schoolapi.repository.FacultyRepository;
import ru.hogwarts.schoolapi.service.FacultyService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FacultyController.class)
@ExtendWith(MockitoExtension.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // для сириализации/десириализации

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private RecordMapper recordMapper;

    @Test
    public void createTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("Green");
        faculty.setName("Gryffindor");

        when(facultyRepository.save(any())).thenReturn(faculty);

        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setColor("Green");
        facultyRecord.setName("Gryffindor");

        mockMvc.perform(MockMvcRequestBuilders.post("/faculty").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(facultyRecord))).andExpect(result -> {
            MockHttpServletResponse mockHttpServletResponse = result.getResponse();
            FacultyRecord facultyRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), FacultyRecord.class);
            assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(facultyRecordResult).isNotNull();
            assertThat(facultyRecordResult).usingRecursiveComparison().ignoringFields("id").isEqualTo(facultyRecord);
            assertThat(facultyRecordResult.getId()).isEqualTo(faculty.getId());
        });

    }

    @Test
    public void getFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("Green");
        faculty.setName("Gryffindor");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setColor("Green");
        facultyRecord.setName("Gryffindor");

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/{id}", faculty.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(facultyRecord))).andExpect(result -> {
            MockHttpServletResponse mockHttpServletResponse = result.getResponse();
            FacultyRecord facultyRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), FacultyRecord.class);
            assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(facultyRecordResult).isNotNull();
            assertThat(facultyRecordResult).usingRecursiveComparison().ignoringFields("id").isEqualTo(facultyRecord);
            assertThat(facultyRecordResult.getId()).isEqualTo(faculty.getId());
        });
    }

    @Test
    public void editFacultyTest() throws Exception {
        Faculty facultyToEdit = new Faculty();
        facultyToEdit.setId(1L);
        facultyToEdit.setColor("Green");
        facultyToEdit.setName("Gryffindor");

        Faculty facultyToSave = new Faculty();
        facultyToSave.setId(1L);
        facultyToSave.setColor("Red");
        facultyToSave.setName("Slytherin");

        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setColor("Red");
        facultyRecord.setName("Slytherin");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(facultyToEdit));
        when(facultyRepository.save(any())).thenReturn(facultyToSave);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty/{id}", facultyToEdit.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(facultyRecord))).andExpect(result -> {
            MockHttpServletResponse mockHttpServletResponse = result.getResponse();
            FacultyRecord facultyRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), FacultyRecord.class);
            assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(facultyRecordResult).isNotNull();
            assertThat(facultyRecordResult).usingRecursiveComparison().ignoringFields("id").isEqualTo(facultyRecord);
            assertThat(facultyRecordResult.getId()).isEqualTo(facultyToEdit.getId());
        });
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("Green");
        faculty.setName("Gryffindor");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setColor("Green");
        facultyRecord.setName("Gryffindor");


        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/{id}", faculty.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(facultyRecord))).andExpect(result -> {
            MockHttpServletResponse mockHttpServletResponse = result.getResponse();
            FacultyRecord facultyRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), FacultyRecord.class);
            assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(facultyRecordResult).isNotNull();
            assertThat(facultyRecordResult).usingRecursiveComparison().ignoringFields("id").isEqualTo(facultyRecord);
            assertThat(facultyRecordResult.getId()).isEqualTo(faculty.getId());
        });
    }

    //FIXME Fix test getFacultyByNameOrColor()
    @Test
    public void getFacultyByNameOrColor() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("Green");
        faculty.setName("Gryffindor");

        when(facultyRepository.getFacultyByNameContainsIgnoreCaseOrColorContainsIgnoreCase(any(), any())).thenReturn(faculty);

        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setColor("Green");
        facultyRecord.setName("Gryffindor");

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/by-name-or-color")
                .param("color", faculty.getColor())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facultyRecord)))
                .andExpect(result -> {
            MockHttpServletResponse mockHttpServletResponse = result.getResponse();
            FacultyRecord facultyRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), FacultyRecord.class);
            assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(facultyRecordResult).isNotNull();
            assertThat(facultyRecordResult).usingRecursiveComparison().ignoringFields("id").isEqualTo(facultyRecord);
            assertThat(facultyRecordResult.getId()).isEqualTo(faculty.getId());
        });
    }

    //FIXME Fix test findFacultyByColorTest()
    @Test
    public void findFacultyByColorTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("Green");
        faculty.setName("Gryffindor");

        List<Faculty> greenFaculty = new ArrayList<>();
        greenFaculty.add(faculty);

        when(facultyRepository.getFacultyByColor(any())).thenReturn(greenFaculty);

        FacultyRecord facultyRecord1 = new FacultyRecord();
        facultyRecord1.setColor("Green");
        facultyRecord1.setName("Gryffindor");

        List<FacultyRecord> facultyRecordList = new ArrayList<>();
        facultyRecordList.add(facultyRecord1);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/faculty-by-color")
                        .param("colorOrName", faculty.getColor())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facultyRecordList)))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    List<FacultyRecord> facultyRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {});
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(facultyRecordResult).isNotNull();
                    assertThat(facultyRecordResult).usingRecursiveComparison().ignoringFields("id").isEqualTo(faculty);
                });
    }

    //FIXME How to add tests for FacultyController with List??
}
