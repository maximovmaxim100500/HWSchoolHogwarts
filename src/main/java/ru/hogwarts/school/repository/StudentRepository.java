package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // Collection<Student> findByAge(int age);

    List<Student> findAllByAgeBetween(Integer minAge, Integer maxAge);

    @Query(value = "select count(*) from student s", nativeQuery = true)
    Integer getQuantityAllStudents();
}
