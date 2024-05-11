package ru.hogwarts.school.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceptions.RecordNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class StudentService {

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    private final StudentRepository studentRepository;

    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student createStudent(Student student) {
        logger.info("Был вызван метод createStudent");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Был вызван метод findStudent");
        try {
            return studentRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        } catch (RecordNotFoundException e) {
            logger.error("Нет студента с id = " + id);
            throw e;
        }
    }

    public Student editStudent(Student student) {
        logger.info("Был вызван метод editStudent");
        return studentRepository.save(student);
    }

    public void removeStudent(long id) {
        logger.info("Был вызван метод removeStudent");
        studentRepository.deleteById(id);
    }

    public List<Student> studentsByAgeBetween(Integer minAge, Integer maxAge) {
        logger.info("Был вызван метод studentsByAgeBetween");
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    public Avatar findAvatar(long studentId) {
        logger.info("Был вызван метод findAvatar");
        try {
            return avatarRepository.findByStudentId(studentId).orElseThrow();
        } catch (Exception e) {
            logger.error("Не найдена аватарка со studentId = " + studentId);
            throw e;
        }
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Был вызван метод uploadAvatar");
        try {
            Student student = findStudent(studentId);

            Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);

            try (InputStream is = file.getInputStream();
                 OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedInputStream bis = new BufferedInputStream(is, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            ) {
                bis.transferTo(bos);
            }

            Avatar avatar = avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
            avatar.setStudent(student);
            avatar.setFilePath(filePath.toString());
            avatar.setFileSize(file.getSize());
            avatar.setMediaType(file.getContentType());
            avatar.setData(file.getBytes());
            avatarRepository.save(avatar);
        } catch (Exception e) {
            logger.error("Ошибка при загрузке аватара для студента с studentId = " + studentId);
            throw e;
        }
    }

    private String getExtension(String fileName) {
        logger.info("Был вызван метод getExtension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public List<Avatar> findAll(Integer pageNumber, Integer pageSize) {
        logger.info("Был вызван метод findAll");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }

    public Integer getQuantityAllStudents() {
        logger.info("Был вызван метод getQuantityAllStudents");
        return studentRepository.getQuantityAllStudents();
    }

    public Integer getAverageAgeOfStudents() {
        logger.info("Был вызван метод getAverageAgeOfStudents");
        return studentRepository.getAverageAgeOfStudents();
    }

    public List<Student> getLatestFiveStudents() {
        logger.info("Был вызван метод getLatestFiveStudents");
        return studentRepository.getLatestFiveStudents();
    }

    public List<String> getStudentsByStartingWithA() {
        logger.info("Был вызван метод getStudentsByStartingWithA");
        List<Student> allStudents = studentRepository.findAll();
        List<String> studentsNamesByStartingWithA = allStudents.stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
        return studentsNamesByStartingWithA;
    }

    public double getAverageAgeOfAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
        double averageAge = allStudents.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
        return averageAge;
    }

    public void printStudentsNamesParallel() {
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

    public void printStudentsNamesSync() {
        List<Student> allStudents = studentRepository.findAll();
        printStudentName(allStudents.get(0));
        printStudentName(allStudents.get(1));

        new Thread(() -> {
            printStudentName(allStudents.get(2));
            printStudentName(allStudents.get(3));
        }).start();

        new Thread(() -> {
            printStudentName(allStudents.get(4));
            printStudentName(allStudents.get(5));
        }).start();
    }

    public synchronized void printStudentName(Student student) {
        System.out.println(student.getName());
    }
}
