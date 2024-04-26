package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoadsFaculty() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void getFacultyInfo() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/1", String.class))
                .isNotNull();
    }

    @Test
    void createFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("facultyTest");
        faculty.setColor("facultyColorTest");
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, String.class))
                .isNotNull();
    }

    @Test
    void editFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("facultyTest");
        faculty.setColor("facultyColorTest");
        faculty.setId(53L);
        ResponseEntity<Faculty> responseEntity = restTemplate.exchange("/faculty", HttpMethod.PUT,
                new HttpEntity<>(faculty), Faculty.class);
        Assertions.
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty editFaculty = responseEntity.getBody();
        Assertions.
                assertThat(editFaculty).isEqualTo(faculty);
    }

    @Test
    void deleteFaculty() throws Exception {
        Long id = 102L;
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/faculty/{id}",
                HttpMethod.DELETE, null, Void.class, id);
        Assertions.
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getFacultyByColor() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/facultyByColor?color=red", String.class))
                .isNotNull();
    }

    @Test
    void findFacultyByNameOrColor() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/facultyByNameOrColor?name=IT", String.class))
                .isNotNull();
    }

    @Test
    void getStudentsOfFaculty() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/studentsOfFaculty?facultyId=1", String.class))
                .isNotNull();
    }
}