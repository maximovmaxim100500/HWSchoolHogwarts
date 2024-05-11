package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.RecordNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {

    @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Был вызван метод createFaculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("Был вызван метод findFaculty");
        try {
            return facultyRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        } catch (Exception e) {
            logger.error("Не найден факультет с id = " + id);
            throw e;
        }
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Был вызван метод editFaculty");
        return facultyRepository.save(faculty);
    }

    public void removeFaculty(long id) {
        logger.info("Был вызван метод removeFaculty");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> facultyByColor(String color) {
        logger.info("Был вызван метод facultyByColor");
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Faculty facultyByName(String color) {
        logger.info("Был вызван метод facultyByName");
        return facultyRepository.findFacultyByNameIgnoreCase(color);
    }
}
