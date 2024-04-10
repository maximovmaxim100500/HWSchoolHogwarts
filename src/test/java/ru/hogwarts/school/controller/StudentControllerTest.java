package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import ru.hogwarts.school.model.Student;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoadsStudent() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void getStudentInfo() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/2", String.class))
                .isNotNull();
    }

    @Test
    void createStudent() throws Exception {
        Student student = new Student();
        student.setName("Karl");
        student.setAge(40);
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
                .isNotNull();

    }

    @Test
    void editStudent() throws Exception {
        Student student = new Student();
        student.setId(202L);
        student.setName("Karl");
        student.setAge(40);
        ResponseEntity<Student> responseEntity = restTemplate.exchange("/student", HttpMethod.PUT,
                new HttpEntity<>(student), Student.class);
        Assertions.
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student editedStudent = responseEntity.getBody();
        Assertions.
                assertThat(editedStudent).isEqualTo(student);
    }

    @Test
    void deleteStudent() throws Exception {
        Long id = 252L;
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/student/{id}",
                HttpMethod.DELETE, null, Void.class, id);
        Assertions.
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findAllByAgeBetween() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/ageBetween?minAge=20&maxAge=30", String.class))
                .isNotNull();
    }

    @Test
    void getFacultyOfStudent() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/facultyOfStudent?studentId=2", String.class))
                .isNotNull();
    }

    @Test
    void uploadAvatar() throws Exception {
        byte[] avatar = new byte[1024];
        long id = 1;
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.put("avatar", Collections.singletonList(new ByteArrayResource(avatar)));
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                RequestEntity.post("/{id}/avatar", id)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(body), String.class);
        assertNotNull(responseEntity);
    }

    @Test
    void downloadAvatarPreview() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/2/avatar/preview", String.class))
                .isNotNull();
    }

    @Test
    void DownloadAvatar() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/facultyOfStudent?studentId=2", String.class))
                .isNotNull();
        //
    }
}