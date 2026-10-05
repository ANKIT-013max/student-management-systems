package com.example.sms.service;

import com.example.sms.dto.DepartmentRequestDto;
import com.example.sms.dto.DepartmentResponseDto;


public interface DepartmentService {

    DepartmentResponseDto createDepartment(DepartmentRequestDto dto);

    DepartmentResponseDto getDepartmentById(Long id);

    DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto dto);

    void deleteDepartment(Long id);
}