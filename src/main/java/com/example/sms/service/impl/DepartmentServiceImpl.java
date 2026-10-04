package com.example.sms.service.impl;

import com.example.sms.dto.DepartmentRequestDto;
import com.example.sms.dto.DepartmentResponseDto;
import com.example.sms.entity.Department;
import com.example.sms.exception.ConflictException;
import com.example.sms.exception.DuplicateResourceException;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.CourseRepository;
import com.example.sms.repository.DepartmentRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository  studentRepository;
    private final CourseRepository courseRepository;


    // CREATE
    @Override
    @Transactional
    public DepartmentResponseDto createDepartment(DepartmentRequestDto departmentRequestDto) {

        log.info("Creating department with code: {}", departmentRequestDto.getCode());

        if (departmentRepository.existsByNameIgnoreCase(departmentRequestDto.getName().trim())) {
            throw new DuplicateResourceException("Department already exists with name : " + departmentRequestDto.getName());
        }
        if (departmentRepository.existsByCodeIgnoreCase(departmentRequestDto.getCode().trim())) {
            throw new DuplicateResourceException("Department already exists with code : " + departmentRequestDto.getCode());
        }

        Department departmentBuild = Department.builder()
                .name(departmentRequestDto.getName())
                .code(departmentRequestDto.getCode())
                .description(departmentRequestDto.getDescription())
                .build();

        Department saved = departmentRepository.save(departmentBuild);

        return entityToDto(saved);
    }


    // GET BY ID
    @Override
    public DepartmentResponseDto getDepartmentById(Long id) {
        log.info("Getting department with id: {}", id);
        return entityToDto(findDepartmentById(id));
    }


    // UPDATE
    @Override
    @Transactional
    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto departmentRequestDto) {
        log.info("Updating department with ID: {}", id);

        Department department = findDepartmentById(id);

        if (departmentRepository.existsByNameIgnoreCaseAndIdNot(departmentRequestDto.getName().trim(), id)) {
            throw new DuplicateResourceException("Another department already exists with name : " + departmentRequestDto.getName());
        }
        if (departmentRepository.existsByCodeIgnoreCaseAndIdNot(departmentRequestDto.getCode().trim(), id)) {
            throw new DuplicateResourceException("Another department already exists with code : " + departmentRequestDto.getCode());
        }

        department.setName(departmentRequestDto.getName().trim());
        department.setCode(departmentRequestDto.getCode().trim().toUpperCase());
        department.setDescription(departmentRequestDto.getDescription());

        return entityToDto(departmentRepository.save(department));
    }


    // DELETE
    @Override
    public void deleteDepartment(Long id) {

        log.info("Deleting department with ID: {}", id);

        Department department = findDepartmentById(id);

        if (studentRepository.existsByDepartmentId(id)) {
            throw new ConflictException("Cannot delete department: students are still assigned to it");
        }
        if (courseRepository.existsByDepartmentId(id)) {
            throw new ConflictException("Cannot delete department: it still has courses");
        }

        departmentRepository.delete(department);
    }



    // HELPER METHODS
    public Department findDepartmentById(Long id) {
        log.info("Finding department with id: {}", id);
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found : " + id));
    }

    // entity -> dto
    private DepartmentResponseDto entityToDto(Department d) {
        return DepartmentResponseDto.builder()
                .id(d.getId())
                .name(d.getName())
                .code(d.getCode())
                .description(d.getDescription())
                .build();
    }
}
