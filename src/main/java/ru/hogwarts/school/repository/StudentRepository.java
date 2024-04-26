package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAgeBetween(Integer minAge, Integer maxAge);

    @Query(value = "select count(*) from student s", nativeQuery = true)
    Integer getQuantityAllStudents();

    @Query(value = "select AVG(age) from student s", nativeQuery = true)
    Integer getAverageAgeOfStudents();

    @Query(value = "select * from student s order by id desc  limit 5", nativeQuery = true)
    List<Student> getLatestFiveStudents();

}
