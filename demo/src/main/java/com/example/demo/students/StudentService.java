package com.example.demo.students;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
            return studentRepository.findAll();
    }


    public void addNewStudent(Student student) {
       Optional<Student> studentEmail=
               studentRepository.findStudentByEmail(student.getEmail());
        if (studentEmail.isPresent()) {
            throw  new IllegalStateException("email taken ");
        }
        studentRepository.save(student);
        }

    public void deleteStudent(Long id) {
       boolean exist= studentRepository.existsById(id);
        if (!exist) {
            throw new IllegalStateException("not found student");
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public void updateStudent(Long studentId, String email, String name) {

       Student student= studentRepository.findById(studentId).orElseThrow(
                ()->new IllegalStateException("student does not exist"));

        if (name != null&& !name.isEmpty() && !Objects.equals(student.getName(),name)) {
            student.setName(name);
        }

        if (email != null && !email.isEmpty() && !Objects.equals(student.getEmail(),email)) {
          Optional<Student> studentOptional= studentRepository.findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }


    }
}
