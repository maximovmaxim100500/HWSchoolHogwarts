package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class FacultyControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private AvatarRepository avatarRepository;
    @SpyBean
    private FacultyService facultyService;
    @SpyBean
    private StudentService studentService;
    @InjectMocks
    private FacultyController facultyController;
    @InjectMocks
    private StudentController studentController;

    private TestRestTemplate template;

    @Test
    public void getFacultyInfoTest() throws Exception {
        final Long id = 1L;
        final String name = "IT";
        final String color = "red";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].color").value(color));

    }

    @Test
    public void createFacultyTest() throws Exception {
        final Long id = 1L;
        final String name = "IT";
        final String color = "red";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void editFacultyTest() throws Exception {
        final Long id = 1L;
        final String name = "IT";
        final String color = "red";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        final Long id = 1L;

        Faculty faculty = new Faculty();
        faculty.setId(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getFacultyByColorTest() throws Exception {
        final Long id = 1L;
        final String name = "IT";
        final String color = "red";

        List<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty(id, name, color));

        when(facultyRepository.findByColorIgnoreCase(eq(color))).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/facultyByColor?color=" + color))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].color").value(color));
    }

    @Test
    void findFacultyByNameOrColorTest() throws Exception {
        final Long id = 1L;
        final String name = "IT";
        final String color = "red";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findFacultyByNameIgnoreCase(eq(name))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/facultyByNameOrColor?name=" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void getStudentsOfFacultyTest() throws Exception {
        var f = new Faculty(null, "IT", "red");
        var f1 = template.postForEntity("/faculty", f, Faculty.class).getBody();

        Student newStudent = new Student(null, "s1", 29);
        Student newStudent2 = new Student(null, "s2", 39);
        newStudent.setFaculty(f1);
        newStudent2.setFaculty(f1);
        var s1 = template.postForEntity("/student", newStudent, Student.class).getBody();
        var s2 = template.postForEntity("/student", newStudent2, Student.class).getBody();

        ResponseEntity<List<Student>> result = template.exchange("/faculty/studentsOfFaculty?facultyId=" + f1.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                });
        assertThat(result.getBody()).containsExactlyInAnyOrder(
                new Student(1L, "s1", 29),
                new Student(2L, "s2", 39)
        );
    }
}
