package com.example.sms.controller;

import com.example.sms.dto.DepartmentRequestDto;
import com.example.sms.dto.DepartmentResponseDto;

import com.example.sms.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor

public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/create")
    public ResponseEntity<DepartmentResponseDto> create(@Valid @RequestBody DepartmentRequestDto departmentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(departmentRequestDto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<DepartmentResponseDto> update(
            @PathVariable Long id, @Valid @RequestBody DepartmentRequestDto departmentRequestDto) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentRequestDto));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}