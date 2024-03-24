package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long incrementId = 0;

    public Student createStudent(Student student) {
        student.setId(++incrementId);
        students.put(incrementId, student);
        return student;
    }

    public Student findStudent(long id) {
        return students.get(id);
    }

    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            students.put(student.getId(), student);
            return student;
        }
        return null;
    }

    public Student removeStudent(long id) {
        return students.remove(id);
    }

    public Collection<Student> studentsByAge(int age) {
        return students.values().stream()
                .filter(s -> s.getAge() == age)
                .toList();
    }
}
