package fr.takima.training.sampleapplication.controller;

import fr.takima.training.sampleapplication.entity.Student;
import fr.takima.training.sampleapplication.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/students")
public class StudentController {
    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getStudentById(@PathVariable(name="id") long id) {
        Optional<Student> studentOptional = Optional.ofNullable(this.studentService.getStudentById(id));
        if (!studentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(studentOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> addStudent(@RequestBody Student student) {
        Student savedStudent;
        try {
            savedStudent = this.studentService.addStudent(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedStudent.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable(name="id") long id) {
        Optional<Student> studentOptional = Optional.ofNullable(studentService.getStudentById(id));
        if (!studentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        student.setId(id);
        this.studentService.addStudent(student);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> removeStudent(@PathVariable(name="id") long id) {
        Optional<Student> studentOptional = Optional.ofNullable(studentService.getStudentById(id));
        if (!studentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        this.studentService.removeStudentById(id);

        return ResponseEntity.ok().build();
    }
}
