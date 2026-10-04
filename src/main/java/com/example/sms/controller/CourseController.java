package com.example.sms.controller;

import com.example.sms.dto.CourseRequestDto;
import com.example.sms.dto.CourseResponseDto;
import com.example.sms.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<CourseResponseDto> create(@Valid @RequestBody CourseRequestDto courseRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(courseRequestDto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<CourseResponseDto> update(@PathVariable Long id, @Valid @RequestBody CourseRequestDto courseRequestDto) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseRequestDto));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}