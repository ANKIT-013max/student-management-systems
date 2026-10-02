package com.example.sms.controller;

import com.example.sms.dto.StudentPatchRequestDto;
import com.example.sms.dto.StudentRequestDto;
import com.example.sms.dto.StudentResponseDto;
import com.example.sms.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping("/create")
    public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentRequestDto studentRequestDto) {

        log.info("Received request to create student");

        StudentResponseDto response = studentService.createStudent(studentRequestDto);

        log.info("Student created successfully with id: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {

        log.debug("Received request to fetch all students");

        List<StudentResponseDto> students = studentService.getAllStudents();

        log.info("Returning {} students", students.size());

        return ResponseEntity.ok(students);
    }


    @GetMapping("get/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {

        log.debug("Received request to fetch student with id: {}", id);

        StudentResponseDto response = studentService.getStudentById(id);

        log.info("Student fetched successfully with id: {}", id);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequestDto studentRequestDto) {

        log.info("Received request to update student with id: {}", id);

        StudentResponseDto response = studentService.updateStudent(id, studentRequestDto);

        log.info("Student updated successfully with id: {}", id);

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/patch/{id}")
    public ResponseEntity<StudentResponseDto> patchStudent(@PathVariable Long id, @Valid @RequestBody StudentPatchRequestDto studentPatchRequestDto) {

        log.info("Received request to partially update student with id: {}", id);

        StudentResponseDto response = studentService.patchStudent(id, studentPatchRequestDto);

        log.info("Student partially updated successfully with id: {}", id);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void>  deleteStudent(@PathVariable Long id) {

        log.info("Received request to delete student with id: {}", id);

        studentService.deleteStudent(id);

        log.info("Student deleted successfully with id: {}", id);

        return ResponseEntity.noContent().build();
    }


}
