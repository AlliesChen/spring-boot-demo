package com.example.demo.student;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService
{
    private final StudentRepository studentRepository;
    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new BadRequestException("Email already taken");
        }
        return studentRepository.save(student);
    }


    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new ResourceNotFoundException("Student with id " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public Student updateStudent(Long studentId, String name, String email) {
        System.out.println(studentId);
        System.out.println(name);
        System.out.println(email);
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "student with id " + studentId + " does not exist"
                )
        );
        if (name != null
                && !name.isEmpty()
                && !Objects.equals(student.getName(), name)
        ) {
            student.setName(name);
        }

        if (email != null
                && !email.isEmpty()
                && !Objects.equals(student.getEmail(), email)
        ) {
            Optional<Student> studentOptional = studentRepository
                    .findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new BadRequestException("Email already taken");
            }
            student.setEmail(email);
        }
        return studentRepository.save(student);
    }
}
