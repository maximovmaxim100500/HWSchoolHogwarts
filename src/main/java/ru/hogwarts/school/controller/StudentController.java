package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student editStudent = studentService.editStudent(student);
        if (editStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ageBetween")
    public ResponseEntity<List<Student>> findAllByAgeBetween(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        List<Student> students = studentService.studentsByAgeBetween(minAge, maxAge);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/facultyOfStudent")
    public Faculty getFacultyOfStudent(@RequestParam long studentId) {
        return studentService.findStudent(studentId).getFaculty();
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        studentService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = studentService.findAvatar(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = studentService.findAvatar(id);
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping("/getQuantityAllStudents")
    public Integer getQuantityAllStudents() {
        return studentService.getQuantityAllStudents();
    }

    @GetMapping("/getAverageAgeOfStudents")
    public Integer getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudents();
    }

    @GetMapping("/getLatestFiveStudents")
    public List<Student> getLatestFiveStudents() {
        return studentService.getLatestFiveStudents();
    }

    @GetMapping("/getAllAvatarAtPage")
    public List<Avatar> getAllAvatarAtPage(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        List<Avatar> avatars = studentService.findAll(pageNumber, pageSize);
        return avatars;
    }

    @GetMapping("/getStudentsByStartingWithA")
    public List<String> getStudentsByStartingWithA() {
        return studentService.getStudentsByStartingWithA();
    }

    @GetMapping("/getAverageAgeOfAllStudents")
    public double getAverageAgeOfAllStudents() {
        return studentService.getAverageAgeOfAllStudents();
    }

    @GetMapping("/print-parallel")
    public void printNamesStudent() {
        List<Student> allStudents = studentRepository.findAll();
        System.out.println(allStudents.get(0).getName());
        System.out.println(allStudents.get(1).getName());

        new Thread(() -> {
            System.out.println(allStudents.get(2).getName());
            System.out.println(allStudents.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println(allStudents.get(4).getName());
            System.out.println(allStudents.get(5).getName());
        }).start();
    }

    @GetMapping("/print-synchronized")
    public void printNamesStudentSynchronized() {

        List<Student> allStudents = studentRepository.findAll();
        studentService.printStudentName(allStudents.get(0));
        studentService.printStudentName(allStudents.get(1));

        new Thread(() -> {
            studentService.printStudentName(allStudents.get(2));
            studentService.printStudentName(allStudents.get(3));
        }).start();

        new Thread(() -> {
            studentService.printStudentName(allStudents.get(4));
            studentService.printStudentName(allStudents.get(5));
        }).start();
    }


}
